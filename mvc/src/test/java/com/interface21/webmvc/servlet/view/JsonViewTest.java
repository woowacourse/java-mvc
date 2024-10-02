package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("모델에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void render_SingleEntry() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Person person1 = new Person("tre", 20);
        Map<String, Person> model = Map.of("person", person1);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter).hasToString("{\"name\":\"tre\",\"age\":20}");
    }

    @DisplayName("모델에 데이터가 2개면 값을 Map 형태로 반환한다.")
    @Test
    void render_TwoEntries() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Person person = new Person("tre", 20);
        Pet pet = new Pet("candy", "cute candy");

        Map<String, ?> model = Map.of(
                "person", person,
                "pet", pet
        );

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isIn(
                "{\"person\":{\"name\":\"tre\",\"age\":20},\"pet\":{\"name\":\"candy\",\"description\":\"cute candy\"}}",
                "{\"pet\":{\"name\":\"candy\",\"description\":\"cute candy\"},\"person\":{\"name\":\"tre\",\"age\":20}}"
        );
    }

    private record Person(String name, int age) {

    }

    private record Pet(String name, String description) {

    }
}
