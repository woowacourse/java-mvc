package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TestChildController extends TestParentController {

    @RequestMapping(value = "/child", method = RequestMethod.GET)
    public ModelAndView child(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("child"));
    }
}
