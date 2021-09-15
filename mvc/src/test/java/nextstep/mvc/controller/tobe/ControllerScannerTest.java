package nextstep.mvc.controller.tobe;

import java.util.Map;

import org.junit.jupiter.api.Test;

import nextstep.web.support.RequestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<HandlerKey, HandlerExecution> controllers = controllerScanner.getControllers();
        assertEquals(controllers.size(), 2);

        HandlerKey handlerKeyGet = new HandlerKey("/get-test", RequestMethod.GET);
        HandlerKey handlerKeyPost = new HandlerKey("/post-test", RequestMethod.POST);

        assertTrue(controllers.containsKey(handlerKeyGet));
        assertTrue(controllers.containsKey(handlerKeyPost));
    }
}
