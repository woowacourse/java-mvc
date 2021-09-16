package nextstep.mvc.handler;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HandlerMappingsTest {

    @DisplayName("initialize 기능 테스트")
    @Test
    void initializeAllTest() {
        //given
        HandlerMapping mock = Mockito.mock(HandlerMapping.class);
        HandlerMappings handlerMappings = new HandlerMappings();
        //when
        handlerMappings.add(mock);
        handlerMappings.initializeAll();
        //then
        verify(mock, atLeastOnce()).initialize();
    }
}