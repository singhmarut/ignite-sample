package com.marut.camelsample;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by marutsingh on 12/30/16.
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@ContextConfiguration(classes = {MyRouteTest.ContextConfiguration.class}, loader = CamelSpringDelegatingTestContextLoader.class)

//ContextConfiguration(classes = {FilterTest.ContextConfig.class}, loader = CamelSpringDelegatingTestContextLoader.class)
public class MyRouteTest  extends AbstractJUnit4SpringContextTests {

    @Configuration
    @ComponentScan
    static class ContextConfiguration {

    }

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;




    @Test
    public void soapUnwrapperTest_usingMock() throws Exception {

        template.getCamelContext().getRouteDefinitions().get(0).adviceWith(template.getCamelContext(), new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // mock only log endpoints
                interceptSendToEndpoint("http://localhost:8080/abc").setBody(simple("Hello World singapore"));
                mockEndpoints("http:*");
            }
        });

        template.sendBody("direct:foo", "Hello World");
        MockEndpoint.assertIsSatisfied(template.getCamelContext());
    }
}
