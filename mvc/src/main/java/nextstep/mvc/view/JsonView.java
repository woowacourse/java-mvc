package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public String getTrimName() {
        return null;
    }
}
