# MVC 프레임워크 구현하기

## 미션 개요 
- MVC 프레임워크를 직접 구현하면서 Servlet에 대한 이해도를 높인다. 
- Servlet Container로 Tomcat을 실행하여 들어오는 요청에 대한 소켓 연결 및 ServletRequest, SerlvetResponse 생성 등을 처리한다. 
- 자바의 Reflection 라이브러리를 학습하여 활용하여 구현한다. 
- 기존 (이전 HTTP 서버 구현 미션)에는 컨트롤러가 추가되면 추가된 컨트롤러 객체를 RequestMapping 클래스에 수동으로 추가했어야 했다. 
- 프레임워크에 신경쓰지 않고 비지니스 로직 구현에 집중하도록 어노테이션 기반 MVC 프레임워크를 구현한다. 
- 기존의 Controller 인터페이스 기반 처리 방식 (Legacy MVC) 또한 유지되어 공존하도록 구현한다.

## 미션 키워드
- DispatcherServlet
- Reflection
- HandlerAdapter
- ModelAndView, rendering

## 미션 요구사항 
### Reflection 학습테스트
- [x] 학습테스트 완료

### Annotation 기반 MVC 프레임워크 구현 
- [x] Annotation 기반 Handler 구현
  - [x] `@Controller` annotation이 붙은 클래스 수집
  - [x] `@RequestMapping`이 붙은 메서드 가져와서 `HandlerKey`에 매핑
  - [x] 해당 메서드와 메서드가 선언된 class로 `HandlerExecution` 생성 
  - [x] `getHandler()` 로 적합한 handler 호출

- [x] HandlerAdapter 구현 
    - [x] 인터페이스 기반 컨트롤러 handler 처리 adapter
    - [x] 어노테이션 기반 컨트롤러 handler 처리 adapter

- [x] ModelAndView 구현
  - [x] `JspView.java` todo 구현

## 구현 

## 학습 내용 
- 추가 예정 
