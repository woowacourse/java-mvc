package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class RedirectView implements View {

    private final String viewName;

    public RedirectView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }

    @Override
    public String getViewName() {
        return viewName;
    }
}
