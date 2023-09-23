package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.View;

public class RedirectView implements View {

    private static final Logger log = LoggerFactory.getLogger(RedirectView.class);
    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public RedirectView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        log.debug("redirect to {}", viewName);
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }

    @Override
    public String getName() {
        return viewName;
    }
}
