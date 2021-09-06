package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface View {
    void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
