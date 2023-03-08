package dat3.cars.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(name="car_brand",length = 50, nullable = false)
  String brand;

  @Column(name="car_model",length = 60, nullable = false)
  String model;

  @Column(name="rental_price-day")
  double pricePrDay;

  @Column(name="max_discount")
  int bestDiscount;

  @CreationTimestamp
  LocalDateTime created;

  @UpdateTimestamp
  LocalDateTime lastEdited;

  @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Reservation> reservations;

}
