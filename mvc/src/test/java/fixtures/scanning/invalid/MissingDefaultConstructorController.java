package fixtures.scanning.invalid;

import com.interface21.context.stereotype.Controller;

@Controller
public class MissingDefaultConstructorController {

    public MissingDefaultConstructorController(final String dependency) {
    }
}
