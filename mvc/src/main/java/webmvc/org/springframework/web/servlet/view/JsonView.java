package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import web.org.springframework.util.HttpRequestBodyConverter;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String deserialize = getDeserializedJson(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().println(deserialize);
    }

    private String getDeserializedJson(final Map<String, ?> model) throws Exception {
        if (model.size() == 1) {
            final Object object = model.values().iterator().next();
            return HttpRequestBodyConverter.deserialize(object);
        }
        return HttpRequestBodyConverter.deserialize(model);
    }
}
