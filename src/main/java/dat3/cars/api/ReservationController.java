package dat3.cars.api;


import dat3.cars.dto.ReservationRequest;
import dat3.cars.dto.ReservationResponse;
import dat3.cars.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservations")
@CrossOrigin
public class ReservationController {

  ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  //Security AUTHENTICATED  --> Rewrite to use the currently logged in user ONLY
  @PostMapping
  ReservationResponse makeReservation(@RequestBody ReservationRequest body){
    return reservationService.makeReservation(body);
  }

  //Security AUTHENTICATED  --> Rewrite to use the currently logged in user ONLY
  @GetMapping("/{username}")
  public List<ReservationResponse> getReservationsForUser(@PathVariable String username){
    List<ReservationResponse> res = reservationService.getReservationsForUser(username);
    return res;
  }
}
