package project.controllers.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String message;
    private Object responseObject;
    private ResponseType responseType;
}
