package com.trace.app.config

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.samplers.Sampler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter

@Configuration
class OpenTelemetryConfig {

    @Bean
    fun tracer(): Tracer {
        return OpenTelemetrySdk.builder().build().tracerProvider.get("demo-app", "1.0.0")
    }
    @Bean
    fun jaxb2RootElementHttpMessageConverter(): Jaxb2RootElementHttpMessageConverter {
        return Jaxb2RootElementHttpMessageConverter()
    }

//    @Bean
//    fun tracer(): Tracer {
//        return GlobalOpenTelemetry.getTracer("soap-api-tracer")
//    }

//    @Bean
//    fun openTelemetry(): OpenTelemetry {
//        val sdk = OpenTelemetrySdk.builder()
//            .setTracerProvider(SdkTracerProvider.builder().setSampler(Sampler.alwaysOn()).build())
//            .setLoggerProvider(
//                SdkLoggerProvider.builder()
//                    .setResource(
//                        Resource.getDefault().toBuilder()
//                            .put(ResourceAttributes.SERVICE_NAME, "soap-api-example")
//                            .build()
//                    )
//                    .addLogRecordProcessor(
//                        BatchLogRecordProcessor.builder(
//                            OtlpGrpcLogRecordExporter.builder()
//                                .setEndpoint("http://localhost:4317")
//                                .build()
//                        ).build()
//                    )
//                    .build()
//            )
//            .build()
//
//        Runtime.getRuntime().addShutdownHook(Thread(sdk::close))
//
//        return sdk
//    }

}
