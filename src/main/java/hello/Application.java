package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(PersonRepository repository) {
        return (args) -> {
            repository.save(new Person("Jon","Snow"));
            repository.save(new Person("Daenerys", "Targaryen"));
            repository.save(new Person("Cersei","Lanister"));
            repository.save(new Person("Arya","Stark"));
            repository.save(new Person("Night","King"));

            log.info("findAll");
            log.info("-------");

            for(Person person : repository.findAll()) {
                log.info(person.toString());
            }

            log.info("");

            repository.findById(1L)
                    .ifPresent(
                            person -> {
                                log.info("Person with id 1 ");
                                log.info("-----------------");
                                log.info(person.toString());
                                log.info("");
                            }
                    );

            //search by last name
            log.info("Find all Targareyens");
            log.info("--------------------");
            repository.findByLastName("Targaryen")
                    .forEach(targareyns -> {
                        log.info(targareyns.toString());
                    });
            log.info("");

        };
    }
}
