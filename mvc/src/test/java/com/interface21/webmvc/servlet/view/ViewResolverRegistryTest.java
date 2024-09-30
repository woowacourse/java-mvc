package com.interface21.webmvc.servlet.view;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import com.interface21.webmvc.servlet.View;

import static org.assertj.core.api.Assertions.assertThat;

class ViewResolverRegistryTest {

    private ViewResolverRegistry viewResolverRegistry;

    @BeforeEach
    void setUp() {
        viewResolverRegistry = new ViewResolverRegistry();
        JsonViewResolver jsonViewResolver = new JsonViewResolver();
        JspViewResolver jspViewResolver = new JspViewResolver();
        viewResolverRegistry.addViewResolver(jsonViewResolver);
        viewResolverRegistry.addViewResolver(jspViewResolver);
    }

    static Stream<Object[]> viewNamesAndExpectedClasses() {
        return Stream.of(
                new Object[]{"index.jsp", JspView.class},
                new Object[]{"index.json", JsonView.class}
        );
    }

    @ParameterizedTest
    @MethodSource("viewNamesAndExpectedClasses")
    @DisplayName("viewName을 통해 View를 찾을 수 있다.")
    void resolveViewName(String viewName, Class<?> expectedViewClass) {
        assertThat(viewResolverRegistry.resolveViewName(viewName)).isInstanceOf(expectedViewClass);
    }

    @DisplayName("특정 viewName을 처리할 수 있는 ViewResolver가 없는 경우 기본 jsp 파일을 반환한다.")
    @Test
    void failGetHandlerAdapter() {
        // given
        String viewName = "unknown.xml";

        // when
        View view = viewResolverRegistry.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }
}
