## MVC 프레임워크

- 동적으로 URL과 Controller 매핑
- 비즈니스 로직에만 집중할 수 있도록 어노테이션 기반 MVC 프레임워크

## TODO

### 1단계

- [x] AnnotationHandlerMappingTest
- [x] AnnotationHandlerMapping.initialize()
    - [x] handlerExecutions 초기화
    - `@RequestMapping` value 가져오기
    - key = URL&HTTP 메서드
    - value = 컨트롤러
- [x] HandlerExecution
    - [x] `@RequestMapping`있는 메서드 리플렉션으로 메서드 호출
    - [x] ModelAndView 반환
- [x] `@RequestMapping`
    - [x] URL
    - [x] HTTP 메서드
        - [x] 설정되어 있지 않으면 모든 메서드 지원

- [x] JspView
    - [x] DispatcherServlet 클래스의 service 메서드에서 뷰에 대한 처리 파악
    - [x] JspView 클래스로 이동

### 2단계

**AnnotationHandlerMapping**

- [ ] ControllerScanner
    - 컨트롤러를 찾아서 인스턴스 생성
    - [ ] `@Controller`가 설정된 모든 클래스를 찾기 (Reflection)
    - [ ] 각 클래스(컨트롤러)의 인스턴스를 생성
- [ ] HandlerExecutions
    - 스캔한 컨트롤러 정보를 바탕으로 HandlerKey, HandlerExecution 매핑
    - [ ] HandlerKey
        - 매핑되는 메서드를 실행
    - [ ] HandlerExecution
        - 인스턴스 변수
            - 실행할 메서드의 인스턴스
            - 실행할 메서드

**DispatcherServlet**

- [x] HandlerMapping 인터페이스
    - [x] ManualHandlerMapping, AnnotationHandlerMapping 초기화
- [x] HandlerAdapter 인터페이스
    - [x] HandlerMapping으로 찾은 컨트롤러 실행
- [ ] JspViewResolver
