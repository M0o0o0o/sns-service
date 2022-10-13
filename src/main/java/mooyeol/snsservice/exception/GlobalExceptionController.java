package mooyeol.snsservice.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice()
public class GlobalExceptionController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<BindResponse> bindingExceptionHandler(BindException e) {
        return BindResponse.getBindResponseList(e.getBindingResult());
    }


    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchEx(TypeMismatchException e) {
        return "bad Request ";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle404(NoHandlerFoundException e) {
        return new ErrorResponse("페이지를 찾을 수 없습니다.");
    }


    @Getter
    @Setter
    static class BindResponse {
        private String field;
        private String message;

        private BindResponse(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public static List<BindResponse> getBindResponseList(BindingResult bindingResult) {
            List<BindResponse> bindResponseList = new ArrayList<>();

            bindingResult.getFieldErrors().stream().forEach(e -> {
                bindResponseList.add(new BindResponse(e.getField(), e.getDefaultMessage()));
            });

            return bindResponseList;
        }
    }
}

