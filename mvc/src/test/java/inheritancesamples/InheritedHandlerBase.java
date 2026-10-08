package inheritancesamples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InheritedHandlerBase {

    @RequestMapping(value = "/inherited-test", method = RequestMethod.GET)
    public ModelAndView inherited(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""))
                .addObject("source", "parent");
    }
}
