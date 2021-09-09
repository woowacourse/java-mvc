package nextstep.mvc.controller.asis;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ForwardController implements Controller {

    private final String path;

    public ForwardController(String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
