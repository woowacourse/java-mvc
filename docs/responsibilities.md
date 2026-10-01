## AnnotationHandlerMapping

### 책임

HTTP 요청에 대응하는 Handler를 어노테이션을 기반으로 등록하고 조회한다.

### 계약

- [x] `@Controller`가 선언된 클래스를 Handler 등록 대상으로 사용한다.
- [x] `@RequestMapping`이 선언된 메서드를 Handler로 등록한다.
- [x] 요청 경로와 HTTP Method가 일치하는 Handler를 반환한다.
- [x] `@RequestMapping`에 HTTP Method가 지정되지 않으면 모든 HTTP Method에 대응한다.

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
