package duplicates;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class SecondDuplicateController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public void handle() {
    }
}
