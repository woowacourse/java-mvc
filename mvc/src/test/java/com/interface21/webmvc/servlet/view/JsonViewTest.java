package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

class JsonViewTest {

    @Test
    @DisplayName("Json 형태로 데이터를 잘 랜더링 한다.")
    void render() throws Exception {
        JsonView jsonView = JsonView.getInstance();
        HashMap<String, Object> model = new HashMap<>();
        model.put("key", "value");
        model.put("key2", 1);

        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = Mockito.spy(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("{\"key2\":1,\"key\":\"value\"}");
    }
}
