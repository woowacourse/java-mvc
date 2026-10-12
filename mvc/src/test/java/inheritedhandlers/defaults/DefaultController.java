package inheritedhandlers.defaults;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DefaultController implements DefaultRoute {
}

interface DefaultRoute {
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    default ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("")).addObject("handler", "interface-default");
    }
}
