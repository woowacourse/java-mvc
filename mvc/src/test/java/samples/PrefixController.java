package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/prefix")
public class PrefixController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView findUserId(final HttpServletRequest request, final HttpServletResponse response) {
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("controller", "prefix");
        return modelAndView;
    }
}
