package dev.timetable.spring.graphql.interceptor;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.ExecutionGraphQlResponse;
import org.springframework.graphql.server.WebGraphQlInterceptor.Chain;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.support.DefaultExecutionGraphQlResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TransactionalGraphQlInterceptorの単体テスト.
 */
@ExtendWith(MockitoExtension.class)
class TransactionalGraphQlInterceptorTest {

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    @Mock
    private Chain chain;

    private TransactionalGraphQlInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TransactionalGraphQlInterceptor(transactionManager);
    }

    @Test
    void mutationWithSuccess_shouldCommitTransaction() {
        // Given
        String mutationQuery = "mutation { upsertSchoolDays(input: {}) { id } }";
        WebGraphQlRequest request = createWebGraphQlRequest(mutationQuery);

        ExecutionResult executionResult = ExecutionResultImpl.newExecutionResult()
                .data(Map.of("upsertSchoolDays", List.of()))
                .build();

        WebGraphQlResponse response = createWebGraphQlResponse(request, executionResult);

        when(transactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(transactionStatus);
        when(chain.next(request)).thenReturn(Mono.just(response));

        // When
        interceptor.intercept(request, chain).block();

        // Then
        verify(transactionManager).getTransaction(any(TransactionDefinition.class));
        verify(transactionManager).commit(transactionStatus);
        verify(transactionManager, never()).rollback(any());
    }

    @Test
    void mutationWithError_shouldRollbackTransaction() {
        // Given
        String mutationQuery = "mutation { upsertSchoolDays(input: {}) { id } }";
        WebGraphQlRequest request = createWebGraphQlRequest(mutationQuery);

        GraphQLError error = GraphqlErrorBuilder.newError()
                .message("Test error")
                .build();

        ExecutionResult executionResult = ExecutionResultImpl.newExecutionResult()
                .errors(List.of(error))
                .build();

        WebGraphQlResponse response = createWebGraphQlResponse(request, executionResult);

        when(transactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(transactionStatus);
        when(chain.next(request)).thenReturn(Mono.just(response));

        // When
        interceptor.intercept(request, chain).block();

        // Then
        verify(transactionManager).getTransaction(any(TransactionDefinition.class));
        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(any());
    }

    @Test
    void mutationWithException_shouldRollbackTransaction() {
        // Given
        String mutationQuery = "mutation { upsertSchoolDays(input: {}) { id } }";
        WebGraphQlRequest request = createWebGraphQlRequest(mutationQuery);

        when(transactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(transactionStatus);
        when(chain.next(request)).thenReturn(Mono.error(new RuntimeException("Test exception")));

        // When / Then
        try {
            interceptor.intercept(request, chain).block();
        } catch (RuntimeException e) {
            // Expected exception
        }

        // Then
        verify(transactionManager).getTransaction(any(TransactionDefinition.class));
        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(any());
    }

    @Test
    void query_shouldNotStartTransaction() {
        // Given
        String queryString = "query { schoolDays(input: {}) { id } }";
        WebGraphQlRequest request = createWebGraphQlRequest(queryString);

        ExecutionResult executionResult = ExecutionResultImpl.newExecutionResult()
                .data(Map.of("schoolDays", List.of()))
                .build();

        WebGraphQlResponse response = createWebGraphQlResponse(request, executionResult);

        when(chain.next(request)).thenReturn(Mono.just(response));

        // When
        interceptor.intercept(request, chain).block();

        // Then
        verify(transactionManager, never()).getTransaction(any());
        verify(transactionManager, never()).commit(any());
        verify(transactionManager, never()).rollback(any());
    }

    @SuppressWarnings("removal")
    private WebGraphQlRequest createWebGraphQlRequest(String document) {
        URI uri = URI.create("http://localhost:8080/graphql");
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = Map.of(
                "query", document,
                "variables", Collections.emptyMap()
        );
        return new WebGraphQlRequest(uri, headers, null, Collections.emptyMap(), body, "1", null);
    }

    private WebGraphQlResponse createWebGraphQlResponse(WebGraphQlRequest request, ExecutionResult executionResult) {
        ExecutionGraphQlResponse executionResponse = new DefaultExecutionGraphQlResponse(
                request.toExecutionInput(),
                executionResult
        );
        return new WebGraphQlResponse(executionResponse);
    }
}
