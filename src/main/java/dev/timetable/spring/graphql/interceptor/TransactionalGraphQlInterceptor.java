package dev.timetable.spring.graphql.interceptor;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.OperationDefinition;
import graphql.parser.InvalidSyntaxException;
import graphql.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * GraphQLリクエストに対してトランザクション管理を行うインターセプター.
 * 
 * <p>mutationリクエストの場合、リクエスト開始時にトランザクションを開始し、
 * すべてのリゾルバがエラーなく終了した場合はコミット、エラーが発生した場合はロールバックする。</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionalGraphQlInterceptor implements WebGraphQlInterceptor {

    private final PlatformTransactionManager transactionManager;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (isMutation(request)) {
            return executeWithTransaction(request, chain);
        }
        return chain.next(request);
    }

    /**
     * リクエストがmutationかどうかを判定する.
     *
     * <p>GraphQLドキュメントのパースに失敗した場合は、falseを返し、
     * 通常のGraphQLバリデーションに処理を委ねる。</p>
     *
     * @param request GraphQLリクエスト
     * @return mutationの場合はtrue
     */
    private boolean isMutation(WebGraphQlRequest request) {
        try {
            String document = request.getDocument();
            Document parsedDocument = Parser.parse(document);
            
            for (Definition<?> definition : parsedDocument.getDefinitions()) {
                if (definition instanceof OperationDefinition operationDefinition) {
                    if (operationDefinition.getOperation() == OperationDefinition.Operation.MUTATION) {
                        return true;
                    }
                }
            }
            return false;
        } catch (InvalidSyntaxException e) {
            // パースに失敗した場合は、通常のGraphQLバリデーションに処理を委ねる
            log.debug("GraphQL document parse failed, delegating to normal validation", e);
            return false;
        }
    }

    /**
     * トランザクション内でGraphQLリクエストを実行する.
     *
     * <p>トランザクションのスレッドコンテキストを維持するため、
     * boundedElasticスケジューラを使用して同一スレッドで実行する。</p>
     *
     * @param request GraphQLリクエスト
     * @param chain インターセプターチェーン
     * @return GraphQLレスポンス
     */
    private Mono<WebGraphQlResponse> executeWithTransaction(WebGraphQlRequest request, Chain chain) {
        return Mono.fromCallable(() -> {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            
            TransactionStatus status = transactionManager.getTransaction(def);
            
            try {
                WebGraphQlResponse response = chain.next(request).block();
                
                if (response != null && hasErrors(response)) {
                    transactionManager.rollback(status);
                } else if (response != null) {
                    transactionManager.commit(status);
                }
                
                return response;
            } catch (Exception e) {
                transactionManager.rollback(status);
                throw e;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * レスポンスにエラーが含まれているかを判定する.
     *
     * @param response GraphQLレスポンス
     * @return エラーがある場合はtrue
     */
    private boolean hasErrors(WebGraphQlResponse response) {
        return response.getExecutionResult().getErrors() != null 
            && !response.getExecutionResult().getErrors().isEmpty();
    }
}
