package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

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
        apply(model, request);

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void apply(final Map<String, ?> model, final HttpServletRequest request) {
        model.entrySet().forEach(entry -> {
            final var key = entry.getKey();
            final var value = entry.getValue();
            log.debug("attribute name : {}, value : {}", key, value);

            request.setAttribute(key, value);
        });
    }

    public String getViewName() {
        if (!isRedirectView()) {
            return viewName;
        }
        return viewName.split(REDIRECT_PREFIX)[1];
    }

    public boolean isRedirectView() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

}
