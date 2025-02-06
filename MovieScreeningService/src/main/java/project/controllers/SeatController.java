package project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.dtos.SeatDTO;
import project.entities.Seat;
import project.services.SeatService;

@RestController
@RequestMapping("project/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/seat/{id}")
    public ResponseEntity<Response> findSeatByUuid(@PathVariable int id) {
        Seat seatFound = seatService.getSeatByUuid(id);
        if (seatFound == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Seat with id " + id + " doesn't exist")
                            .responseType(ResponseType.ERROR)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(SeatDTO.builder()
                                .uuid(seatFound.getUuid())
                                .seat_number(seatFound.getSeat_number())
                                .available(seatFound.isAvailable())
                                .row_number(seatFound.getRow_number())
                                .build())
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }
}
