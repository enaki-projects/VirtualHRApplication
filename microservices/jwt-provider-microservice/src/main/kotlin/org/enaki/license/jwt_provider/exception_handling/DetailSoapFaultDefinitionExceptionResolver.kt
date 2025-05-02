package org.enaki.license.jwt_provider.exception_handling

import org.springframework.ws.soap.SoapFault
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver
import javax.xml.namespace.QName


class DetailSoapFaultDefinitionExceptionResolver : SoapFaultMappingExceptionResolver() {
    override fun customizeFault(endpoint: Any, ex: Exception, fault: SoapFault) {
        if (ex is ServiceFaultException){
            logger.warn("Exception processed: Code - '${ex.serviceFault.code}', Description - '${ex.serviceFault.description}'")
        } else {
            logger.warn("Exception processed: Code - '${ex.message}'")
        }
        if (ex is ServiceFaultException) {
            val serviceFault: ServiceFault = ex.serviceFault
            val detail = fault.addFaultDetail()
            detail.addFaultDetailElement(CODE).addText(serviceFault.code)
            detail.addFaultDetailElement(DESCRIPTION).addText(serviceFault.description)
        }
    }

    companion object {
        private val CODE = QName("code")
        private val DESCRIPTION = QName("description")
    }
}