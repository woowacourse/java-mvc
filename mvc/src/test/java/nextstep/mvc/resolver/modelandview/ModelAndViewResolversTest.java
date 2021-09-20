package nextstep.mvc.resolver.modelandview;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import nextstep.mvc.controller.ResponseEntity;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ModelAndViewResolversTest {

    @DisplayName("지원여부 테스트")
    @ParameterizedTest
    @MethodSource
    void supportsTest(ModelAndViewResolver modelAndViewResolver, Object returnType) {
        //given
        //when
        //then
        assertThat(modelAndViewResolver.isSupport(returnType.getClass())).isTrue();
    }

    private static Stream<Arguments> supportsTest() {
        return Stream.of(
            Arguments.of(
                new JustModelAndViewResolver(),
                new ModelAndView(new JsonView())
            ),
            Arguments.of(
                new ResponseEntityToJsonModelAndViewResolver(),
                new ResponseEntity<>("")
            ),
            Arguments.of(
                new StringToJspModelAndViewResolver(),
                "index.html"
            ));
    }

    @DisplayName("justModelAndViewResolver 기능 테스트")
    @Test
    void justModelAndViewResolverTest() {
        //given
        JustModelAndViewResolver justModelAndViewResolver = new JustModelAndViewResolver();
        //when
        ModelAndView given = new ModelAndView(new JsonView());
        ModelAndView expectedResult = justModelAndViewResolver
            .chooseProperModelAndView(given);
        //then
        assertThat(given).isEqualTo(expectedResult);
    }

    @DisplayName("ResponseEntityToJsonModelAndViewResolver 기능 테스트")
    @Test
    void responseEntityResolverTest() {
        //given
        ResponseEntityToJsonModelAndViewResolver resolver = new ResponseEntityToJsonModelAndViewResolver();
        //when
        ResponseEntity<String> givenData = new ResponseEntity<>("givenData");
        ModelAndView expectedResult = resolver
            .chooseProperModelAndView(givenData);
        //then
        assertThat(expectedResult.getObject("data")).isEqualTo(givenData.getValue());
    }

    @DisplayName("StringToJspModel 기능 테스트")
    @Test
    void stringToJspModelTest() {
        //given
        StringToJspModelAndViewResolver resolver = new StringToJspModelAndViewResolver();
        //when
        ModelAndView expectedResult = resolver
            .chooseProperModelAndView("index.html");
        //then
        assertThat(expectedResult.getView()).isInstanceOf(JspView.class);
    }
}
