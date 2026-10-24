package fixtures.mapping.inherited;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseController {

    @RequestMapping(value = "/inherited", method = RequestMethod.GET)
    public ModelAndView inherited(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/inherited.jsp")).addObject("handler", "inherited");
    }
}
