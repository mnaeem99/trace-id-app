package com.trace.app.dto.response

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "SoapResponse", namespace = "http://www.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
data class SoapResponse(
    @XmlElement(name = "result", namespace = "http://www.example.com/")
    val result: String
)