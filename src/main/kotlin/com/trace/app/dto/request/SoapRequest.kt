package com.trace.app.dto.request

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "SoapRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class SoapRequest(
    @XmlElement(name = "orderId")
    val orderId: String?,
    @XmlElement(name = "orderName")
    val orderName: String?
)
