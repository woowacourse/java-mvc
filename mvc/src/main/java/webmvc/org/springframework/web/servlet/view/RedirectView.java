package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Map;

public abstract class RedirectView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";

    protected final String viewName;

    protected RedirectView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }
        renderWithoutRedirect(model, request, response);
    }

    protected abstract void renderWithoutRedirect(final Map<String, ?> model, final HttpServletRequest request,
                                                  final HttpServletResponse response) throws ServletException, IOException;
}
