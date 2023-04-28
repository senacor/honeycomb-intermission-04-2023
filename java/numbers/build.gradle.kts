import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension


plugins {
    java
    kotlin("jvm") version "1.8.21"
    id("org.springframework.boot") version "3.0.6"
}

apply(plugin = "io.spring.dependency-management")


group = "com.senacor.innolab.honeycomb"
version = "1.0-SNAPSHOT"

val jvmVersion: JavaToolchainSpec.() -> Unit = {
    languageVersion.set(JavaLanguageVersion.of(17))
}

java {
    toolchain {
        jvmVersion()
    }
}

kotlin {
    jvmToolchain {
        jvmVersion()
    }
}

tasks {
    bootJar {
        archiveFileName.set("numbers.jar")
        mainClass.set("com.senacor.innolab.honeycomb.MainKt")
    }

    register<Copy>("copyAgent") {
        from(agent.files)
        into(project.buildDir)
    }

    withType<Jar> {
        dependsOn("copyAgent")
    }
}
repositories {
    mavenCentral()
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.2")
        mavenBom("io.opentelemetry:opentelemetry-bom:1.25.0")
    }
}

val agent by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-jdbc")
    implementation("org.postgresql", "postgresql")
    implementation("io.micrometer", "micrometer-observation")
    implementation("io.micrometer", "micrometer-tracing")
    implementation("io.micrometer", "micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry", "opentelemetry-exporter-otlp")
    implementation("io.opentelemetry", "opentelemetry-sdk")
    implementation("io.opentelemetry", "opentelemetry-sdk-trace")
    agent("io.opentelemetry.javaagent", "opentelemetry-javaagent", "1.25.0")
    implementation("io.opentelemetry.instrumentation", "opentelemetry-jdbc", "1.25.0-alpha")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
