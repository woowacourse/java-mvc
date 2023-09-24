package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        // 추후 구현
    }
}
