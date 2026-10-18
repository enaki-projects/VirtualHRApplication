package org.enaki.license.jwt_provider.exception_handling

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlType


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = ["code", "description"])
class ServiceFault {
    var code: String? = null
    var description: String? = null

    constructor() {}
    constructor(code: String?, description: String?) {
        this.code = code
        this.description = description
    }
}