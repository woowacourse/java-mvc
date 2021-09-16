package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class TestController5 {

    @RequestMapping(value = "/integration-model-and-view", method = RequestMethod.GET)
    public ModelAndView modelAndView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    @RequestMapping(value = "/integration-string", method = RequestMethod.GET)
    public String string(HttpServletRequest request, HttpServletResponse response) {
        return "/index.jsp";
    }

    @RequestMapping(value = "/integration-string-redirect", method = RequestMethod.GET)
    public String stringRedirect(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/index.jsp";
    }
}
