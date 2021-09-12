package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import samples.ManualTestController;

class ControllerHandlerAdapterTest {

    private static final HandlerAdapter ADAPTER = new ControllerHandlerAdapter();

    @Test
    void supports() {
        Object controller = new ManualTestController();

        assertTrue(ADAPTER.supports(controller));
    }

    @Test
    void supportsFalse() {
        assertFalse(ADAPTER.supports(new Object()));
    }
}
