package com.trace.app.dto.response

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "SoapResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class SoapResponse(
    @XmlElement(name = "result")
    val result: String?
)