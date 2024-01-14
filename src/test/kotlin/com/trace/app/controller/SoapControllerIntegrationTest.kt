package com.trace.app.controller

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter
import io.opentelemetry.sdk.trace.data.SpanData
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import kotlin.test.assertEquals
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import kotlin.test.assertTrue


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class SoapControllerIntegrationTest {

    private val logger = LoggerFactory.getLogger(SoapController::class.java) as Logger
    private val listAppender = ListAppender<ILoggingEvent>()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Test
    fun `soapGETEndpoint should process SOAP request and include TraceId in logs`() {
        // Arrange

        // Retrieve the MEMORY appender and set it to listAppender
        listAppender.start()
        logger.addAppender(listAppender)

        // Set up MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()


        // Set up InMemorySpanExporter
        val inMemorySpanExporter = InMemorySpanExporter.create()
        val spanProcessor = SimpleSpanProcessor.create(inMemorySpanExporter)
        val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .build()

        // Set the tracer provider for your tracer
        val tracer: Tracer = tracerProvider.get("your-instrumentation-name")

        // Set the span processor on the TracerProvider
        SdkTracerProvider.builder().addSpanProcessor(spanProcessor).build()

        // Act
        val span: Span = tracer.spanBuilder("your-span-name").startSpan()

        // You can set attributes, events, etc., on the span as needed
        span.setAttribute("key", "value")

        // Perform your SOAP request
        mockMvc.perform(get("/api"))
            .andExpect(status().isOk)

        // End the span when you are done with the operation
        span.end()

        // Assert
        val spans: List<SpanData> = inMemorySpanExporter.finishedSpanItems
        assertEquals(1, spans.size, "Expected one span")

        // Verify logs
        val logs = listAppender.list.joinToString("\n") { it.formattedMessage }
        assertTrue(logs.contains("TraceId"), "TraceId should be present in the logs")

        // Clean up
        listAppender.list.clear()
        inMemorySpanExporter.reset()
    }
}
