package nextstep.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.context.test_case_1.TC1_Layer_1_1;
import nextstep.context.test_case_1.TC1_Layer_1_2;
import nextstep.context.test_case_1.TC1_Layer_1_3;
import nextstep.context.test_case_1.TC1_Layer_2_1;
import nextstep.context.test_case_1.TC1_Layer_2_2;
import nextstep.context.test_case_1.TC1_Layer_2_3;
import nextstep.context.test_case_1.TC1_Layer_3_1;
import nextstep.context.test_case_2.InMemoryUserRepository;
import nextstep.context.test_case_2.UserController;
import nextstep.context.test_case_2.UserService;
import nextstep.context.test_case_3.TC3_Layer_1;
import nextstep.context.test_case_3.TC3_Layer_2_1;
import nextstep.context.test_case_3.TC3_Layer_2_2;
import nextstep.context.test_case_3.TC3_Layer_3_1;
import nextstep.context.test_case_3.TC3_Layer_3_2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class PeanutBoxTest {

    @AfterEach
    void tearDown() {
        PeanutBox.INSTANCE.clear();
    }

    @Test
    void getPeanut() {
        PeanutBox.INSTANCE.init("nextstep.context.test_case_1");
        assertAll(
                () -> assertPeanut(TC1_Layer_1_1.class),
                () -> assertPeanut(TC1_Layer_1_2.class),
                () -> assertPeanut(TC1_Layer_1_3.class),
                () -> assertPeanut(TC1_Layer_2_1.class),
                () -> assertPeanut(TC1_Layer_2_2.class),
                () -> assertPeanut(TC1_Layer_2_3.class),
                () -> assertPeanut(TC1_Layer_3_1.class)
        );
    }

    @Test
    void getPeanut_2() {
        PeanutBox.INSTANCE.init("nextstep.context.test_case_2");
        assertAll(
                () -> assertPeanut(UserService.class),
                () -> assertPeanut(InMemoryUserRepository.class),
                () -> assertPeanut(UserController.class)
        );
    }

    @Test
    void getPeanut_3() {
        PeanutBox.INSTANCE.init("nextstep.context.test_case_3");
        assertAll(
                () -> assertPeanut(TC3_Layer_1.class),
                () -> assertPeanut(TC3_Layer_2_1.class),
                () -> assertPeanut(TC3_Layer_2_2.class),
                () -> assertPeanut(TC3_Layer_3_1.class),
                () -> assertPeanut(TC3_Layer_3_2.class)
        );
    }

    private void assertPeanut(final Class<?> clazz) {
        assertThat(PeanutBox.INSTANCE.getPeanut(clazz)).isInstanceOf(clazz);
    }
}
