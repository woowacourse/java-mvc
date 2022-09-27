package nextstep.context.test_case_3;

import nextstep.web.annotation.GiveMePeanut;
import nextstep.web.annotation.ImPeanut;

@ImPeanut
public class TC3_Layer_2_1 {

    @GiveMePeanut
    private TC3_ILayer_3_1 layer_3_1;

    @GiveMePeanut
    private TC3_Layer_3_2 layer_3_2;
}
