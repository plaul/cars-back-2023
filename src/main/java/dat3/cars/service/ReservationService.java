package dat3.cars.service;

import dat3.cars.dto.ReservationRequest;
import dat3.cars.dto.ReservationResponse;
import dat3.cars.entity.Car;
import dat3.cars.entity.Member;
import dat3.cars.entity.Reservation;
import dat3.cars.repositories.CarRepository;
import dat3.cars.repositories.MemberRepository;
import dat3.cars.repositories.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

  ReservationRepository reservationRepository;
  MemberRepository memberRepository;
  CarRepository carRepository;

  public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, CarRepository carRepository) {
    this.reservationRepository = reservationRepository;
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
  }

  public ReservationResponse makeReservation(ReservationRequest body) {
    if (reservationRepository.existsByCarIdAndRentalDate(body.getCarId(), body.getDateToReserveCar())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is already reserved on this day");
    }
    Car car = carRepository.findById(body.getCarId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car with provide id not found"));
    Member member = memberRepository.findById(body.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member with provided username not found"));

    // Check if the reservation date is in the future
    if (body.getDateToReserveCar().isBefore(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation date cannot be a date in the past");
    }
    Reservation reservation = new Reservation(member, car, body.getDateToReserveCar());
    reservation = reservationRepository.save(reservation);
    return new ReservationResponse(reservation);
  }

  public List<ReservationResponse> getReservationsForUser(String username) {
    Member member = memberRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    List<ReservationResponse> reservations = member.getReservations().stream().map(r -> new ReservationResponse(r)).toList();
    return reservations;
  }
}

