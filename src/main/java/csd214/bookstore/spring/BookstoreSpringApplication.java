package csd214.bookstore.spring;

import com.github.javafaker.Faker;
import csd214.bookstore.jpa.entities.*;
import csd214.bookstore.spring.repositories.SpringProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@ComponentScan(basePackages = "csd214.bookstore.spring")
@EntityScan(basePackages = "csd214.bookstore.jpa.entities")
@EnableJpaRepositories(basePackages = "csd214.bookstore.spring.repositories")
public class BookstoreSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreSpringApplication.class, args);
        System.out.println("Spring Boot Bookstore is running!");
        System.out.println("View data at: http://localhost:8080/products");
    }

    /**
     * This bean runs on startup.
     * It uses JavaFaker to create a "Bunch" of random products.
     */
    @Bean
    CommandLineRunner seedDatabase(SpringProductRepository repository) {
        return args -> {
            // Check if DB is already full to avoid duplicates on restart
            if (repository.count() > 0) {
                System.out.println("Database already seeded. Skipping generation.");
                return;
            }

            System.out.println("Seeding database with 50 random items...");
            Faker faker = new Faker();

            for (int i = 0; i < 50; i++) {
                int randomType = faker.number().numberBetween(1, 5); // 1 to 4

                switch (randomType) {
                    case 1: // BOOK
                        BookEntity b = new BookEntity(
                                faker.book().title(),
                                faker.number().randomDouble(2, 10, 100),
                                faker.number().numberBetween(1, 50),
                                faker.book().author()
                        );
                        repository.save(b);
                        break;

                    case 2: // MAGAZINE
                        MagazineEntity m = new MagazineEntity(
                                faker.company().name() + " Weekly",
                                faker.number().randomDouble(2, 5, 15),
                                faker.number().numberBetween(10, 100),
                                faker.number().numberBetween(100, 1000),
                                faker.date().past(30, TimeUnit.DAYS)
                        );
                        repository.save(m);
                        break;

                    case 3: // DISCMAG
                        DiscMagEntity dm = new DiscMagEntity(
                                "PC Gamer: " + faker.esports().game(),
                                faker.number().randomDouble(2, 15, 25),
                                faker.number().numberBetween(5, 20),
                                faker.number().numberBetween(50, 200),
                                faker.date().past(60, TimeUnit.DAYS),
                                true // Always has disc
                        );
                        repository.save(dm);
                        break;

                    case 4: // TICKET
                        TicketEntity t = new TicketEntity(
                                "Concert: " + faker.rockBand().name() + " Live",
                                faker.number().randomDouble(2, 50, 300)
                        );
                        repository.save(t);
                        break;
                }
            }
            System.out.println("Seeding complete! 50 items added.");
        };
    }
}