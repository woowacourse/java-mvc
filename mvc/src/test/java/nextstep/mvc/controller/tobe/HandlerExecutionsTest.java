package nextstep.mvc.controller.tobe;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Set;
import org.junit.jupiter.api.Test;

class HandlerExecutionsTest {

    @Test
    void HandlerExecutions_에_추가한_핸들러를_반환한다() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerExecutions handlerExecutions = new HandlerExecutions();

        handlerExecutions.add(new HandlerKey("/api/test", GET), handlerExecution);

        Object controller = handlerExecutions.getHandlerExecution("/api/test", "GET");
        assertThat(controller).isEqualTo(handlerExecution);
    }

    @Test
    void HandlerExecutions_에_추가한_HandlerKey_를_반환한다() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerExecutions handlerExecutions = new HandlerExecutions();
        HandlerKey getKey = new HandlerKey("/api/post", GET);
        HandlerKey postKey = new HandlerKey("/api/test", POST);

        handlerExecutions.add(getKey, handlerExecution);
        handlerExecutions.add(postKey, handlerExecution);

        Set<HandlerKey> handlers = handlerExecutions.getHandlers();
        assertThat(handlers).contains(getKey, postKey);
    }
}
