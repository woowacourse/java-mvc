package web.org.springframework.http;

public class MediaType {
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_JSON_UTF8_VALUE = APPLICATION_JSON + ";charset=UTF-8";

    public static boolean isJson(String mediaType) {
        return APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(mediaType) || APPLICATION_JSON.equalsIgnoreCase(mediaType);
    }
}
