package com.techcourse;

import air.Application;
import air.ApplicationContext;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

public class JwpApplication {

    public static void main(String[] args) throws Exception {
        Application.run(args);
    }
}
