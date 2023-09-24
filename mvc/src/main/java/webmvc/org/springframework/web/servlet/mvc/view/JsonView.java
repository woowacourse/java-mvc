package webmvc.org.springframework.web.servlet.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final var entries = model.entrySet();
        for (final var entry : entries) {
            response.getWriter().write(entry.getKey() + " : " + entry.getValue() + "\n");
        }
    }

    @Override
    public String getViewName() {
        return null;
    }
}
