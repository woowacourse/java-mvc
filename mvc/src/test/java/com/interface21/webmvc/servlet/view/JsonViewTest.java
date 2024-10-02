package com.interface21.webmvc.servlet.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void render_model_size_one() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        String userName = "name";
        String userPassword = "password";
        Map<String, ?> model = Map.of("user", new User(userName, userPassword));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response.getWriter()).write("{\"name\":\"" + userName + "\",\"password\":\"" + userPassword + "\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void render_model_size_over_one() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        String userName1 = "name1";
        String userPassword1 = "password1";
        String userName2 = "name2";
        String userPassword2 = "password2";
        LinkedHashMap<String, User> model = new LinkedHashMap<>();
        model.put("user1", new User(userName1, userPassword1));
        model.put("user2", new User(userName2, userPassword2));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(response.getWriter()).write("{\"user1\":{\"name\":\"" + userName1 + "\",\"password\":\"password1\"},"
                                           + "\"user2\":{\"name\":\"" + userName2 + "\",\"password\":\"" + userPassword2
                                           + "\"}}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private class User {

        private final String name;
        private final String password;

        public User(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }
}
