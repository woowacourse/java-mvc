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

    public static final String REDIRECT_PREFIX = "redirect:";
    public static final String INTERNAL_SERVER_ERROR = "/500.jsp";
    public static final String NOT_FOUND = "/404.jsp";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    public static void renderInternalServerError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(INTERNAL_SERVER_ERROR);
        requestDispatcher.forward(request, response);
    }

    public static void renderNotFound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(NOT_FOUND);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
