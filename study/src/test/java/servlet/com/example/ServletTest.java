package servlet.com.example;

import org.junit.jupiter.api.Test;
import support.HttpUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ServletTest {

    private static final String WEBAPP_DIR_LOCATION = "src/main/webapp/";

    @Test
    void testSharedCounter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";  // 최초 호출 시 init() 메서드가 호출된다.
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        final var response = HttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();  // 이때 서블릿의 destroy() 메서드가 호출된다.

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        assertThat(Integer.parseInt(response.body())).isEqualTo(3);
        // SharedCounterServlet의 server() 메서드에서 sharedCounter++를 호출하면서 공유 카운터가 증가한다.
        // 서블릿은 하나의 인스턴스를 생성하여 공유하기 때문에 서블릿 인스턴스 변수인 sharedCounter는 여러 요청에 대해 공유된다.
        // 서블릿은 여러 스레드에서 동시에 접근 가능하므로 서블릿에서 인스턴스 변수를 사용할 때는 주의해야 한다.
    }

    @Test
    void testLocalCounter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();

        // local-counter 페이지를 3번 호출한다.
        final var PATH = "/local-counter";
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        final var response = HttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
        // LocalCounterServlet의 service() 메서드에서 메서드 스코프의 지역변수 localCounter를 0으로 초기화하고 1을 증가시킨다.
        // 서블릿은 요청이 올 때마다 새로운 스레드에서 service() 메서드를 호출하므로 지역변수는 요청마다 새로 생성되어 공유되지 않는다.
    }
}
