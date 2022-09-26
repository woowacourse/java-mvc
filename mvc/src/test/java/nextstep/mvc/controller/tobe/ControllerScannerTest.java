package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("ControllerScanner는 지정한 패키지 이하 RequestMapping 정보를 생성한다")
    @Test
    void controllerScanner_scan() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        final Map<HandlerKey, HandlerExecution> handlerMappings = controllerScanner.scan();
        final int actualSize = handlerMappings.size();
        final boolean containsGetTest = handlerMappings.containsKey(new HandlerKey("/get-test", RequestMethod.GET));
        final boolean containsPostTest = handlerMappings.containsKey(new HandlerKey("/post-test", RequestMethod.POST));

        // then
        assertAll(
                () -> assertThat(actualSize).isEqualTo(2),
                () -> assertThat(containsGetTest).isTrue(),
                () -> assertThat(containsPostTest).isTrue()
        );
    }
}
