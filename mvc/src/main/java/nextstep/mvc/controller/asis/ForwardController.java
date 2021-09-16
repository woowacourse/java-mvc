package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ForwardController implements Controller {

    private final String path;

    public ForwardController(String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(path));
    }
}
