package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
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

    private final String viewPath;

    public JspView(final String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (isRedirectResponse()) {
            response.sendRedirect(viewPath.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        forwardRequest(model, request, response);
    }

    private boolean isRedirectResponse() {
        return viewPath.startsWith(JspView.REDIRECT_PREFIX);
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
}
