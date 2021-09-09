package samples.di;

import com.techcourse.air.core.annotation.Autowired;
import com.techcourse.air.core.annotation.Controller;

@Controller
public class InjectController {
    public InjectComponent injectComponent;

    @Autowired
    public InjectController(InjectComponent injectComponent) {
        this.injectComponent = injectComponent;
    }
}
