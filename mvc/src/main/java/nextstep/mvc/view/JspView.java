package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;
    private final boolean redirect;

    public JspView(final String viewName) {
        validateJsp(viewName);
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            this.viewName = viewName.substring(REDIRECT_PREFIX.length());
            this.redirect = true;
            return;
        }
        this.viewName = viewName;
        this.redirect = false;
    }

    private void validateJsp(final String viewName) {
        if (!viewName.endsWith(".jsp")) {
            throw new IllegalArgumentException(String.format("JSP 형식이 아닙니다. [%s]", viewName));
        }
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        addLocation(response);
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.include(request, response);
    }

    private void addLocation(final HttpServletResponse response) throws IOException {
        if (redirect) {
            response.sendRedirect(viewName);
        }
    }
}
