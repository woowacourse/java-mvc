package nextstep.context.test_case_3;

import nextstep.web.annotation.GiveMePeanut;
import nextstep.web.annotation.ImPeanut;

@ImPeanut
public class TC3_Layer_3_1 implements TC3_ILayer_3_1 {

    @GiveMePeanut
    private TC3_Layer_4 layer_4;

    protected TC3_Layer_3_1() {
    }
}
