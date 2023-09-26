package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            redirect(response);
            return;
        }
        forward(model, request, response);
    }

    private void redirect(HttpServletResponse response) {
        try {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
        } catch (IOException e) {
            throw new IllegalArgumentException("redirect 중 예외가 발생했습니다.", e);
        }
    }

    private void forward(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        try {
            model.keySet().forEach(key -> {
                log.debug("attribute name : {}, value : {}", key, model.get(key));
                request.setAttribute(key, model.get(key));
            });
            final var requestDispatcher = request.getRequestDispatcher(viewName);

            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new IllegalArgumentException("forward 중 예외가 발생했습니다.", e);
        }
    }

}
