package samples;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InheritedMappingBase {

    @RequestMapping(value = "/inherited-test", method = RequestMethod.GET)
    public ModelAndView inherited(final HttpServletRequest request, final HttpServletResponse response) {
        final var modelAndView = new ModelAndView("");
        modelAndView.addObject("owner", getClass().getSimpleName());
        return modelAndView;
    }
}
