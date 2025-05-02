package org.enaki.license.jwt_provider.config


import org.enaki.license.jwt_provider.endpoints.AuthEndpoint
import org.enaki.license.jwt_provider.exception_handling.DetailSoapFaultDefinitionExceptionResolver
import org.enaki.license.jwt_provider.exception_handling.ServiceFaultException
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import java.util.*


@EnableWs
@Configuration
class WebServiceConfig : WsConfigurerAdapter() {
    @Bean
    fun exceptionResolver(): SoapFaultMappingExceptionResolver {
        val exceptionResolver: SoapFaultMappingExceptionResolver = DetailSoapFaultDefinitionExceptionResolver()
        val faultDefinition = SoapFaultDefinition()
        faultDefinition.faultCode = SoapFaultDefinition.SERVER
        exceptionResolver.setDefaultFault(faultDefinition)
        val errorMappings = Properties()
        errorMappings.setProperty(Exception::class.java.name, SoapFaultDefinition.SERVER.toString())
        errorMappings.setProperty(ServiceFaultException::class.java.name, SoapFaultDefinition.SERVER.toString())
        exceptionResolver.setExceptionMappings(errorMappings)
        exceptionResolver.order = 1
        return exceptionResolver
    }

    @Bean
    fun messageDispatcherServlet(applicationContext: ApplicationContext?): ServletRegistrationBean<*> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(applicationContext!!)
        servlet.isTransformWsdlLocations = true
        return ServletRegistrationBean(servlet, "/ws/*")
    }

    @Bean(name = ["auth"])
    fun defaultWsdl11Definition(authSchema: XsdSchema?): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("AuthPort")
        wsdl11Definition.setLocationUri("/ws")
        wsdl11Definition.setTargetNamespace(AuthEndpoint.NAMESPACE_URI)
        wsdl11Definition.setSchema(authSchema)
        return wsdl11Definition
    }

    @Bean
    fun authSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("xsd/auth.xsd"))
    }
}