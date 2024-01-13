package com.trace.app.dto.request

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "SoapRequest", namespace = "http://www.example.com/")
@XmlAccessorType(XmlAccessType.FIELD)
data class SoapRequest(
    @XmlElement(name = "param", namespace = "http://www.example.com/")
    val param: String
)
