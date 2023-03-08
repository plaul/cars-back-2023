package dat3.cars.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationRequest {
  private int carId;
  private String username;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateToReserveCar;
}
