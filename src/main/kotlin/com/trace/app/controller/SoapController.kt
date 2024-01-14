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
class SoapController(private val tracer: Tracer) {

    private val logger = LoggerFactory.getLogger(SoapController::class.java)

    @GetMapping("/soap")
    fun soapGETEndpoint(): ResponseEntity<String> {
        val span = tracer.spanBuilder("soap-endpoint").startSpan()
        try {
            logWithTraceId("Processing SOAP request")

            // SOAP processing logic here...

            val response = "Dummy Response"
            logWithTraceId("Sending SOAP response: $response")
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Error processing SOAP request", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing SOAP request")
        } finally {
            span.end()
        }
    }

    @PostMapping("/soap")
    fun soapPOSTEndpoint(@RequestBody request: SoapRequest): SoapResponse {
        val span = tracer.spanBuilder("soap-endpoint").startSpan()
        try {
            logWithTraceId("Processing SOAP request: $request")

            // SOAP processing logic here...

            val response = SoapResponse("Dummy Response")
            logWithTraceId("Sending SOAP response: $response")

            return response
        } catch (e: Exception) {
            logger.error("Error processing SOAP request", e)
            throw Exception("INTERNAL_SERVER_ERROR")
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
