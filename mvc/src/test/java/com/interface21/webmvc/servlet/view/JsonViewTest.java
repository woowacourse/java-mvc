package com.interface21.webmvc.servlet.view;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("model을 읽어서 response에 JSON으로 응답한다.")
    @Test
    void render() throws Exception {
        Map<String, String> model = new LinkedHashMap<>();
        model.put("account", "gugu");
        model.put("password", "password");
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        JsonView jsonView = new JsonView();

        jsonView.render(model, request, response);

        then(response).should().setContentType("application/json;charset=UTF-8");
        then(writer).should().write("{\"account\":\"gugu\",\"password\":\"password\"}");
    }
}
