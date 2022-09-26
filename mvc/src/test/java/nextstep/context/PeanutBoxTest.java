package nextstep.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.context.peanuts.Layer_1_1;
import nextstep.context.peanuts.Layer_1_2;
import nextstep.context.peanuts.Layer_1_3;
import nextstep.context.peanuts.Layer_2_1;
import nextstep.context.peanuts.Layer_2_2;
import nextstep.context.peanuts.Layer_2_3;
import nextstep.context.peanuts.Layer_3_1;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

class PeanutBoxTest {

    public PeanutBoxTest() {
        PeanutBox.INSTANCE.init("nextstep.context.peanuts");
    }

    @Test
    void getPeanut() {
        assertAll(
                () -> assertPeanut(Layer_1_1.class),
                () -> assertPeanut(Layer_1_2.class),
                () -> assertPeanut(Layer_1_3.class),
                () -> assertPeanut(Layer_2_1.class),
                () -> assertPeanut(Layer_2_2.class),
                () -> assertPeanut(Layer_2_3.class),
                () -> assertPeanut(Layer_3_1.class)
        );
    }

    private void assertPeanut(final Class<?> clazz) {
        assertThat(PeanutBox.INSTANCE.getPeanut(clazz)).isInstanceOf(clazz);
    }
}