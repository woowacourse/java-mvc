# @MVC 구현하기

## 1단계 요구 사항

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

## 2단계 요구 사항

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

## 3단계 요구 사항

- [x] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

### 특이점

- 모든 controller 의 메서드에서 ModelAndView 를 반환하는 이유
  - 실제 스프링에서는 ModelAndView 로 반환해도, String 으로 반환해도 모두 ModelAndView 형식으로 변환되어 DispatcherServlet 에서 받을 수 있습니다.
  - Json 객체는 MappingJackson2JsonView 로 관리를 해야하는데, 이는 controller 메서드에서 MappingJackson2JsonView 를 생성하고 ModelAndView 에 넣어 해당 ModelAndView 에서 반환해야 합니다.
  - 결국 ModelAndView 를 반환하지 않고 String 으로 반환하여 view 로 변환되는 로직은 String 으로 resource 의 Path 를 반환할 때인데 이 변환 로직을 해당 미션에서 적용할지에 대한 의문이 들었고, 적용할지 정하기 위해 Spring 에서 해당 로직들을 찾아보았습니다.
  - 먼저 handler adapter 에서는 ModelAndView 를 생성하고, handler method 로 invokeAndHandle 메서드를 호출하는 로직을 가지고 있습니다.
  - handler method 의 invokeAndHandle 메서드에서는 실제 controller 의 메서드를 invoke 하는 로직과 응답으로 받은 Object 를 `ModelAndViewContainer` 에 알맞은 값으로 핸들링하는 로직을 가지고 있습니다. 그리고 알맞은 값으로 핸들링하는 과정에서 응답이 String 인지, ModelAndView 인지 확인할 때 `ReturnValueHandler` 를 이용합니다.
  1. ModelAndView 가 반환되었을 때
     1. `ModelAndViewReturnValueHandler` 가 select 되고 MAVContainer 에 view name 을 세팅합니다.
  2. String 이 반환되었을 때
     1. `ViewNameMethodReturnValueHandler` 가 select 되고 MAVContainer 에 view name 을 세팅합니다.
     2. "/index.html" 이든 "eden" 이든 resource 가 존재하는지 확인은 하지 않고 그냥 때려박습니당
  3. 객체가 반환되었을 때
     1. `ServletModelAttributeMethodProcessor` 가 select 되고 view name 없이 model 에 반환받은 객체가 저장된 ModelAndView 가 반환됩니다.
     2. 후에 view name 은 DispatcherServlet 에서 applyDefaultViewName() 메서드로 `url path` 가(ex, api/user) view name 으로 설정됩니다.
     3. 그래서 이러한 경우에는 객체를 반환하는데, 404 에러가 뜨더라구요! (url path 에 맞는 resource 가 없으니까)
  4. 번외) `@ResponseBody` 와 함께 객체가 반환되었을 때
     1. `RequestResponseBodyMethodProcessor` 가 select 되고 MAVContainer `request handled directly` 라는 flag 와 함께 ModelAndView 는 null 이 반환됩니다.
     2. view 처리 로직 없이 객체를 읽어 Json 형태로 반환됩니다.
  - 위의 경우들을 종합해보았을 때, 이번 미션의 목표인 json 변환은 model and view 를 이용하기때문에, 기존 asis 로 있었던 String 반환 메서드도 모두 ModelAndView 로 수정하여, String 을 view 로 변환하는 로직(json 변환과 관련이 없기때문에) 없이 진행하게 되었습니다! 
