package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

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

            log.info("Now access data from H2");
            log.info("-----------------------");


            log.info("");
            log.info("Create a table");

            jdbcTemplate.execute("DROP TABLE CUSTOMER IF EXISTS ");
            jdbcTemplate.execute("CREATE TABLE CUSTOMER(" +
                    "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

            log.info("");

            // Batch update works only on objects
            List<Object[]> names = Arrays.asList("Jon Snow","Daenerys Targaryen","Cersei Lanister", "Arya Stark", "Night King")
                    .stream()
                    .map(name -> name.split("\\s"))
                    .collect(Collectors.toList());

            log.info("Insert into table");
            log.info("-----------------");

            jdbcTemplate.batchUpdate("INSERT INTO CUSTOMER(first_name, last_name) VALUES(?,?)", names);

            log.info("Get all records");
            log.info("---------------");

            jdbcTemplate.query(
                    "SELECT id, first_name, last_name FROM customer ", new Object[] { },
                    (rs, rowNum) -> new Customer( rs.getString("first_name"), rs.getString("last_name"))
            ).forEach(customer -> log.info(customer.toString()));




        };
    }
}
