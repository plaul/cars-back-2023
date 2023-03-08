package dat3.cars.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {
  @Id
  String username;
  String password;
  String email;
  String firstName;
  String lastName;
  String street;
  String city;
  String zip;
  boolean approved;
  int ranking;

  @CreationTimestamp
  LocalDateTime created;

  @UpdateTimestamp
  LocalDateTime lastEdited;

  public Member(String user, String password, String email,
                String firstName, String lastName, String street, String city, String zip) {
    this.username = user;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.zip = zip;
  }

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Reservation> reservations;

//  You can remove the following when we get to week2 if you like, they were only include to demonstrate
//  collections of basic type
  /*
  @ElementCollection
  List<String> favoriteCarColors = new ArrayList<>();

  @ElementCollection
  @MapKeyColumn(name = "description")
  @Column(name = "phone_number")
  Map<String,String> phones = new HashMap<>();
 */
}
