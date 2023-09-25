package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestLegacyController;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.supports.resolver.JspViewResolver;
import webmvc.org.springframework.web.servlet.mvc.supports.resolver.ViewResolvers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdaptersTest {

    @Test
    void 생성자는_호출하면_HandlerAdapters를_초기화한다() {
        assertDoesNotThrow(HandlerAdapters::new);
    }

    @Test
    void getHandlerAdapter_메서드는_해당_핸들러를_처리할_수_있는_HandlerAdapter가_있다면_해당_HandlerAdapter를_반환한다() {
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final ViewResolvers viewResolvers = new ViewResolvers();
        viewResolvers.addResolvers(new JspViewResolver());
        handlerAdapters.addHandlerAdapter(new ManualHandlerMappingAdapter(viewResolvers));

        final HandlerAdapter actual = handlerAdapters.getHandlerAdapter(new TestLegacyController());

        assertThat(actual).isNotNull();
    }

    @Test
    void getHandlerAdapter_메서드는_해당_핸들러를_처리할_수_있는_HandlerAdapter가_없으면_예외가_발생한다() {
        final HandlerAdapters handlerAdapters = new HandlerAdapters();

        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(new TestLegacyController()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("해당 Handler를 수행할 수 있는 HandlerAdapter가 존재하지 않습니다.");
    }
}
