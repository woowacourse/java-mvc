package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import nextstep.mvc.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    private JspView(final String viewName) {
        this.viewName = viewName;
    }

    public static JspView from(final String location) {
        if (location.startsWith(JspView.REDIRECT_PREFIX)) {
            String viewName = location.substring(JspView.REDIRECT_PREFIX.length());
            return new JspView(viewName);
        }
        return new JspView(location);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        if (Objects.isNull(requestDispatcher)) {
            throw new NotFoundException("JSP not found.");
        }
        requestDispatcher.forward(request, response);
    }
}
