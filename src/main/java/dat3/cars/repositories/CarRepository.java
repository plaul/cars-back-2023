package dat3.cars.repositories;

import dat3.cars.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
  List<Car> findByBrandAndModel(String brand, String model);

  @Query(value = "SELECT AVG(c.pricePrDay) from Car c")
  Double avrPricePrDay();

  @Query("SELECT c FROM Car c WHERE c.bestDiscount = (SELECT MAX(c.bestDiscount) FROM Car c)")
  List<Car> findAllByBestDiscount();

  List<Car> findByReservationsIsEmpty();
}