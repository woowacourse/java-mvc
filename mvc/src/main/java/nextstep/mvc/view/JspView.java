package nextstep.mvc.view;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        storeAttributes(model, request);
        moveTo(viewName, request, response);
    }

    private void storeAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        final var entries = model.entrySet();
        entries.forEach(entry -> storeAttribute(entry, request));
    }

    private void storeAttribute(final Entry<String, ?> entry, final HttpServletRequest request) {
        final var key = entry.getKey();
        final var value = entry.getValue();

        log.debug("attribute name : {}, value : {}", key, value);
        request.setAttribute(key, value);
    }

    private void moveTo(final String viewName, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        if (isRedirect(viewName)) {
            redirect(viewName, response);
            return;
        }
        forward(viewName, request, response);
    }

    private boolean isRedirect(final String viewName) {
        return viewName.startsWith(JspView.REDIRECT_PREFIX);
    }

    private void redirect(final String viewName, final HttpServletResponse response) throws Exception {
        final var location = viewName.substring(JspView.REDIRECT_PREFIX.length());
        response.sendRedirect(location);
    }

    private void forward(final String viewName, final HttpServletRequest request,
                         final HttpServletResponse response) throws Exception {
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

}
