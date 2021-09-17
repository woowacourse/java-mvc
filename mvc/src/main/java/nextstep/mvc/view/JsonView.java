package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.Header;
import nextstep.web.support.MediaType;

import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();

        response.setHeader(Header.CONTENT_TYPE_HEADER_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() < 2) {
            for (Object object : model.values()) {
                objectMapper.writeValue(outputStream, object);
            }
            return;
        }

        objectMapper.writeValue(outputStream, model);
    }
}
