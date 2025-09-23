package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("빈 모델을 JSON으로 렌더링한다")
    void renderEmptyModel() throws Exception {
        // given
        final var emptyModel = Map.<String, Object>of();

        // when
        jsonView.render(emptyModel, request, response);

        // then
        printWriter.flush();
        final var json = stringWriter.toString();
        Mockito.verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(json).isEqualTo("{}");
    }

    @Test
    @DisplayName("단순 타입 데이터를 JSON으로 렌더링한다")
    void renderSimpleTypes() throws Exception {
        // given
        final var model = Map.<String, Object>of(
                "name", "test",
                "age", 25
        );

        // when
        jsonView.render(model, request, response);

        // then
        printWriter.flush();
        final var json = stringWriter.toString();
        Mockito.verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(json).contains("\"name\":\"test\"");
        assertThat(json).contains("\"age\":25");

    }

    @Test
    @DisplayName("객체를 JSON으로 렌더링한다")
    void renderComplexObject() throws Exception {
        // given
        final var user = new User(1L, "testAccount", "testPassword");
        final var model = Map.<String, Object>of("user", user);

        // when
        jsonView.render(model, request, response);

        // then
        printWriter.flush();
        final var json = stringWriter.toString();
        Mockito.verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(json).contains("\"user\"");
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"account\":\"testAccount\"");
        assertThat(json).contains("\"password\":\"testPassword\"");
    }

    record User(Long id, String account, String password) {
    }
}
