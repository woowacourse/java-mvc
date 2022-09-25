package nextstep.mvc.view;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JspView implements View {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, ServletException {
        setAttributes(model, request);

        if (isRedirect()) {
            response.sendRedirect(removeRedirectPrefix());
            return;
        }

        request.getRequestDispatcher(viewName)
            .forward(request, response);
    }

    private void setAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    private boolean isRedirect() {
        return viewName.startsWith(JspView.REDIRECT_PREFIX);
    }

    private String removeRedirectPrefix() {
        return viewName.substring(JspView.REDIRECT_PREFIX.length());
    }
}
