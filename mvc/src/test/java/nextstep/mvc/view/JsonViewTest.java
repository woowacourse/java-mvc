package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    @DisplayName("Json으로 정상적으로 출력되는지 테스트한다.")
    @Test
    void render() throws Exception {
        JsonView jsonView = new JsonView();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        Mockito.when(response.getWriter()).thenReturn(writer);

        jsonView.render(Map.<String, Object>of("account", "gugu", "name", "bab"), request, response);
        String responseBody = stringWriter.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        User user = (User) objectMapper.readerFor(User.class).readValue(responseBody);

        assertThat(user.getAccount()).isEqualTo("gugu");
    }

    static class User {
        private String account;
        private String name;

        User() {
        }

        public String getName() {
            return name;
        }

        public String getAccount() {
            return account;
        }
    }
}