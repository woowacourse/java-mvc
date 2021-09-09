package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.exception.view.IllegalRenderException;
import nextstep.mvc.exception.view.NoSuchPathException;
import nextstep.mvc.exception.view.NoSuchRequestDispatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger LOG = LoggerFactory.getLogger(JspView.class);
    private static final String REDIRECT_PREFIX = "redirect:";

    private final String name;

    public JspView(String name) {
        this.name = name;
    }

    @Override
    public void render(
        Map<String, ?> model,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        setAttributes(model, request);

        String path = preparePath();

        RequestDispatcher requestDispatcher = getRequestDispatcher(request, path);

        try {
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            throw new IllegalRenderException();
        }
    }

    private void setAttributes(Map<String, ?> model, HttpServletRequest request) {
        model.forEach((key, value) -> {
            if (value != null) {
                LOG.debug("attribute name: {}, attribute value: {}", key, model.get(key));
                request.setAttribute(key, value);
                return;
            }
            request.removeAttribute(key);
        });
    }

    private String preparePath() {
        if (name.startsWith(REDIRECT_PREFIX)) {
            int index = name.indexOf(":");
            return name.substring(index + 1);
        }
        if (name.startsWith("/")) {
            return name;
        }
        throw new NoSuchPathException();
    }

    private RequestDispatcher getRequestDispatcher(HttpServletRequest request, String path) {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        if (requestDispatcher != null) {
            return requestDispatcher;
        }
        throw new NoSuchRequestDispatcherException();
    }

    public String getName() {
        return name;
    }
}
