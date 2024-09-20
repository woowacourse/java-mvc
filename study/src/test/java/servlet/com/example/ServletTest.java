package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import support.HttpUtils;

class ServletTest {

    private final String WEBAPP_DIR_LOCATION = "src/main/webapp/";

    @Test
    void testSharedCounter() {
        // 톰캣 서버 시작
        TomcatStarter tomcatStarter = new TomcatStarter(WEBAPP_DIR_LOCATION);
        tomcatStarter.start();

        // shared-counter 페이지를 3번 호출한다.
        String PATH = "/shared-counter";
        HttpUtils.send(PATH);
        HttpUtils.send(PATH);
        HttpResponse<String> response = HttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        /* 산초의 답변: 서블릿 클래스에 불변이 아닌 변수가 있고,
         * 요청을 처리하는 service 함수에서 그 변수를 수정하고 있으므로 이런 일이 생긴다.
         * 서블릿은 쓰레드가 공유를 하기 때문에, 공유되는 자원을 두면 안된다. */
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
        /* 산초의 답변: 지역변수가 쓰레드마다 할당되므로 공유되지 않는다. */
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
