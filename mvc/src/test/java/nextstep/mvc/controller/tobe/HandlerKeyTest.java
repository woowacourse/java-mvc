package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.handlermapping.HandlerKey;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerKeyTest {

    @DisplayName("request에 맞는 HandlerKey로 변환한다.")
    @Test
    void fromRequest() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerKey actual = HandlerKey.from(request);
        HandlerKey expected = new HandlerKey("/get-test", RequestMethod.GET);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
