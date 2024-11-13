package project.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import project.entities.Seat;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieHallDTO {
    private int id;
    private List<SeatDTO> configuration;
    private int uuid;
}
