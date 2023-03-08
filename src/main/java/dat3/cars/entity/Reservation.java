package dat3.cars.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @CreationTimestamp
  LocalDateTime reservationDate;

  //The day for which the car is reserved
  LocalDate rentalDate;

  @ManyToOne
  Car car;

  @ManyToOne
  Member member;

  public Reservation(Member member, Car car, LocalDate rentalDate) {
    this.member = member;
    this.car = car;
    this.rentalDate = rentalDate;
  }
}
