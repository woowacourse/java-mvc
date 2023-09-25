package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerMappingsTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HandlerMapping mockHandlerMapping;

    @Mock
    private Controller mockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 요청에_맞는_handler를_반환한다() {
        // given
        given(mockHandlerMapping.getHandler(request))
                .willReturn(mockController);
        HandlerMappings mappings = new HandlerMappings();
        mappings.add(mockHandlerMapping);

        // when
        Object handler = mappings.getHandler(request);

        // then
        assertThat(Controller.class.isAssignableFrom(handler.getClass())).isTrue();
    }

    @Test
    void 요청에_맞는_handler가_없을_경우_예외를_던진다() {
        // given
        HandlerMappings mappings = new HandlerMappings();
        mappings.add(mockHandlerMapping);

        // expect
        assertThatThrownBy(() -> mappings.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하는 handler가 존재하지 않습니다.");
    }

    @Test
    void handler를_추가한다() {
        // given
        HandlerMappings mappings = new HandlerMappings();

        // expect
        assertThatNoException().isThrownBy(() -> mappings.add(new AnnotationHandlerMapping()));
    }
}
