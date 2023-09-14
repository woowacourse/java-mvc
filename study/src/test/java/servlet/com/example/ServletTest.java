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

        /*
        Servlet의 생명주기는 init -> service 반복 -> destory 이다.
        따라서 가변 인스턴스 변수를 필드로 가진다면 service 메소드가 반복적으로 호출하는 과정에서 내부 상태가 변할 수 있다.
        위 테스트 코드에서는 GET 요청을 3번 보냈으므로, Counter의 값은 3으로 변하게 된다.
         */
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

        /*
        이 경우에는 Servlet이 인스턴스 변수를 가지지 않는다.
        무상태 Servlet이므로 service를 아무리 호출하더라도 상태가 변하지 않는다.
        따라서 언제 호출하더라도 1을 반환한다.
         */
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
