package com.interface21.webmvc.servlet.mvc;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;
import com.interface21.web.SpringServletContainerInitializer;
import java.util.Set;

public class TomcatStarter {

    public static final String WEBAPP_DIR_LOCATION = "app/src/main/webapp/";

    private final Tomcat tomcat;

    public TomcatStarter(final int port) {
        this(WEBAPP_DIR_LOCATION, port);
    }

    public TomcatStarter(final String webappDirLocation, final int port) {
        this.tomcat = new Tomcat();
        tomcat.setConnector(createConnector(port));

        final var docBase = new File(webappDirLocation).getAbsolutePath();
        final var context = (StandardContext) tomcat.addWebapp("", docBase);

        context.addServletContainerInitializer(new SpringServletContainerInitializer(),
                Set.of(DispatcherServletInitializer.class));
        skipJarScan(context);
        skipClearReferences(context);
    }

    public void start() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new UncheckedServletException(e);
        }
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new UncheckedServletException(e);
        }
    }

    private Connector createConnector(final int port) {
        final var connector = new Connector();
        connector.setPort(port);
        return connector;
    }

    private void skipJarScan(final Context context) {
        final var jarScanner = (StandardJarScanner) context.getJarScanner();
        jarScanner.setScanClassPath(false);
    }

    private void skipClearReferences(final StandardContext context) {
        /**
         * https://tomcat.apache.org/tomcat-11.0-doc/config/context.html
         *
         * ObjectStreamClass와 관련된 메모리 누수는 Java 19 이상, Java 17.0.4 이상 및
         * Java 11.0.16 이상에서 수정되었습니다.
         * Tomcat 11에서는 setClearReferencesObjectStreamClassCaches 메서드가 제거되어
         * Java 21에서는 더 이상 필요하지 않습니다.
         *
         * 학습과 관련 없는 메시지가 나오지 않도록 관련 설정을 끈다.
         */
        context.setClearReferencesRmiTargets(false);
        context.setClearReferencesThreadLocals(false);
    }
}
