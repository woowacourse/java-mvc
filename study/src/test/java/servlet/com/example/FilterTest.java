package servlet.com.example;

import org.junit.jupiter.api.Test;
import support.HttpUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static servlet.com.example.KoreanServlet.인코딩;

class FilterTest {

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
        /**
         * CharacterEncodingFilter에서 response에 UTF-8 설정을 넣어줌.
         * getWriter()를 호출하기전 response 객체에 인코딩이 설정되어야 하기 때문에 Filter에서 인코딩 설정을 해준다.
         */
        assertThat(response.body()).isEqualTo(인코딩);
    }
}
