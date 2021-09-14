package nextstep.mvc.returntype;

import nextstep.web.support.StatusCode;

public class ResponseEntity {

    private StatusCode statusCode;
    private Object body;

    public ResponseEntity(StatusCode statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static ResponseEntity ok(Object body) {
        return new ResponseEntity(StatusCode.OK, body);
    }

    public static ResponseEntity ok() {
        return ok(null);
    }

    public static ResponseEntity notFound() {
        return new ResponseEntity(StatusCode.NOT_FOUND, null);
    }

    public StatusCode getStatusCode() {
        if(statusCode == null) {
            return StatusCode.OK;
        }
        return statusCode;
    }

    public Object getBody() {
        return body;
    }
}
