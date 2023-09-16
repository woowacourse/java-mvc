package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    }

    @Override
    public String getViewName() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public boolean isRedirectCommand() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public String getRedirectFilePath() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }
}
