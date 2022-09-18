package nextstep.mvc.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        HttpServletResponse response) {
    }
}
