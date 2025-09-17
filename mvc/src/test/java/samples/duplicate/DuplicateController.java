package samples.duplicate;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public final class DuplicateController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public String duplicate1() {
        return "duplicate";
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public String duplicateGet2() {
        return "duplicate";
    }
}
