package webmvc.org.springframework.web.servlet.mvc.tobe;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class ForwardController {

    private final String path;

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @RequestMapping(value = "/", method = GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(path));
    }
}
