package fails;

import nextstep.web.annotation.Controller;

// 기본 생성자가 없는 FailController
@Controller
public class FailController {

    private String field;

    public FailController(String field) {
        this.field = field;
    }
}
