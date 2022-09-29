package nextstep.context.test_case_3;

import nextstep.web.annotation.ImPeanut;

@ImPeanut
public class TC3_Layer_1 {

    private final TC3_Layer_2_1 layer_2_1;
    private final TC3_ILayer_2_2 layer_2_2;

    public TC3_Layer_1(final TC3_Layer_2_1 layer_2_1, final TC3_ILayer_2_2 layer_2_2) {
        this.layer_2_1 = layer_2_1;
        this.layer_2_2 = layer_2_2;
    }
}
