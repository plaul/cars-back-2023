package dat3.cars.repositories;

import dat3.cars.entity.Car;
import dat3.cars.entity.Member;
import dat3.cars.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {
  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CarRepository carRepository;

  @Autowired
  ReservationRepository reservationRepository;

  boolean dataIsInitialized = false;

  @BeforeEach
  void setUp() {
    if(dataIsInitialized)
      return;
    Car car = Car.builder().brand("Volvo").model("V70").pricePrDay(500).bestDiscount(10).build();
    carRepository.saveAndFlush(car);
    Member m1 = new Member("m1", "pw", "m1@a.dk", "bb", "jjj", "xx 34", "ly", "2800");
    Member m2 = new Member("m2", "pw", "m2@a.dk", "aa", "lll", "xx 31", "ly", "2800");
    memberRepository.saveAndFlush(m1);//Flush ensures date fields are set
    memberRepository.saveAndFlush(m2);
    reservationRepository.saveAndFlush(new Reservation(m1,car, LocalDate.of(2028,9,10)));
    reservationRepository.saveAndFlush(new Reservation(m1,car, LocalDate.of(2028,9,11)));
    dataIsInitialized = true;
  }

  @Test
  void existsByEmail() {
    assertTrue(memberRepository.existsByEmail("m1@a.dk"));
  }
  @Test
  void existsByEmailNotFound() {
    assertFalse(memberRepository.existsByEmail("xx@a.dk"));
  }

  @Test
  void findDistinctMemberByReservationsIsNotNull() {
    List<Member> membersWithReservations = memberRepository.findDistinctMemberByReservationsIsNotNull();
    assertTrue(membersWithReservations.size()==1);
    assertEquals("m1",membersWithReservations.get(0).getUsername());
  }

  @Test
  void testFindReservationByUsername(){
    assertEquals(2,reservationRepository.countReservationsByMemberUsername("m1"));
    assertEquals(0,reservationRepository.countReservationsByMemberUsername("m2"));
  }
}