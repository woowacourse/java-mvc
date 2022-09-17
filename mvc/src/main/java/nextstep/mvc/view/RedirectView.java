package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public RedirectView(String viewName) {
        if (!viewName.startsWith(REDIRECT_PREFIX)) {
            throw new IllegalArgumentException("");
        }
        this.viewName = viewName.substring(REDIRECT_PREFIX.length());
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }
}
