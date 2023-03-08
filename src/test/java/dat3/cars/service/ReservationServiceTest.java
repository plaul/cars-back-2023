package dat3.cars.service;

import dat3.cars.dto.ReservationRequest;
import dat3.cars.dto.ReservationResponse;
import dat3.cars.entity.Car;
import dat3.cars.entity.Member;
import dat3.cars.entity.Reservation;
import dat3.cars.repositories.CarRepository;
import dat3.cars.repositories.MemberRepository;
import dat3.cars.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationServiceTest {

  ReservationService reservationService;

  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  CarRepository carRepository;
  @Autowired
  MemberRepository memberRepository;

  static int carId;
  boolean dataIsInitialized = false;
  @BeforeEach
  void setUp() {
    if(dataIsInitialized) {
      return;
    }
    reservationRepository.deleteAll();
    memberRepository.deleteAll();
    carRepository.deleteAll();
    Car volvo = Car.builder().brand("Volvo").model("V70").pricePrDay(700).bestDiscount(30).build();
    volvo = carRepository.saveAndFlush(volvo);
    carId = volvo.getId();
    Member m1 = memberRepository.saveAndFlush(new Member("m1", "pw", "m1@a.dk", "fn", "ln", "vej 2", "Lyngby", "2800"));
    //Reserve the Car
    Reservation r = reservationRepository.saveAndFlush(new Reservation(m1,volvo, LocalDate.of(2028,6,6)));

    reservationService = new ReservationService(reservationRepository,memberRepository,carRepository);

    dataIsInitialized = true;
  }

  @Test
  void makeReservation() {
    ReservationRequest request = ReservationRequest.builder().username("m1").carId(carId).dateToReserveCar(LocalDate.of(2023,5,5)).build();
    ReservationResponse response = reservationService.makeReservation(request);
    reservationRepository.flush();
    assertEquals("m1",response.getMemberUsername());
    assertEquals(carId,response.getCarId());
    assertTrue(response.getId()>1);
  }

  @Test
  public void reserveAReservedCarThrows(){
    ReservationRequest request = ReservationRequest.builder().username("m1").carId(carId).dateToReserveCar(LocalDate.of(2028,6,6)).build();
    assertThrows(ResponseStatusException.class,()->reservationService.makeReservation(request));
  }
  @Test
  public void reserveACarInPastThrows(){
    ReservationRequest request = ReservationRequest.builder().username("m1").carId(carId).dateToReserveCar(LocalDate.of(2022,7,7)).build();
    assertThrows(ResponseStatusException.class,()->reservationService.makeReservation(request));
  }

}