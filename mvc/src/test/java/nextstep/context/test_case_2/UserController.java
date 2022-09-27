package nextstep.context.test_case_2;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.GiveMePeanut;

@Controller
public class UserController {

    @GiveMePeanut
    private UserService userService;
}
