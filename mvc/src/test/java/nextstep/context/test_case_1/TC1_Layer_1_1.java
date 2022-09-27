package nextstep.context.test_case_1;

import nextstep.web.annotation.Controller;

@Controller
public class TC1_Layer_1_1 {

    private final TC1_Layer_2_1 layer_2_1;
    private final TC1_Layer_2_2 layer_2_2;

    public TC1_Layer_1_1(final TC1_Layer_2_1 layer_2_1, final TC1_Layer_2_2 layer_2_2) {
        this.layer_2_1 = layer_2_1;
        this.layer_2_2 = layer_2_2;
    }
}
