package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handler.HandlerAdapter;
import nextstep.mvc.resolver.modelandview.JustModelAndViewResolver;
import nextstep.mvc.resolver.modelandview.ModelAndViewResolvers;
import nextstep.mvc.resolver.modelandview.ResponseEntityToJsonModelAndViewResolver;
import nextstep.mvc.resolver.modelandview.StringToJspModelAndViewResolver;
import nextstep.mvc.resolver.parameter.ParameterResolverExecutor;
import nextstep.mvc.resolver.parameter.RequestAndResponseParameterResolver;
import nextstep.mvc.resolver.parameter.RequestParameterResolver;
import nextstep.mvc.resolver.parameter.SessionResolver;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionAdapter implements HandlerAdapter {

    private final ParameterResolverExecutor parameterResolverExecutor;
    private final ModelAndViewResolvers modelAndViewResolvers;

    public HandlerExecutionAdapter(
        ParameterResolverExecutor parameterResolverExecutor,
        ModelAndViewResolvers modelAndViewResolvers) {
        this.parameterResolverExecutor = parameterResolverExecutor;
        this.modelAndViewResolvers = modelAndViewResolvers;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        Object[] parameters = parameterResolverExecutor
            .captureProperParameter(handlerExecution.getMethod(), request, response);

        Object handleResult = handlerExecution.handle(parameters);
        return findProperModelAndView(handleResult);
    }

    private ModelAndView findProperModelAndView(Object handleResult) {
        return modelAndViewResolvers.resolveProperModelAndView(handleResult);
    }

    public static class Builder {

        private ParameterResolverExecutor parameterResolverExecutor;
        private ModelAndViewResolvers modelAndViewResolvers;

        public Builder setParameterResolverExecutor(
            ParameterResolverExecutor parameterResolverExecutor) {
            this.parameterResolverExecutor = parameterResolverExecutor;
            return this;
        }

        public Builder setModelAndViewResolvers(ModelAndViewResolvers modelAndViewResolvers) {
            this.modelAndViewResolvers = modelAndViewResolvers;
            return this;
        }

        public Builder setDefault() {
            this.modelAndViewResolvers = defaultModelAndViewResolvers();
            this.parameterResolverExecutor = defaultParameterResolverExcutor();
            return this;
        }

        private ParameterResolverExecutor defaultParameterResolverExcutor() {
            return new ParameterResolverExecutor(
                new RequestAndResponseParameterResolver(),
                new SessionResolver(),
                new RequestParameterResolver()
            );
        }

        private ModelAndViewResolvers defaultModelAndViewResolvers() {
            return new ModelAndViewResolvers(
                new ResponseEntityToJsonModelAndViewResolver(),
                new StringToJspModelAndViewResolver(),
                new JustModelAndViewResolver()
            );
        }

        public HandlerExecutionAdapter build() {
            return new HandlerExecutionAdapter(parameterResolverExecutor, modelAndViewResolvers);
        }
    }
}
