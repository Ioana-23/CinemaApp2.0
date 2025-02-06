package project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.services.UserService;

@RestController
@RequestMapping("project/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(userService.getAllUsers())
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }
}
