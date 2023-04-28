package com.senacor.innolab.honeycomb

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.trace.export.SpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
open class IntegerNumberApplication

@Configuration
open class ObservabilityConfiguration : BeanPostProcessor {

    @Bean
    open fun openTelemetry(): OpenTelemetry =
        GlobalOpenTelemetry.get()

    @Bean
    open fun spanExporter(
        @Value("\${management.otlp.tracing.endpoint}") endpoint: String,
        ctx: ApplicationContext
    ): SpanExporter =
        OtlpGrpcSpanExporter.builder()
            .setEndpoint(endpoint)
            .build()
}

fun main(args: Array<String>) {
    runApplication<IntegerNumberApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
