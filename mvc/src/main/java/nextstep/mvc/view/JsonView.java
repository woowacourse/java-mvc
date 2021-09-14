package nextstep.mvc.view;

import static nextstep.web.support.MediaType.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.ContentType;
import nextstep.web.support.MediaType;
import nextstep.web.support.StatusCode;

public class JsonView implements View {

    private final String json;
    private final StatusCode statusCode;

    public JsonView(String json, StatusCode statusCode) {
        this.json = json;
        this.statusCode = statusCode;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(statusCode.statusNumber());
        response.setHeader(ContentType.headerKey(), APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().append(json);
        response.flushBuffer();
    }
}
