package fixture.nonpublic;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NonPublicMappingController {

    @RequestMapping(value = "/non-public", method = RequestMethod.GET)
    private ModelAndView nonPublic(final HttpServletRequest request, final HttpServletResponse response) {
        return null;
    }
}
