package nextstep.context.test_case_1;

import nextstep.web.annotation.Controller;

@Controller
public class TC1_Layer_1_2 {

    private TC1_Layer_2_3 layer_2_3;

    public TC1_Layer_1_2(final TC1_Layer_2_3 layer_2_3) {
        this.layer_2_3 = layer_2_3;
    }
}
