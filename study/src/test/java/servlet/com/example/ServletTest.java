package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import support.HttpUtils;
import support.TestTomcatStarter;

class ServletTest {

    private final String WEBAPP_DIR_LOCATION = "src/main/webapp/";

    @Test
    void testSharedCounter() {
        // 톰캣 서버 시작
        int port = 8083;
        final var tomcatStarter = new TestTomcatStarter(WEBAPP_DIR_LOCATION, port);
        tomcatStarter.start();

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";
        HttpUtils.send(PATH, port);
        HttpUtils.send(PATH, port);
        final var response = HttpUtils.send(PATH, port);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        //서블릿 계속 살아있으니깐 클래스 전역변수가 계속 바뀜 실행 될 떄마다 3번씩 호출됨
        assertThat(Integer.parseInt(response.body())).isEqualTo(3);
    }

    @Test
    void testLocalCounter() {
        // 톰캣 서버 시작
        int port = 8082;
        final var tomcatStarter = new TestTomcatStarter(WEBAPP_DIR_LOCATION, port);
        tomcatStarter.start();

        // local-counter 페이지를 3번 호출한다.
        final var PATH = "/local-counter";
        HttpUtils.send(PATH, port);
        HttpUtils.send(PATH, port);
        final var response = HttpUtils.send(PATH, port);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
