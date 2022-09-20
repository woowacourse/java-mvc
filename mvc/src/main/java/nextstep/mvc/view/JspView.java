package nextstep.mvc.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (isNeedToRedirect()) {
            redirect(response);
            return;
        }
        forward(model, request, response);
    }

    private boolean isNeedToRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    private void redirect(final HttpServletResponse response) throws IOException {
        response.sendRedirect(url());
    }

    private String url() {
        if (isNeedToRedirect()) {
            return viewName.substring(REDIRECT_PREFIX.length());
        }
        return viewName;
    }

    private void forward(final Map<String, ?> model, final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        putModelToRequestAttribute(model, request);
        forward(request, response);
    }

    private void putModelToRequestAttribute(final Map<String, ?> model, final HttpServletRequest request) {
        for (String key : model.keySet()) {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        }
    }

    private void forward(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final var requestDispatcher = request.getRequestDispatcher(url());
        requestDispatcher.forward(request, response);
    }
}
