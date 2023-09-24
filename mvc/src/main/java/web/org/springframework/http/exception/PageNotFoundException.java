package web.org.springframework.http.exception;

public class PageNotFoundException extends HttpException {

    public PageNotFoundException() {
        this.statusCode = 404;
        this.message = "요청하신 페이지를 찾을 수 없습니다 :";
    }
}
