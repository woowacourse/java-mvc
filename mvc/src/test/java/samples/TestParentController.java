package samples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestParentController {

    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public ModelAndView parent(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView(""));
    }

}
