package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {

    String getViewName();

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
