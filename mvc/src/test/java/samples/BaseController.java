package samples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BaseController {

    @RequestMapping(value = "/base-test", method = RequestMethod.GET)
    public ModelAndView base(final HttpServletRequest request, final HttpServletResponse response) {
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("owner", getClass().getSimpleName());
        return modelAndView;
    }
}
