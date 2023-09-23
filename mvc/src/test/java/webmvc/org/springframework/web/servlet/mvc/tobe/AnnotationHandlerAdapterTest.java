package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationHandlerAdapterTest {

    @Test
    void 어노테이션_핸들러_어답터는_HandlerExecution를_지원한다() {
        // given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // expected
        assertThat(annotationHandlerAdapter.isSupported(handlerExecution)).isTrue();
    }
}
