package servlet.com.example;

import org.junit.jupiter.api.Test;
import support.HttpUtils;

import static org.assertj.core.api.Assertions.assertThat;

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

        // 하나의 서블릿 인스턴스가 세 요청에서 같은 필드를 사용한다.
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

        // 요청마다 service()의 지역 변수는 새로 만들어진다.
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
