package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JsonViewTest {

    @DisplayName("Json으로 파싱한다.")
    @Test
    void renderTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        Map<String, Object> model = new HashMap<>();
        model.put("user1", "Mangcho");
        model.put("user2", new User("Groom", 2L));

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        ArgumentCaptor<String> writerCaptor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(writerCaptor.capture());

        String expected = "{\"user1\":\"Mangcho\",\"user2\":{\"name\":\"Groom\",\"age\":2}}";
        assertThat(writerCaptor.getValue()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    static class User {

        private final String name;
        private final Long age;

        User(String name, Long age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Long getAge() {
            return age;
        }
    }
}
