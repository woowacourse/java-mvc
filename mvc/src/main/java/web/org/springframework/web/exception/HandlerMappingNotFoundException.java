package web.org.springframework.web.exception;

public class HandlerMappingNotFoundException extends RuntimeException {

        public HandlerMappingNotFoundException(final String message) {
            super(message);
        }
}
