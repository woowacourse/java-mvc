package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.NotSupportedMethod;
import nextstep.mvc.exception.ResponseParseJsonException;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE.getContentType());
        PrintWriter writer = response.getWriter();
        String jsonData = (String) parseJsonData(model);
        log.debug("Json Data: {}", jsonData);
        writer.write(jsonData);
    }

    private Object parseJsonData(Map<String, ?> model) {
        try {
            if (model.size() == 1) {
                return objectMapper.writeValueAsString(model.values().toArray()[0]);
            }
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.error("응답을 Json 형식으로 변환하는 도중 예외가 발생했습니다.");
            throw new ResponseParseJsonException("응답을 Json 형식으로 변환하는 도중 예외가 발생했습니다.", e);
        }
    }

    @Override
    public String getViewName() {
        throw new NotSupportedMethod("지원하지 않는 메서드입니다.");
    }
}
