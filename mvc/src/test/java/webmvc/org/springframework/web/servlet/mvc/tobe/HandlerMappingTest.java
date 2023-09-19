package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;
import static web.org.springframework.web.bind.annotation.RequestMethod.GET;

class HandlerMappingTest {

    @Test
    void HttpServletRequest_객체를_이용해서_HandlerKey를_반환한다() {
        // given
        final HandlerMapping handlerMapping = mock(HandlerMapping.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        final HandlerKey expected = new HandlerKey("/test", GET);
        when(handlerMapping.getHandlerKey(request)).thenReturn(expected);

        // then
        final HandlerKey actual = handlerMapping.getHandlerKey(request);

        assertThat(actual).isEqualTo(expected);
    }
}
