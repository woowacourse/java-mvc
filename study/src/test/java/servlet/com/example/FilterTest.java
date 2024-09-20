package servlet.com.example;

import static org.assertj.core.api.Assertions.assertThat;

import static servlet.com.example.KoreanServlet.인코딩;

import org.junit.jupiter.api.Test;

import support.HttpUtils;

class FilterTest {

    /**
     * Filter는 Java 웹 애플리케이션에서 모든 요청과 응답을 가로챈다.
     *
     * @WebFilter로 필터가 적용될 URL을 지정할 수 있다.
     */
    @Test
    void testFilter() {
        // 톰캣 서버 시작
        final var tomcatStarter = new TomcatStarter("src/main/webapp/");
        tomcatStarter.start();

        final var response = HttpUtils.send("/korean");

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // 테스트가 통과하도록 CharacterEncodingFilter 클래스를 수정해보자.
        assertThat(response.body()).isEqualTo(인코딩);
    }
}
