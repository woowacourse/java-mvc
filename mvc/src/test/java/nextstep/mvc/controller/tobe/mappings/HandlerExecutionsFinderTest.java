package nextstep.mvc.controller.tobe.mappings;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.mvc.controller.tobe.mappings.HandlerExecution;
import nextstep.mvc.controller.tobe.mappings.HandlerExecutionsFinder;
import nextstep.mvc.controller.tobe.mappings.HandlerKey;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.Test;

class HandlerExecutionsFinderTest {

    @Test
    void findHandlerExecutionsByAnnotation() {
        // given
        HandlerExecutionsFinder finder = new HandlerExecutionsFinder();
        // when
        Map<HandlerKey, HandlerExecution> executions = finder.findHandlerExecutions("samples");
        // then
        assertThat(executions.containsKey(new HandlerKey("/get-test", RequestMethod.GET))).isTrue();
    }
}
