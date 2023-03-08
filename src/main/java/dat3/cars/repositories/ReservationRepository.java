package dat3.cars.repositories;

import dat3.cars.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
  boolean existsByCarIdAndRentalDate(int carId, LocalDate rentalDate);
  List<Reservation> findByMemberUsername(String userName);

  Long countReservationsByMemberUsername(String username);
}
