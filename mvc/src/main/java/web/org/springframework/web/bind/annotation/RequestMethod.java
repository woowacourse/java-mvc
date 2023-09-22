package web.org.springframework.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod resolve(String method) {
        RequestMethod requestMethod;
        switch (method) {
            case "GET":
                requestMethod = GET;
                break;
            case "HEAD":
                requestMethod = HEAD;
                break;
            case "POST":
                requestMethod = POST;
                break;
            case "PUT":
                requestMethod = PUT;
                break;
            case "PATCH":
                requestMethod = PATCH;
                break;
            case "DELETE":
                requestMethod = DELETE;
                break;
            case "OPTIONS":
                requestMethod = OPTIONS;
                break;
            case "TRACE":
                requestMethod = TRACE;
                break;
            default:
                throw new RuntimeException();
        }
        ;
        return requestMethod;
    }
}
