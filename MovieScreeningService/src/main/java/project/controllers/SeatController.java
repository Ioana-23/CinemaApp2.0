package project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.entities.MovieHall;
import project.entities.Seat;
import project.services.MovieHallService;
import project.services.SeatService;

import java.util.List;

@RestController
@RequestMapping("project/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/seat/{id}")
    public ResponseEntity<Seat> findSeatByUuid(@PathVariable int id)
    {
        Seat seatFound = seatService.findSeatByUuid(id);
        if(seatFound == null)
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(seatFound, HttpStatus.ACCEPTED);
    }
}
