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
        // 인스턴스 변수가 공유되어 같은 변수에 접근하여 body의 값이 3번 증가하여 3이 된다.
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
        // 메서드가 종료되면 stack 영역에 있는 지역 변수가 해제되기 때문
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
