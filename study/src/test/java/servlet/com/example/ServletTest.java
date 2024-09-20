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
        /**
         * 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
         *
         * 서블릿의 인스턴스 변수는 다른 스레드와 공유된다.
         * 서버는 여러 스레드에서 접근 가능하므로 서블릿에서 비즈니스 로직을 처리할 때 인스턴스 변수는 사용하지 않는다.
         * 해당 서블릿이 3개의 스레드에서 호출되었으므로 body의 결과는 3이 된다.
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
        /**
         * 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
         *
         * 서블릿의 인스턴스 변수는 다른 스레드와 공유되지만, 메서드 내 로컬 변수는 다른 스레드와 공유되지 않는다.
         * 따라서, 비즈니스 로직 처리는 로컬 변수를 사용한다.
         */
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
