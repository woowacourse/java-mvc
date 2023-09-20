package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView extends AbstractView {

    public JspView(String viewName) {
        super(viewName);
    }

    @Override
    protected void renderInternal(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
