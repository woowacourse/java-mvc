package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewPath;

    public JspView(final String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (isRedirectResponse()) {
            response.sendRedirect(viewPath.substring(REDIRECT_PREFIX.length()));
            return;
        }

        forwardRequest(model, request, response);
    }

    private boolean isRedirectResponse() {
        return viewPath.startsWith(REDIRECT_PREFIX);
    }

    private void forwardRequest(final Map<String, ?> model, final HttpServletRequest request,
                                final HttpServletResponse response) throws ServletException, IOException {
        addModelToRequestAttribute(model, request);
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void addModelToRequestAttribute(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JspView jspView = (JspView) o;
        return Objects.equals(viewPath, jspView.viewPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewPath);
    }
}
