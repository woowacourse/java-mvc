package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.exception.view.NoSuchRequestDispatcherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger LOG = LoggerFactory.getLogger(JspView.class);
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String JSP_SUFFIX = ".jsp";

    private final String name;

    public JspView(String name) {
        this.name = name;
    }

    @Override
    public void render(
        Map<String, ?> model,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        setAttributes(model, request);

        String path = getPath();

        if (path.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(parsePath(path));
            return;
        }

        RequestDispatcher requestDispatcher = getRequestDispatcher(request, path);
        requestDispatcher.forward(request, response);
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

    private String getPath() {
        if (name.endsWith(JSP_SUFFIX)) {
            return name;
        }
        return name + JSP_SUFFIX;
    }

    private String parsePath(String path) {
        return path.substring(REDIRECT_PREFIX.length());
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
