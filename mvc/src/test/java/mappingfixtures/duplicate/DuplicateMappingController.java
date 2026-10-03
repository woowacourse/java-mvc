package mappingfixtures.duplicate;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class DuplicateMappingController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public void first() {
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public void second() {
    }
}
