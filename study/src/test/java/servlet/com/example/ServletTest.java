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

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        // -> 하나의 서블릿 인스턴스를 3개의 쓰레드(요청이 3번이니)에서 공유했기 때문
        // -> 즉 3개의 쓰레드에서 메서드를 통해 하나의 전역 변수를 변경
        // -> 톰캣은 시작 시점에서 등록한 모든 서블릿을 인스턴스화 한다. (요청 시점이 아니다.)
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
        // 로컬 변수이므로 메서드가 호출될 때마다 할당 되기 때문이다.
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
