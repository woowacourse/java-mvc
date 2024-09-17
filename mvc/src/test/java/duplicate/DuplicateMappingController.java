package duplicate;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class DuplicateMappingController {

    @RequestMapping(method = {RequestMethod.GET})
    public String index() {
        return "index";
    }

    @RequestMapping(method = {RequestMethod.GET})
    public String index2() {
        return "index2";
    }
}
