package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DispatcherServlet은 스프링 MVC의 핵심 서블릿으로,
 * 클라이언트의 HTTP 요청을 받아 적절한 핸들러(컨트롤러)를 찾아 실행하고,
 * 결과인 ModelAndView를 통해 뷰를 렌더링하는 역할을 수행한다.
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings; // 요청 URI와 매핑된 핸들러(컨트롤러)를 찾아주는 HandlerMapping 리스트
    private List<HandlerAdaptor> handlerAdaptors; // 핸들러를 실행하는 HandlerAdaptor 리스트 (핸들러 종류별 어댑터 역할)

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMapping();
        initHandlerAdaptor();
    }

    /**
     * 핸들러 어댑터 초기화.
     * 다양한 형태의 핸들러(컨트롤러)를 실행할 수 있도록 어댑터 인스턴스 생성
     */
    private void initHandlerAdaptor() {
        this.handlerAdaptors = List.of(
                new ManualHandlerAdaptor(), // 직접 구현한 수동 어댑터
                new AnnotationHandlerAdaptor() // Annotation 기반 컨트롤러 어댑터
        );
    }

    /**
     * 핸들러 매핑 초기화.
     * 요청 URL을 핸들러에 매핑하는 매핑 객체들을 생성하고 초기화 호출
     */
    private void initHandlerMapping() {
        handlerMappings = List.of(
                new ManualHandlerMapping(), // 수동 핸들러 매핑
                new AnnotationHandlerMapping("com.techcourse.controller") // Annotation 매핑, 특정 패키지 스캔
        );

        for (HandlerMapping handlerMapping : handlerMappings) {
            log.info("initialize HandlerMapping :: %s".formatted(handlerMapping.getClass().getName()));
            handlerMapping.initialize(); // 초기화 (매핑되는 객체들 생성)
        }
    }

    /**
     * HTTP 요청을 처리
     * 1. 요청에 매핑된 핸들러가 있는지 조회
     * 2. 핸들러를 실행할 어댑터 조회
     * 3. 핸들러 실행
     * 4. 뷰 렌더링
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request); // 1. 요청에 매핑된 핸들러 조회
            HandlerAdaptor handlerAdaptor = getAdaptor(handler); // 2. 핸들러를 실행할 어댑터 조회

            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler); // 3. 핸들러 실행 후 ModelAndView 반환
            render(modelAndView, request, response); // 4. View 렌더링 수행
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * 핸들러에 맞는 HandlerAdaptor를 찾는 메서드.
     * 각 어댑터의 supports() 메서드를 통해 처리 가능 여부 판단
     * @param handler 실행할 핸들러 객체
     * @return 해당 핸들러를 처리할 수 있는 어댑터
     * @throws IllegalArgumentException 지원하는 어댑터가 없으면 예외 던짐
     */
    private HandlerAdaptor getAdaptor(Object handler) {
        for (HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if(handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("매핑 가능한 HandlerAdaptor가 존재하지 않습니다. (Handler type: %s)"
                .formatted(handler.getClass().getName()));
    }

    /**
     * 요청에 맞는 핸들러를 찾는 메서드.
     * 등록된 HandlerMapping 리스트에서 순차적으로 요청 URI에 매핑되는 핸들러 조회
     * @param request 클라이언트 요청 객체
     * @return 요청을 처리할 핸들러 객체
     * @throws IllegalArgumentException 매핑 가능한 핸들러가 없으면 예외 던짐
     */
    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if(handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("매핑 가능한 Handler가 존재하지 않습니다. (Request URI : %s"
                .formatted(request.getRequestURI()));
    }

    /**
     * ModelAndView를 통해 얻은 View를 호출해 실제 응답을 렌더링한다.
     * @param modelAndView 모델과 뷰 정보를 담고 있는 객체
     * @param request 요청 객체
     * @param response 응답 객체
     * @throws Exception 뷰 렌더링 실패 시 예외 발생 가능
     */
    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();

        view.render(model, request, response); // 뷰 객체의 렌더 메서드 호출로 화면 표시
    }
}
