package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

public abstract class AbstractView implements View {

    public static final Logger log = LoggerFactory.getLogger(AbstractView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    protected final String viewName;

    public AbstractView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (isRedirect()) {
            renderRedirect(response);
            return;
        }
        renderInternal(model, request, response);
    }

    private boolean isRedirect() {
        return viewName.startsWith(REDIRECT_PREFIX);
    }

    protected void renderRedirect(HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }

    protected abstract void renderInternal(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception;
}
