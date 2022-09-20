package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class DefaultView implements View {

    private static final String REDIRECT_PREFIX = "redirect:";
    public static final String DEFAULT_VIEW_PREFIX = "/";
    public static final String DEFAULT_VIEW_REDIRECT = REDIRECT_PREFIX + "/";

    private final String viewName;

    public DefaultView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }
        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
