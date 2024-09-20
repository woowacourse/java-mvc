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
        tomcatStarter.start(); // 이 때 웹서버가 시작되면서 서블릿 컨테이너가 서블릿 객체를 생성하므로 init() 메서드가 호출되는 것으로 보인다.

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        final var response = HttpUtils.send(PATH);

        // 톰캣 서버 종료 -> 이 때 destroy 메서드가 호출되는 것으로 보인다.
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        // 답 : 서블릿 객체는 서블릿 컨테이너가 단 하나만 생성하기 때문에 /shared-counter라는 url로 오는 유저 요청은
        // 모두 같은 서블릿 객체를 사용한다. 따라서 모든 사용자 요청이 sharedCounter 라는 필드를 공유하게 되므로 3이라는 값이 나온다.
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
        // 답 : 사용자 요청마다 서로 다른 스택 메모리를 사용하므로, 같은 서블릿 객체를 사용하더라도 지역 변수는 공유가 되지 않기 때문
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
