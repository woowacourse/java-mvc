package webmvc.org.springframework.web.servlet.mock.view;

import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.mockito.BDDMockito.when;

public class MockModelAndViews {

    private MockModelAndViews() {
    }

    public static ModelAndView HYENA_MODEL_AND_VIEW = hyenaModelAndView();
    public static ModelAndView HERB_MODEL_AND_VIEW = herbModelAndView();


    private static ModelAndView hyenaModelAndView() {
        final ModelAndView mockMv = Mockito.mock(ModelAndView.class);
        when(mockMv.getView()).thenReturn(new MockHyenaView());

        return mockMv;
    }

    private static ModelAndView herbModelAndView() {
        final ModelAndView mockMv = Mockito.mock(ModelAndView.class);
        when(mockMv.getView()).thenReturn(new MockHerbView());

        return mockMv;
    }
}
