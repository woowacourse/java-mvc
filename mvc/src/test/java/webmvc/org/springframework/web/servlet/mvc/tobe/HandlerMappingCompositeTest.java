package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class HandlerMappingCompositeTest {

    @Test
    void 핸들러_매핑_목록에서_요청에_맞는_핸들러_매핑을_반환한다() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final HandlerMapping expected = mock(HandlerMapping.class);
        final HandlerMappingComposite handlerMappingComposite = new HandlerMappingComposite(List.of(
                expected
        ));

        // when
        when(expected.support(any(HttpServletRequest.class))).thenReturn(true);
        final Optional<HandlerMapping> actual = handlerMappingComposite.getHandlerMapping(request);

        // then
        assertThat(actual).contains(expected);
    }

    @Test
    void 핸들러_매핑_목록에서_요청에_맞는_핸들러_매핑이_없는_경우_비어있는_Optional을_반환한다() {
        // given
        final HandlerMappingComposite handlerMappingComposite = new HandlerMappingComposite(Collections.emptyList());

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        final Optional<HandlerMapping> actual = handlerMappingComposite.getHandlerMapping(request);

        // then
        assertThat(actual).isEmpty();
    }
}
