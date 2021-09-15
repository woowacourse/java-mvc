package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// TODO JsonView에 맞게 수정하기
public class JsonView extends AbstractView {

    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    public JsonView(String viewName) {
        super(viewName);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JsonView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JsonView.REDIRECT_PREFIX.length()));
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
