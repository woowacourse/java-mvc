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

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (isRedirect()) {
            redirect(response);
            return;
        }
        forward(model, request, response);
    }

    private boolean isRedirect() {
        return viewName.startsWith(JspView.REDIRECT_PREFIX);
    }

    private void redirect(final HttpServletResponse response) throws IOException {
        response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
        log.info("redirect view {}", viewName);
    }

    private void forward(final Map<String, ?> model,
                         final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        setAttributes(model, request);
        requestDispatcher.forward(request, response);
        log.info("render view {}", viewName);
    }

    private void setAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet()
                .forEach(key -> {
                    log.debug("attribute name : {}, value : {}", key, model.get(key));
                    request.setAttribute(key, model.get(key));
                });
    }
}
