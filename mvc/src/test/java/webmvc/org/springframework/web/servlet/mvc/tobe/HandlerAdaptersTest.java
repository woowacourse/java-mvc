package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdaptersTest {

    @Mock
    private HandlerExecution handlerExecution;

    @Mock
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 지원하는_adapter를_반환한다() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.add(handlerAdapter);

        given(handlerAdapter.supports(handlerExecution))
                .willReturn(true);

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handlerExecution);

        // then
        assertThat(handlerAdapter).isNotNull();
    }

    @Test
    void 지원하는_adapter가_없을_경우_예외를_던진다() {
        // given
        Object wrongHandler = "wrongHandler";
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // expect
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(wrongHandler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하는 Adapter가 존재하지 않습니다.");
    }

    @Test
    void adapter를_추가한다() {
        // given
        HandlerAdapters handlerAdapters = new HandlerAdapters();

        // expect
        assertThatNoException().isThrownBy(() -> handlerAdapters.add(new AnnotationHandlerAdapter()));
    }
}
