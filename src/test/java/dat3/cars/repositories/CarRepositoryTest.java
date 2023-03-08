package dat3.cars.repositories;

import dat3.cars.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CarRepositoryTest {

  @Autowired
  CarRepository carRepository;

  boolean testDataIsInitalized = false;
  double averagePriceForAllCars;
  @BeforeEach
  void setUp() {
    if(testDataIsInitalized)
      return;
    List<Car> cars = new ArrayList<>(Arrays.asList(
            Car.builder().brand("Suzuki").model("Swift").pricePrDay(350).bestDiscount(28).build(),
            Car.builder().brand("Kia").model("Optima").pricePrDay(450).bestDiscount(18).build(),
            Car.builder().brand("WW").model("Wagon").pricePrDay(400).bestDiscount(20).build(),
            Car.builder().brand("Volvo").model("S80").pricePrDay(600).bestDiscount(12).build(),
            Car.builder().brand("Suzuki").model("SX4").pricePrDay(400).bestDiscount(16).build(),
            Car.builder().brand("Suzuki").model("SX4").pricePrDay(400).bestDiscount(16).build(),
            Car.builder().brand("Suzuki").model("SX4").pricePrDay(400).bestDiscount(7).build(),
            Car.builder().brand("Kia").model("Sorento").pricePrDay(500).bestDiscount(22).build(),
            Car.builder().brand("WW").model("Pickup").pricePrDay(450).bestDiscount(28).build(),
            Car.builder().brand("Volvo").model("V60").pricePrDay(700).bestDiscount(15).build(),
            Car.builder().brand("Suzuki").model("Grand Vitara").pricePrDay(450).bestDiscount(12).build()));
    carRepository.saveAllAndFlush(cars);
    averagePriceForAllCars = cars.stream().mapToDouble(Car::getPricePrDay).average().orElse(0);
    testDataIsInitalized = true;
  }

  @Test
  void findByBrandAndModel() {
    assertEquals(3,carRepository.findByBrandAndModel("Suzuki","SX4").size());
  }
  @Test
  void findNothingByBrandAndModel() {
    assertEquals(0,carRepository.findByBrandAndModel("Suzuki","I Dont Exist").size());
  }

  @Test
  void findAveragePriceForAllCars(){
    double delta = 0.001;
    assertEquals(averagePriceForAllCars,carRepository.avrPricePrDay(),delta);
  }

  @Test
  public void findCarsWithHighestDiscount(){
    List<Car> cars = carRepository.findAllByBestDiscount();
    assertEquals(2,cars.size());
    List<String> carBrands = cars.stream().map(Car::getBrand).toList();
    assertThat(carBrands,containsInAnyOrder("Suzuki","WW"));
  }

}