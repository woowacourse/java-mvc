package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        /// TODO: 2023/09/23 어노테이션 사용하는 방식 적용해보기
        final var json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(json.length());

        print(response, json);
    }

    private void print(HttpServletResponse response, String json) throws IOException {
        final var writer = response.getWriter();
        writer.print(json);
        writer.flush();
    }
    
}
