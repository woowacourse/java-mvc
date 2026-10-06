package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    @DisplayName("JSON 응답의 Content-Type은 application/json;charset=UTF-8이다")
    void setsJsonContentType() throws Exception {
        new JsonView().render(Map.of("id", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @DisplayName("모델 데이터가 1개면 값을 그대로 JSON으로 변환한다")
    void singleModelIsWrittenAsValue() throws Exception {
        new JsonView().render(Map.of("user", new Sample("gugu", 20)), request, response);

        assertThat(body.toString()).isEqualTo("{\"name\":\"gugu\",\"age\":20}");
    }

    @Test
    @DisplayName("모델 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환한다")
    void multipleModelsAreWrittenAsMap() throws Exception {
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("id", "gugu");
        model.put("age", 20);

        new JsonView().render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"id\":\"gugu\",\"age\":20}");
    }

    @Test
    @DisplayName("모델 데이터가 없으면 빈 JSON 객체를 반환한다")
    void emptyModelIsWrittenAsEmptyObject() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    public record Sample(String name, int age) {
    }
}
