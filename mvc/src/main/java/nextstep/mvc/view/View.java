package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface View {
    void render(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
