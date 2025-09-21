### org.springframework.web.servlet

- `servlet.config`: MVC 설정 관리 (WebMvcConfigurer, DelegatingWebMvcConfiguration)
- `servlet.config.annotation`: 어노테이션 기반 MVC 설정 (@EnableWebMvc, @WebMvcTest)
- `servlet.function`: 함수형 웹 프레임워크 지원 (RouterFunction, HandlerFunction)
- `servlet.handler`: 핸들러 매핑 및 어댑터 (HandlerMapping, HandlerAdapter, HandlerInterceptor)
- `servlet.i18n`: 국제화 지원 (LocaleResolver, LocaleChangeInterceptor)
- `servlet.mvc`: MVC 컨트롤러 기본 인터페이스 및 구현체
- `servlet.mvc.annotation`: 어노테이션 기반 컨트롤러 (@Controller, @RequestMapping)
- `servlet.mvc.condition`: 요청 매핑 조건 (RequestCondition, PatternsRequestCondition)
- `servlet.mvc.method`: 메서드 레벨 요청 처리
- `servlet.mvc.method.annotation`: 어노테이션 기반 메서드 처리 (@RequestParam, @PathVariable, @RequestBody)
- `servlet.mvc.support`: MVC 지원 유틸리티 (DefaultHandlerExceptionResolver, RedirectAttributes)
- `servlet.resource`: 정적 리소스 처리 (ResourceHttpRequestHandler, ResourceResolver)
- `servlet.support`: 서블릿 지원 유틸리티 (RequestContext, ServletUriComponentsBuilder)
- `servlet.tags`: JSP 태그 라이브러리 (spring:form, spring:message)
- `servlet.theme`: 테마 관리 (ThemeResolver, ThemeChangeInterceptor)
- `servlet.view`: 뷰 처리 및 해결 (View, ViewResolver, InternalResourceViewResolver)
- `servlet.view.json`: JSON 뷰 지원 (MappingJackson2JsonView)
- `servlet.view.xml`: XML 뷰 지원 (MarshallingView)

### org.springframework.web.servlet.mvc의 asis와 tobe 패키지

- 실제로 존재하는 패키지가 아니라 레거시와 현재 표준을 구별하기 위한 임시패키지
