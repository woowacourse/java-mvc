package web.org.springframework.http.exception;

public class MethodNotAllowedException extends HttpException {


    public MethodNotAllowedException(final String method) {
        this.statusCode = 405;
        this.message = "요청하신 메소드를 사용할 수 없습니다" + method;
    }

    public MethodNotAllowedException() {
        this("");
    }
}
