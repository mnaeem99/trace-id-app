package com.trace.app.controller

import com.trace.app.dto.request.SoapRequest
import com.trace.app.dto.response.SoapResponse
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AnotherController(private val tracer: Tracer) {

    private val logger = LoggerFactory.getLogger(AnotherController::class.java)

    @GetMapping("/another")
    fun anotherGETEndpoint(): ResponseEntity<String> {
        val span = tracer.spanBuilder("another-endpoint").startSpan()
        try {
            logWithTraceId("Processing Another request")

            // Another processing logic here...

            val response = "Another Dummy Response"
            logWithTraceId("Sending Another response: $response")
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error processing Another request", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Another request")
        } finally {
            span.end()
        }
    }

    @PostMapping("/another")
    fun anotherPOSTEndpoint(@RequestBody request: Any): ResponseEntity<String> {
        val span = tracer.spanBuilder("another-endpoint").startSpan()
        try {
            logWithTraceId("Processing Another request: $request")

            // Another processing logic here...

            val response = "Another Dummy Response"
            logWithTraceId("Sending Another response: $response")
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error processing Another request", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Another request")
        } finally {
            span.end()
        }
    }

    private fun logWithTraceId(message: String) {
        val currentSpan = Span.current()
        currentSpan?.let { span ->
            val traceId = span.spanContext.traceId.toString()
            // Log TraceId along with the message
            logger.info("TraceId: $traceId, Event: log, Message: $message")
        }
    }
}
