package all;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/all")
    public String all() {
        return "all";
    }
}
