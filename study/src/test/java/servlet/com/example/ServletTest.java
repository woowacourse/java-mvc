package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import support.HttpUtils;

class ServletTest {

    private final String WEBAPP_DIR_LOCATION = "src/main/webapp/";

    @Test
    void testSharedCounter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        final var response = HttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        // 답변 :
        // tomcat 내부에서 init() 메서드를 호출하면 SharedCounterServlet 내부 필드인 sharedCount가 0으로 초기화 됨
        // service 메서드 호출 할 때마다, 톰켓 내부에서 이미 생성된 서블릿(SharedCounterServlet)에서 요청을 처리하므로
        // 호출 횟수만큼 sharedCount가 증가함
        assertThat(Integer.parseInt(response.body())).isEqualTo(3);
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
        // 답변
        // doService()를 호출할 때마다 localCount 변수를 초기화 하고, 숫자를 증가시켰기 때문이다.
        // servlet 내부 필드를 사용하는 것이 아니므로 호출할 때 마다 결과값이 1이 나타나게 됨
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
