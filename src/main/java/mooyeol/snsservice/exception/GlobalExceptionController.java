package mooyeol.snsservice.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<BindResponse> bindingExceptionHandler(BindException e) {
        return BindResponse.getBindResponseList(e.getBindingResult());
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

