package dat3.cars.repositories;

import dat3.cars.entity.Car;
import dat3.cars.entity.Member;
import dat3.cars.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationRepositoryTest {
  MemberRepository memberRepository;
  ReservationRepository reservationRepository;
  CarRepository carRepository;

  public ReservationRepositoryTest(MemberRepository memberRepository, ReservationRepository reservationRepository, CarRepository carRepository) {
    this.memberRepository = memberRepository;
    this.reservationRepository = reservationRepository;
    this.carRepository = carRepository;
  }

  boolean dataIsInitialized = false;
  int carId;

  @BeforeEach
  void setUp() {
    if (dataIsInitialized) return;
    Car car = Car.builder().brand("Volvo").model("V70").pricePrDay(500).bestDiscount(10).build();
    carId = carRepository.saveAndFlush(car).getId();
    Member m1 = new Member("m1", "pw", "m1@a.dk", "bb", "jjj", "xx 34", "ly", "2800");
    Member m2 = new Member("m2", "pw", "m2@a.dk", "aa", "lll", "xx 31", "ly", "2800");
    memberRepository.saveAndFlush(m1);//Flush ensures date fields are set
    memberRepository.saveAndFlush(m2);
    reservationRepository.saveAndFlush(new Reservation(m1,car, LocalDate.of(2028,9,10)));

    reservationRepository.saveAndFlush(new Reservation(m2,car, LocalDate.of(2028,9,11)));
    reservationRepository.saveAndFlush(new Reservation(m2,car, LocalDate.of(2028,8,11)));

    dataIsInitialized = true;
  }

  @Test
  void existsByCarIdAndRentalDate() {
    assertTrue(reservationRepository.existsByCarIdAndRentalDate(carId,LocalDate.of(2028,8,11)));
  }

  @Test
  void findByMemberUsername() {
    List<Reservation> reservations1 = reservationRepository.findByMemberUsername("m1");
    assertEquals(1,reservations1.size());
    List<Reservation> reservations2 = reservationRepository.findByMemberUsername("m2");
    assertEquals(2,reservations2.size());
  }
}