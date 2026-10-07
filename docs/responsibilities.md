## AnnotationHandlerMapping

### 책임

HTTP 요청에 대응하는 Handler를 어노테이션을 기반으로 등록하고 조회한다.

### 계약

- [x] `@Controller`가 선언된 클래스를 Handler 등록 대상으로 사용한다.
- [x] `@RequestMapping`이 선언된 public 메서드를 Handler로 등록한다.
- [x] 요청 경로와 HTTP Method가 일치하는 Handler를 반환한다.
- [x] `@RequestMapping`에 HTTP Method가 지정되지 않으면 모든 HTTP Method에 대응한다.
- [x] 동일한 요청 경로와 HTTP Method의 Handler가 중복 등록되면 초기화에 실패한다.

## HandlerExecution

### 책임

Controller의 Handler 메서드를 실행한다.

### 계약

- [x] HTTP 요청과 응답을 전달해 Handler 메서드를 실행한다.
- [x] Handler 메서드의 실행 결과를 `ModelAndView`로 반환한다.

## HandlerKey

### 책임

요청 경로와 HTTP Method로 Handler를 식별한다.

### 계약

- [x] 요청 경로와 HTTP Method가 모두 같으면 같은 Handler를 식별한다.
 
## JspView

### 책임

JSP 기반 View를 렌더링한다.

### 계약

- [x] 리다이렉트 View는 지정된 경로로 리다이렉트한다.
- [x] Model의 값을 Request attribute로 전달한다.
- [x] 일반 View는 지정된 JSP로 요청을 전달한다.

## HandlerMapping

### 책임

HTTP 요청에 대응하는 Handler를 반환한다.

### 계약

- [x] HTTP 요청에 대응하는 Handler를 반환한다.
- [x] 요청에 대응하는 Handler가 없으면 `null`을 반환한다.

## DispatcherServlet

### 책임

요청을 처리할 Handler를 HandlerMapping에서 찾는다.

### 계약

- [x] 등록된 HandlerMapping들을 순회해 요청에 대응하는 Handler를 찾는다.
- [x] 모든 HandlerMapping에 대응하는 Handler가 없으면 404 응답 후 처리를 종료한다.
- [x] Handler를 찾았지만 지원하는 HandlerAdapter가 없으면 Handler 타입을 식별하는 예외를 발생시킨다.
- [x] 요청 처리 중 예외가 발생하면 `ServletException`의 원인으로 보존한다.

## HandlerAdapter

### 책임

Handler의 종류에 맞는 방식으로 Handler를 실행한다.

### 계약

- [x] 자신이 실행할 수 있는 Handler인지 판단한다.
- [x] Handler를 실행한다.

## ControllerScanner

### 책임

@Controller가 선언된 클래스를 찾아 인스턴스를 생성한다.

### 계약

- [x] 지정된 패키지에서 @Controller가 선언된 클래스를 찾는다.
- [x] 찾은 Controller 클래스의 인스턴스를 생성한다.
