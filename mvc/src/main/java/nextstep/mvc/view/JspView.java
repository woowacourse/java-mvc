package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String JSP_SUFFIX = ".jsp";
    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }
        addModelToRequest(model, request);
        forwardToJsp(request, response);
    }

    private void addModelToRequest(Map<String, ?> model, HttpServletRequest request) {
        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, value);
            request.setAttribute(key, value);
        });
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullViewName = fullNameOf(viewName);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(fullViewName);
        requestDispatcher.forward(request, response);
    }

    private String fullNameOf(String viewName) {
        if (viewName.endsWith(JSP_SUFFIX)) {
            return viewName;
        }
        return viewName + JSP_SUFFIX;
    }
}
