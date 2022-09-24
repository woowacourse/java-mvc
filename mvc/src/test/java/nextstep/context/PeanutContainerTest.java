package nextstep.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import samples.TestController;

class PeanutContainerTest {

    public PeanutContainerTest() {
        PeanutContainer.INSTANCE.init("samples");
    }

    @Test
    void getPeanut() {
        final TestController peanut = PeanutContainer.INSTANCE.getPeanut(TestController.class);
        assertAll(
                () -> assertThat(peanut).isNotNull(),
                () -> assertThat(peanut).isInstanceOf(TestController.class)
        );
    }
}