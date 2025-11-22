FROM eclipse-temurin:21-noble

RUN apt update && apt install -y \
    nodejs \
    npm \
    zsh \
    git \
    curl \
    && apt clean

# zsh
RUN sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" "" --unattended
ENV SHELL=/bin/zsh
CMD ["zsh"]

CMD ["./gradlew","bootRun","--continuous"]
