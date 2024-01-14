package com.trace.app.controller

import com.trace.app.dto.request.SoapRequest
import com.trace.app.dto.response.SoapResponse
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SoapController(private val tracer: Tracer) {

    private val logger = LoggerFactory.getLogger(SoapController::class.java)

    @GetMapping
    fun soapGETEndpoint(): String {
        val span = tracer.spanBuilder("soap-endpoint").startSpan()
        try {
            logWithTraceId("Processing SOAP request")

            //SOAP processing logic here...

            val response = "Dummy Response"
            logWithTraceId("Sending SOAP response: $response")

            return response
        } finally {
            span.end()
        }
    }

    @PostMapping
    fun soapPOSTEndpoint(@RequestBody request: SoapRequest): SoapResponse {
        val span = tracer.spanBuilder("soap-endpoint").startSpan()
        try {
            logWithTraceId("Processing SOAP request: $request")

            //SOAP processing logic here...

            val response = SoapResponse("Dummy Response")
            logWithTraceId("Sending SOAP response: $response")

            return response
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
