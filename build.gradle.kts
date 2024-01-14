import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
}

group = "com.trace"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.21")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.opentelemetry:opentelemetry-api:1.34.1")
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-autoconfigure:0.14.0")
	implementation("io.opentelemetry:opentelemetry-exporters-prometheus:0.9.1")
	testImplementation("io.opentelemetry:opentelemetry-sdk-testing:1.34.1")
	implementation("org.slf4j:slf4j-api:2.0.11")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
	implementation("org.glassfish.jaxb:jaxb-runtime:3.0.1")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("ch.qos.logback:logback-classic")
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
