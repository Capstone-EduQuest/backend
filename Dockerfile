# build stage
FROM gradle:9.4.1-jdk25 AS build

WORKDIR /app

# 필요한 모든 빌드 파일 복사
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

# CRLF를 LF로 변환 후 실행 권한 부여
RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

# 의존성만 다운로드 (실패해도 다음 단계에서 소스 복사 후 재빨리 빌드)
ARG SKIP_TESTS=false
RUN ./gradlew --no-daemon dependencies || true

# 소스 코드 복사 후 빌드
COPY src ./src

RUN ./gradlew clean build -x test

# final stage
FROM amazoncorretto:25

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]