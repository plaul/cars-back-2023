package dat3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication( exclude = {SecurityAutoConfiguration.class} )
public class CarsApplication {
  public static void main(String[] args) {
    SpringApplication.run(CarsApplication.class, args);
  }
}
