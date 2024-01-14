package com.trace.app.config

import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.sdk.OpenTelemetrySdk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenTelemetryConfig {

    @Bean
    fun tracer(): Tracer {
        return OpenTelemetrySdk.builder().build().tracerProvider.get("trace-id-app", "1.0.0")
    }

}
