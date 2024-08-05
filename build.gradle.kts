plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "az.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")

	//Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	//liquibase
//    implementation("org.liquibase:liquibase-core")
	//Data
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//security
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
    implementation("org.springframework.security:spring-security-core:6.2.4")
	implementation("org.springframework.security:spring-security-config:6.2.4")
	//mail sender
	implementation("org.springframework.boot:spring-boot-starter-mail:3.3.2")
	//pdf
	implementation("com.itextpdf:itext7-core:7.1.14")
	implementation("org.apache.commons:commons-io:1.3.2")
	//testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-junit-jupiter:3.11.2")
	testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
	testImplementation("io.github.benas:random-beans:3.9.0")
	testImplementation("org.hamcrest:hamcrest-library:2.2")
	//mapstruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	//validation
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.4")
	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	//JsonB
	//implementation("com.vladmihalcea:hibernate-types-52:2.21.1")
	//Hibernate
	implementation("org.hibernate:hibernate-core:6.0.0.Final")
	//amazon s3
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.470")
	//persistence
	//implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
	// rabbitMQ
//	implementation("org.springframework.boot:spring-boot-starter-amqp:1.4.0.RELEASE")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
