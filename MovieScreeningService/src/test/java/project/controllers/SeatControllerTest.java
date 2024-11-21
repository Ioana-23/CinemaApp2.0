package project.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.MovieScreeningApplication;
import project.controllers.response.ResponseType;
import project.entities.Seat;
import project.services.SeatService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SeatController.class)
@ContextConfiguration(classes = MovieScreeningApplication.class)
@ExtendWith(SpringExtension.class)
public class SeatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    private Seat seat;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        seat = Seat.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getSeatByUuid_returnsFound() throws Exception {
        Mockito.when(seatService.getSeatByUuid(UUID)).thenReturn(seat);

        mockMvc.perform(get("/project/seats/seat/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.uuid", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void getSeatByUuid_returnsNotFound() throws Exception {
        mockMvc.perform(get("/project/seats/seat/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Seat with id " + UUID + " doesn't exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }
}
