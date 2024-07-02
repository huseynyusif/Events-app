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
//    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
//    implementation("org.springframework.security:spring-security-core:6.2.4")
	//mail sender
	implementation("org.springframework.boot:spring-boot-starter-mail")

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
}

tasks.withType<Test> {
	useJUnitPlatform()
}
