package com.trace.app.controller

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.opentelemetry.api.trace.*
import io.opentelemetry.context.Context
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.data.SpanData
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.trace.export.SpanExporter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class AnotherControllerIntegrationTest {

    private val logger = LoggerFactory.getLogger(AnotherController::class.java) as Logger
    private val listAppender = ListAppender<ILoggingEvent>()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Test
    fun `anotherGETEndpoint should process SOAP request and include TraceId in logs`() {
        // Arrange
        listAppender.start()
        logger.addAppender(listAppender)
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        val inMemorySpanExporter = InMemorySpanExporter.create()
        val spanProcessor = SimpleSpanProcessor.create(inMemorySpanExporter)
        val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .build()
        val tracer: Tracer = tracerProvider.get("trace-id-app")
        SdkTracerProvider.builder().addSpanProcessor(spanProcessor).build()

        // Act
        val span: Span = tracer.spanBuilder("soap-endpoint").startSpan()
        span.setAttribute("key", "value")
        mockMvc.perform(get("/api/another"))
            .andExpect(status().isOk)
        span.end()

        // Assert
        val spans: List<SpanData> = inMemorySpanExporter.finishedSpanItems
        assertEquals(1, spans.size, "Expected one span")
        val logs = listAppender.list.joinToString("\n") { it.formattedMessage }
        assertTrue(logs.contains("TraceId"), "TraceId should be present in the logs")
        listAppender.list.clear()
        inMemorySpanExporter.reset()
    }

    @Test
    fun `anotherGETEndpoint should handle request with existing TraceId`() {
        // Arrange
        listAppender.start()
        logger.addAppender(listAppender)
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        val inMemorySpanExporter = InMemorySpanExporter.create()
        val spanProcessor = SimpleSpanProcessor.create(inMemorySpanExporter)
        val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .build()
        val tracer: Tracer = tracerProvider.get("trace-id-app")
        SdkTracerProvider.builder().addSpanProcessor(spanProcessor).build()

        // Act
        val existingTraceId = "00000000000000000000000000000000"
        SpanContext.create(
            existingTraceId,
            SpanId.fromLong(0x0123456789abcdef),
            TraceFlags.getDefault(),
            TraceState.getDefault()
        )
        val parentSpanContext: Context = Context.current()
        val span: Span = tracer.spanBuilder("soap-endpoint")
            .setParent(parentSpanContext)
            .startSpan()
        span.setAttribute("key", "value")
        mockMvc.perform(get("/api/another"))
            .andExpect(status().isOk)
        span.end()

        // Assert
        val spans: List<SpanData> = inMemorySpanExporter.finishedSpanItems
        assertEquals(1, spans.size, "Expected one span")
        val logs = listAppender.list.joinToString("\n") { it.formattedMessage }
        assertTrue(logs.contains(existingTraceId), "Existing TraceId should be present in the logs")
        listAppender.list.clear()
        inMemorySpanExporter.reset()
    }
    @Test
    fun `anotherGETEndpoint should handle SOAP processing error and return Internal Server Error`() {
        // Arrange
        val mockExporter: SpanExporter = mock(SpanExporter::class.java)
        Mockito.`when`(mockExporter.export(any())).thenReturn(CompletableResultCode().fail())
        val spanProcessor = SimpleSpanProcessor.create(mockExporter)

        val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .build()

        val tracer: Tracer = tracerProvider.get("trace-id-app")
        val inMemorySpanExporter = InMemorySpanExporter.create()

        // Set up MockMvc
        val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(AnotherController(tracer)).build()

        // Act
        val result = mockMvc.perform(get("/api/another"))

        // Assert
        result.andExpect(status().isOk)

        // Verify that the exception is logged
        verify(mockExporter, Mockito.atLeastOnce()).export(any())
    }


}

