package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();

    @DisplayName("JsonView는 Model을 Json으로 변환한다.")
    @Test
    void render_SingleModel() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Map<String, ?> model = new HashMap<>(
                Map.of("user", new TestUser("gugu", "1234"))
        );

        StringWriter stringWriter = new StringWriter();
        given(response.getWriter()).willReturn(new PrintWriter(stringWriter));

        // when
        jsonView.render(model, request, response);
        String actualJson = stringWriter.toString();

        // then
        String expectedJson = "{\"account\":\"gugu\",\"password\":\"1234\"}";
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).setCharacterEncoding("UTF-8");
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @DisplayName("JsonView는 Model이 1개 이상일 경우 Map 형태의 Json으로 변환한다.")
    @Test
    void render_MoreThanSingleModel() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Map<String, ?> model = new HashMap<>(Map.of(
                "user1", new TestUser("gugu", "1234"),
                "user2", new TestUser("raon", "1234"),
                "user3", new TestUser("power", "1234"))
        );

        StringWriter stringWriter = new StringWriter();
        given(response.getWriter()).willReturn(new PrintWriter(stringWriter));

        // when
        jsonView.render(model, request, response);
        String actualJson = stringWriter.toString();

        // then
        String expectedJson = "{\"user1\":{\"account\":\"gugu\",\"password\":\"1234\"},\"user2\":{\"account\":\"raon\",\"password\":\"1234\"},\"user3\":{\"account\":\"power\",\"password\":\"1234\"}}";
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).setCharacterEncoding("UTF-8");
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @DisplayName("JsonView는 Model이 없을 경우 빈 Json을 반환한다.")
    @Test
    void render_NoModel() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Map<String, ?> model = new HashMap<>(Map.of());

        StringWriter stringWriter = new StringWriter();
        given(response.getWriter()).willReturn(new PrintWriter(stringWriter));

        // when
        jsonView.render(model, request, response);
        String actualJson = stringWriter.toString();

        // then
        String expectedJson = "";
        assertThat(actualJson).isEqualTo(expectedJson);
    }

    private static class TestUser {

        private final String account;
        private final String password;

        public TestUser(String account, String password) {
            this.account = account;
            this.password = password;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }
    }
}
