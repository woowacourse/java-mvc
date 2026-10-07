package fixtures;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class InvalidHandlerController {

    @RequestMapping("/invalid")
    public ModelAndView handle(final HttpServletRequest request) {
        return new ModelAndView(new JspView(""));
    }
}
