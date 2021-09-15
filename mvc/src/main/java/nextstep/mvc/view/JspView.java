package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";
    public static final String INDEX_PAGE_PATH = "/";
    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    private static final String SUFFIX = ".jsp";

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectPath = viewName.substring(REDIRECT_PREFIX.length());
            response.sendRedirect(getAppropriateViewName(redirectPath));
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(getAppropriateViewName(viewName));
        requestDispatcher.forward(request, response);
    }

    private String getAppropriateViewName(String viewName) {
        if (viewName.endsWith(SUFFIX) || viewName.equals(INDEX_PAGE_PATH)) {
            return viewName;
        }
        return viewName + SUFFIX;
    }

    public String getViewName() {
        return viewName;
    }
}
