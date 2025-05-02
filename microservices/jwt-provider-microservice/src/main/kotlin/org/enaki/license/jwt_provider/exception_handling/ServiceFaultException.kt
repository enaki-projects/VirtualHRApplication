package org.enaki.license.jwt_provider.exception_handling

class ServiceFaultException : RuntimeException {
    var serviceFault: ServiceFault

    constructor(message: String?, serviceFault: ServiceFault) : super(message) {
        this.serviceFault = serviceFault
    }

    constructor(message: String?, e: Throwable?, serviceFault: ServiceFault) : super(message, e) {
        this.serviceFault = serviceFault
    }
}