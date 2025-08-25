package servlet.com.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;

public class TomcatStarter {

    private static final String WEBAPP_DIR_LOCATION = "study/src/main/webapp/";

    private final Tomcat tomcat;

    public TomcatStarter() {
        this(WEBAPP_DIR_LOCATION);
    }

    public TomcatStarter(final String webappDirLocation) {
        this.tomcat = new Tomcat();
        tomcat.setConnector(createConnector());

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

    public void await() {
        tomcat.getServer().await();
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private Connector createConnector() {
        final var connector = new Connector();
        connector.setPort(8080);
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
         * 테스트 코드는 메모리 누수 문제 없으니 관련 설정을 끈다.
         */
        context.setClearReferencesRmiTargets(false);
        context.setClearReferencesThreadLocals(false);
    }
}
