import org.gradle.kotlin.dsl.implementation

plugins {
	java
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.eduquest"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
	maven {
		name = "Central Portal Snapshots"
		url = uri("https://central.sonatype.com/repository/maven-snapshots/")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-restclient")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation ("org.springframework.boot:spring-boot-starter-validation:4.0.5")
	implementation ("org.springframework.boot:spring-boot-starter-mail:4.0.5")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1")
	// aop starter removed to avoid resolution issues in this environment
	implementation("org.springframework.boot:spring-boot-flyway:4.0.5")
	// spring boot 4부터 flyway-core 필수 요구
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	implementation("io.hypersistence:hypersistence-utils-hibernate-71:3.15.2")
	implementation("software.amazon.awssdk:s3:2.42.23")
	implementation("io.github.openfeign.querydsl:querydsl-jpa-spring:7.1")
	implementation(platform("org.springframework.ai:spring-ai-bom:1.1.4"))
	implementation ("org.springframework.ai:spring-ai-openai")
	implementation ("org.springframework.ai:spring-ai-starter-model-google-genai")
	implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20260313.1")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor ("io.github.openfeign.querydsl:querydsl-apt:7.1:jpa")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Mockito for unit testing
	testImplementation("org.mockito:mockito-core:5.5.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

