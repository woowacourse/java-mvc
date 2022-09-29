package nextstep.context.test_case_1;

import nextstep.web.annotation.Service;

@Service
public class TC1_Layer_2_1 {

    private TC1_Layer_3_1 layer_3_1;

    public TC1_Layer_2_1(final TC1_Layer_3_1 layer_3_1) {
        this.layer_3_1 = layer_3_1;
    }
}
