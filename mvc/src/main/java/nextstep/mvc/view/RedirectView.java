package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
    }
}