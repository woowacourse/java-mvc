package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final String viewName;

    public JsonView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    @Override
    public String getViewName() {
        return viewName;
    }
}
