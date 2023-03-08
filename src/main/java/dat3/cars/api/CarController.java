package dat3.cars.api;

import dat3.cars.dto.CarRequest;
import dat3.cars.dto.CarResponse;
import dat3.cars.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/cars" )
@RestController
@CrossOrigin
public class CarController {

  CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  //Security ANONYMOUS
  @GetMapping
  List<CarResponse> getCars(){
    return carService.getCars(false); //false --> Only info relevant for customers
  }

  //Security ANONYMOUS
  @GetMapping(path = "/{id}")
  CarResponse getCarById(@PathVariable int id) {
    return carService.findCarById(id, false);
  }
  //Security ADMIN
  @GetMapping(path = "/admin")
  List<CarResponse>  getCarsAdmin() {
    return carService.getCars(true);
  }
  //Security ADMIN
  @GetMapping(path = "/admin/{id}")
  CarResponse getCarByIdAdmin(@PathVariable int id) {
    return carService.findCarById(id, true);
  }

  //Security ADMIN
  @PostMapping
  CarResponse addCar(@RequestBody CarRequest body){
    return carService.addCar(body);
  }

  //Security ADMIN  ONLY
  @PutMapping("/{id}")
  CarResponse editCar(@RequestBody CarRequest body, @PathVariable int id){
    return carService.editCar(body, id);
  }

  //Security ADMIN ONLY
  @PatchMapping("/price/{id}/{newPrice}")
  void setPrice(@PathVariable int id, @PathVariable double newPrice) {
    carService.setPrice(id, newPrice);
  }
  //Security ADMIN ONLY
  @PatchMapping("/discount/{id}/{newDiscount}")
  void setPrice(@PathVariable int id, @PathVariable int newDiscount) {
    carService.setDiscount(id, newDiscount);
  }

  // Security ADMIN ONLY
  @DeleteMapping("/{id}")
  ResponseEntity<Boolean> deleteCar(@PathVariable int id) {
    return carService.deleteCar(id);
  }
}
