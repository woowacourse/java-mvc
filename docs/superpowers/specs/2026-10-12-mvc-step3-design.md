# MVC 3단계 설계안

## 목표와 기준 상태

DTO, JSON 응답 계약, ObjectMapper 재사용, API 오류 응답, forward/redirect 선택에 대한 리뷰를 반영해 어노테이션 MVC 전환과 JsonView 구현을 완료한다. 이전 리뷰의 핸들러 검증 책임과 상속 메서드 중복도 함께 정리한다.

- 작업 폴더: /Users/jinriro/Desktop/java-mvc
- 확인한 브랜치: step3
- 기준 HEAD: d6753b9ae74097b19a0b96465d93d8959e1eb928
- 현재 JsonView는 빈 구현이고, DispatcherServlet과 레거시 매핑은 app에 있다.
- HandlerExecution의 메서드 검증은 AnnotationHandlerMapping에 있다.
- 기준 확인: 2026-10-12, `:mvc:clean :mvc:test :app:test --offline --no-daemon` 성공.
- 최초 실행의 `DuplicateController 2.class` 등 잘못된 생성 파일은 Gradle clean 후 사라졌다.
- 이 문서는 사용자 요청에 따른 계획의 설계 기준이며, 제품 구현은 후속 작업이다.

## 전역 제약

- Java source/target 21.
- Jackson databind 2.19.0, Servlet API 6.1.0, Tomcat 11.0.10을 유지한다.
- 외부 의존성을 추가하거나 버전을 변경하지 않는다.
- mvc의 운영 코드가 app의 com.techcourse 타입을 참조하지 않는다.
- View.render(Map<String, ?>, HttpServletRequest, HttpServletResponse)와 ModelAndView 계약을 유지한다.
- 작업은 작은 검증 가능한 커밋으로 나누고, 변경 동작의 회귀 테스트를 먼저 작성한다.
- 기존 study 학습 과제는 이번 구현 범위에서 수정하지 않는다.

## 결정 1: JSON 구조와 DTO

기본안은 모델 Map 전체를 항상 직렬화한다. 단일 모델만 자동으로 펼치는 정책은 사용하지 않는다. 명시적인 body 선택 기능은 현재 요구에 없어 추가하지 않는다.

- app의 `com.techcourse.controller.dto.UserResponse`: `public record UserResponse(String account) {}`.
- app의 `com.techcourse.controller.dto.ErrorResponse`: `public record ErrorResponse(String code, String message) {}`.
- 성공 모델: `addObject("user", new UserResponse(user.getAccount()))`.
- 성공 JSON: `{"user":{"account":"gugu"}}`.
- 오류 모델: `addObject("error", new ErrorResponse(code, message))`.
- 빈 모델은 `{}`, 컬렉션·중첩 값·null 값은 모델의 구조 그대로 직렬화한다.
- 모델에 message 같은 항목을 추가해도 기존 `user.account` 경로가 유지된다.
- User 도메인의 getter를 늘리지 않고 응답 필드를 DTO에 명시한다.

대안으로 명시적인 본문 선택 기능을 두면 `{"account":"gugu"}`도 가능하지만, 이번에는 기존 View/ModelAndView를 그대로 사용하는 단순한 Map 직렬화를 선택한다.

## 결정 2: JsonView와 ObjectMapper

- JsonView는 기존 무인자 생성자를 유지한다.
- JsonView의 `private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()`를 요청과 View 인스턴스 사이에서 공유한다.
- mapper 설정은 최초 사용 전에 완료하고 요청 처리 중 변경하지 않는다.
- Content-Type은 기존 `MediaType.APPLICATION_JSON_UTF8_VALUE`, 문자 인코딩은 UTF-8이다. writer를 얻기 전에 설정한다.
- JSON 직렬화는 mvc 모듈만 담당한다. app이 Jackson을 직접 호출하지 않는다.
- 앱별 mapper 주입과 사용자 설정 API는 필요가 생겼을 때 확장한다.
- 근거: [Jackson 2.19 공식 설명](https://github.com/FasterXML/jackson-databind/blob/jackson-databind-2.19.0/src/main/java/com/fasterxml/jackson/databind/ObjectMapper.java).

## 결정 3: 사용자 조회 API와 오류

`GET /api/user?account=...`를 추가한다. account는 정확히 전달된 값으로 조회하고 임의로 trim하거나 대소문자를 변경하지 않는다.

| 입력 | 상태 | JSON |
| --- | --- | --- |
| 존재하는 account | 200 | {"user":{"account":"gugu"}} |
| account 없음, 빈 문자열, 공백만 있음 | 400 | {"error":{"code":"INVALID_ACCOUNT","message":"account is required"}} |
| 존재하지 않는 account | 404 | {"error":{"code":"USER_NOT_FOUND","message":"user not found"}} |

입력 검증과 조회 실패 처리는 UserController가 맡는다. 정상적으로 예상되는 실패에 예외를 던지지 않고 response.setStatus와 오류 DTO를 사용한 ModelAndView를 반환한다. 전역 예외 리졸버나 mvc의 사용자 도메인 예외 지식은 추가하지 않는다. 예상하지 못한 실행·렌더링 예외는 기존 DispatcherServlet의 ServletException 처리로 유지한다.

## 결정 4: 어노테이션 MVC 전환과 모듈 경계

- DispatcherServlet을 `com.interface21.webmvc.servlet`로 이동한다.
- 공개 생성자: `DispatcherServlet(String basePackage)`.
- 앱 initializer가 `"com.techcourse.controller"`를 전달한다.
- 기존 HandlerMapping/HandlerAdapter 인터페이스를 유지하고, 초기화 완료 후 List.of로 구성한다.
- 등록 구현은 AnnotationHandlerMapping과 HandlerExecutionAdapter만 남긴다.
- ManualHandlerMapping, asis 패키지, LoginViewController, RegisterViewController를 제거한다.
- 컨트롤러 전환과 레거시 삭제·Dispatcher 이동은 같은 컴파일 가능한 변경 단위로 처리한다.

| 요청 | 처리 |
| --- | --- |
| GET / | HomeController → /index.jsp forward, 주소 / 유지 |
| GET /login | LoginController.show → /login.jsp forward, 로그인 상태라면 기존 redirect |
| POST /login | LoginController.save → 기존 세션 등록·성공/실패 redirect 유지 |
| GET /logout | LogoutController → 세션 사용자 제거, / redirect |
| GET /register | RegisterController.show → /register.jsp forward |
| POST /register | 기존 저장과 /index.jsp redirect 유지 |
| GET /login/view | LoginController의 호환 메서드가 show에 위임 |
| GET /register/view | RegisterController의 호환 메서드가 show에 위임 |

index.jsp, index.html, login.jsp, register.jsp의 로그인/회원가입 링크는 /login, /register로 통일한다. 이전 링크와 북마크는 어노테이션 호환 메서드로 계속 처리한다. API 라우팅에서 context path 제외 동작을 유지한다. 컨텍스트 경로가 있는 배포의 화면 링크 전체 개편은 별도 범위다.

redirect 모델에 불필요한 User를 넣지 않는다. flash/session 전달 기능은 이번 단계에 추가하지 않는다.

## 결정 5: 핸들러 유효성과 상속 매핑

- HandlerExecution(Object controller, Method method) 생성자가 public, 정확한 (HttpServletRequest, HttpServletResponse) 인자, ModelAndView 호환 반환 타입을 검증한다.
- 검증 실패 메시지는 기존 `Invalid handler method: 선언클래스.메서드` 형식을 유지한다.
- AnnotationHandlerMapping은 생성·검증을 HandlerExecution에 위임하고 발견·매핑 등록을 담당한다.
- 메서드 이름과 인자 타입이 같은 선언은 가장 구체적인 실제 메서드로 모은다. 가장 구체적인 선언의 매핑을 우선하고, 어노테이션이 없다면 가장 가까운 부모 선언의 매핑을 상속한다.
- 클래스 계층에 매핑이 없으면 인터페이스 매핑을 사용한다. 서로 다른 인터페이스가 같은 메서드에 충돌하는 매핑을 선언하면 명시적으로 초기화 실패 처리한다.
- 부모·자식의 동일 매핑 override는 한 번만 등록한다. 서로 다른 실제 메서드의 동일 URL/HTTP 메서드는 계속 중복 오류다.
- initialize는 임시 Map을 완성한 뒤 Map.copyOf로 게시한다. 실패 시 부분 매핑을 노출하지 않고, 재호출 시 기존 매핑과 충돌하지 않도록 새로 구성한다.

## 검증과 완료 조건

JsonView 구조·UTF-8 테스트, UserController DTO·400/404 테스트, 실제 Dispatcher→handler→View 테스트, 동적 포트를 사용하는 실제 HTTP API 테스트를 수행한다. 홈 forward, 로그인·가입 POST redirect, 로그아웃, 이전 화면 경로, context path API 요청, 상속 매핑과 직접 생성 검증을 확인한다.

`:mvc:check :app:check`와 두 모듈 빌드가 성공해야 완료다. 전체 `test`도 실행하되 study 실패가 발생하면 실패 이름과 원인을 따로 기록하고 이번 변경과의 관련성을 확인한다. 실행 가능한 테스트로 확인하지 못한 항목은 완료했다고 표기하지 않는다.

