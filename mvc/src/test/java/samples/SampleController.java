package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
public class SampleController {

    @RequestMapping(value = "sample", method = RequestMethod.GET)
    public ModelAndView sample(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }
}
