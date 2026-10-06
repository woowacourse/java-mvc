package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.HttpUtils;

class ServletTest {

    private static final Logger log = LoggerFactory.getLogger(ServletTest.class);
    private final String WEBAPP_DIR_LOCATION = "src/main/webapp/";

    @Test
    void testSharedCounter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();
//        10월 06, 2026 11:05:36 오전 org.apache.coyote.AbstractProtocol init
//            -> AbstractProtocol init은 톰캣 통신 장치 초기화를 하는 부분
//        INFO: Initializing ProtocolHandler ["http-nio-8080"]
//            -> http 프로토콜을 사용하며 Network IO를 사용하는 방식의 통신을 8080 포트에서 열었다.

//        10월 06, 2026 11:05:36 오전 org.apache.catalina.core.StandardService startInternal
//        INFO: Starting service [Tomcat]
//        10월 06, 2026 11:05:36 오전 org.apache.catalina.core.StandardEngine startInternal
//        INFO: Starting Servlet engine: [Apache Tomcat/11.0.10]
//        -> Engine: 들어온 요청을 적절한 웹 애플리케이션으로 연결하는 역할을 맡는다
//        10월 06, 2026 11:05:36 오전 org.apache.catalina.startup.ContextConfig getDefaultWebXmlFragment
//        INFO: No global web.xml found
//        10월 06, 2026 11:05:36 오전 org.apache.coyote.AbstractProtocol start
//        INFO: Starting ProtocolHandler ["http-nio-8080"]

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";
        log.info("첫 번째 shared-counter 호출");
        HttpUtils.send(PATH);
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: init() 호출 -> ?? 호출을 해야 그 때 init()이 수행된다. 미리 모두 로드하지 않고, 필요한 것만 사용하도록 의도한 거로 생각됨
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: doFilter() 호출
//            -> 요청/응답에 공통 처리를 적용하는 필터가 호출되었다.
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: service() 호출
//            -> 서블릿이 요청을 처리하는 service() 메서드가 호출됨

        log.info("두 번째 shared-counter 호출");
        HttpUtils.send(PATH);
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: doFilter() 호출
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: service() 호출
        log.info("세 번째 shared-counter 호출");
        final var response = HttpUtils.send(PATH);
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: doFilter() 호출
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: service() 호출

        // 톰캣 서버 종료
        log.info("톰캣 종료");
        tomcatStarter.stop();
//        10월 06, 2026 11:01:23 오전 org.apache.coyote.AbstractProtocol pause
//        INFO: Pausing ProtocolHandler ["http-nio-8080"]
//          -> 새 요청을 받아들이는 Handler 동작을 중지시킨다
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.StandardService stopInternal
//        INFO: Stopping service [Tomcat]
//        10월 06, 2026 11:01:23 오전 org.apache.catalina.core.ApplicationContext log
//        INFO: destroy() 호출
//        10월 06, 2026 11:01:23 오전 org.apache.coyote.AbstractProtocol stop
//        INFO: Stopping ProtocolHandler ["http-nio-8080"]
//        10월 06, 2026 11:01:23 오전 org.apache.coyote.AbstractProtocol destroy
//        INFO: Destroying ProtocolHandler ["http-nio-8080"]

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        assertThat(Integer.parseInt(response.body())).isEqualTo(3);
    }

    @Test
    void testLocalCounter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();

        // local-counter 페이지를 3번 호출한다.
        final var PATH = "/local-counter";
        // 1. local-counter는 메서드 지역 변수를 출력한다.
        // 2. 각 요청은 별도의 스레드에서 처리되고, JVM에서 스레드는 별도의 스택 공간을 갖는다.
        // 3. 스레드 고유의 스택에 지역 변수를 저장하므로, 다른 스레드들과 데이터가 공유되지 않는다.
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        final var response = HttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
