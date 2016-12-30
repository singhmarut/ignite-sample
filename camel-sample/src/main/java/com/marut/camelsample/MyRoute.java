/**
 * Created by marutsingh on 12/30/16.
 */

package com.marut.camelsample;

import javafx.application.Application;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void configure() throws Exception {
        from("direct:foo").routeId("myRoute")
                .to("http://localhost:8080/abc")
                .to("stream:out");
    }
}
