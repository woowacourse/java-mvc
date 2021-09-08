package samples.di;

import air.annotation.Autowired;
import air.annotation.Controller;

@Controller
public class InjectController {
    public InjectComponent injectComponent;

    @Autowired
    public InjectController(InjectComponent injectComponent) {
        this.injectComponent = injectComponent;
    }
}
