package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import nextstep.mvc.mapping.ControllerScanner;
import nextstep.mvc.mapping.HandlerExecution;
import nextstep.mvc.mapping.HandlerKey;
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
        final boolean containsGetJsonTest = handlerMappings.containsKey(
                new HandlerKey("/get-json-test", RequestMethod.GET));

        // then
        assertAll(
                () -> assertThat(actualSize).isEqualTo(3),
                () -> assertThat(containsGetTest).isTrue(),
                () -> assertThat(containsPostTest).isTrue(),
                () -> assertThat(containsGetJsonTest).isTrue()
        );
    }
}
