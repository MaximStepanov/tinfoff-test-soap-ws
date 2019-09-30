package com.stepanov.tinkoff.testsoap.webservice;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Class with Bean definitions configuration
 */
@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter {

  private static final String TARGET_NAMESPACE = "http://testsoap.tinkoff.stepanov.com/ws";

  /**
   * It is used for handling SOAP requests. {@link ApplicationContext} is injecting to this servlet
   * so that Spring-WS find other beans. It also declares the URL mapping for the requests.
   *
   * @param applicationContext - Application context
   * @return - {@link ServletRegistrationBean} with context injections
   */
  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      final ApplicationContext applicationContext) {
    final MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  /**
   * {@link DefaultWsdl11Definition} exposes a standard WSDL 1.1 using XsdSchema -
   * "results" will be the wsdl name that will be exposed
   *
   * @param resultSchema - XSD format schema to be exposed
   * @return WSDL 1.1 definition standard
   */
  @Bean(name = "results")
  public DefaultWsdl11Definition defaultWsdl11Definition(final XsdSchema resultSchema) {
    final DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("ResultsPort");
    wsdl11Definition.setLocationUri("/ws");
    wsdl11Definition.setTargetNamespace(TARGET_NAMESPACE);
    wsdl11Definition.setSchema(resultSchema);
    return wsdl11Definition;
  }

  /**
   * Creates XSD schema from the specified file
   *
   * @return {@link SimpleXsdSchema} object
   */
  @Bean
  public XsdSchema resultSchema() {
    return new SimpleXsdSchema(new ClassPathResource("fileNameResults.xsd"));
  }

}
