package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationHandlerAdapterTest {

    @Test
    void Annotation기반_핸들러면_true_반환() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        assertThat(annotationHandlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void Annotation기반_핸들러가_아니면_false_반환() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        assertThat(annotationHandlerAdapter.supports(new Object())).isFalse();
    }
}
