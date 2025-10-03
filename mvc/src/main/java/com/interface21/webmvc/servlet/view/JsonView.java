package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // JSON 포맷팅
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false); // 빈 배열 제외
        return mapper;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        log.debug("Rendering JSON view with model: {}", model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            String jsonContent = objectMapper.writeValueAsString(model);
            log.debug("Generated JSON content: {}", jsonContent);

            try (PrintWriter writer = response.getWriter()) {
                writer.write(jsonContent);
                writer.flush();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 실패: {}", e.getMessage(), e);
            handleJsonProcessingError(response, e);
        } catch (IOException e) {
            log.error("응답 작성 실패: {}", e.getMessage(), e);
            handleIOError(response, e);
        }
    }

    private void handleJsonProcessingError(HttpServletResponse response, JsonProcessingException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"error\":\"JSON 직렬화 실패\",\"message\":\"" + e.getMessage() + "\"}");
    }

    private void handleIOError(HttpServletResponse response, IOException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error("응답을 클라이언트에게 전송할 수 없습니다: {}", e.getMessage());
    }
}
