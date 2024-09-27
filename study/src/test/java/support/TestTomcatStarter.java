package support;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

public class TestTomcatStarter {
    public static final String WEBAPP_DIR_LOCATION = "app/src/main/webapp/";

    private final Tomcat tomcat;

    public TestTomcatStarter(final int port) {
        this(WEBAPP_DIR_LOCATION, port);
    }

    public TestTomcatStarter(final String webappDirLocation, final int port) {
        this.tomcat = new Tomcat();
        tomcat.setConnector(createConnector(port));

        final var docBase = new File(webappDirLocation).getAbsolutePath();
        final var context = (StandardContext) tomcat.addWebapp("", docBase);
        skipJarScan(context);
        skipClearReferences(context);
    }

    public void start() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
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
        context.setClearReferencesObjectStreamClassCaches(false);
        context.setClearReferencesRmiTargets(false);
        context.setClearReferencesThreadLocals(false);
    }
}
