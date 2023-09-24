package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView extends RedirectableView {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public JspView(String viewName) {
        super(viewName);
    }

    @Override
    protected void renderInternal(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        addModelAttributes(model, request);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void addModelAttributes(Map<String, ?> model, HttpServletRequest request) {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
    }
}
