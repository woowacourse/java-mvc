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
        // testSharedCounter()는 첫 요청시 init()이 한 번 호출되어 sharedCounter가 0으로 초기화된다.
        // 이후 같은 서블릿 인스턴스에서 요청마다 service()가 호출되어 sharedCounter가 증가된다.
        // 총 3번 호출되었기 때문에 마지막 응답 값이 3이다.
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
        // localCounter는 service() 내부의 지역 변수기 때문에 요청마다 0으로 초기화된다.
        // service()에서 1 증가시킨 값을 응답하기 때문에 매 요청의 응답 값은 1이다.
        // 따라서 총 3번의 요청이여도 마지막 응답 값은 1이다.
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
