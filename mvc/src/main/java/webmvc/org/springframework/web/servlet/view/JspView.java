package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JspView extends RedirectView {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public JspView(final String viewName) {
        super(viewName);
    }

    @Override
    protected void renderWithoutRedirect(final Map<String, ?> model, final HttpServletRequest request,
                                         final HttpServletResponse response) throws ServletException, IOException {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
