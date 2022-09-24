package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = extractViewName(viewName);
    }

    private String extractViewName(final String viewName) {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            return viewName.substring(JspView.REDIRECT_PREFIX.length());
        }
        return viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        if (requestDispatcher == null) {
            throw new NotFoundException("jsp뷰를 찾을 수 없습니다.");
        }
        requestDispatcher.forward(request, response);
    }
}
