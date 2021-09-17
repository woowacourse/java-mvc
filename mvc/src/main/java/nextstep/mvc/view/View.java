package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
