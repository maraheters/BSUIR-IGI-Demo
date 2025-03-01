package com.sparkplug.sparkplugbackend.posting.query;

import com.sparkplug.sparkplugbackend.posting.model.Car;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.posting.repository.PostingsRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test-containers")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JPASpecificationIntegrationTest {

    @Autowired
    private PostingsRepository repository;

    private Posting posting_1;
    private Posting posting_2;

    private UUID postingUuid_1 = UUID.randomUUID();
    private UUID postingUuid_2 = UUID.randomUUID();

    @BeforeEach
    @Transactional
    public void setUp() {
        posting_1 = new Posting();
        posting_1.setCreationDate(LocalDateTime.now());
        posting_1.setPrice(BigDecimal.valueOf(24500));
        posting_1.setDescription("ABCDEF");
        var car_1 = new Car();
        car_1.setColor("Yellow");
        car_1.setModel("Ford");
        posting_1.setCar(car_1);
        postingUuid_1 = repository.saveAndFlush(posting_1).getId();

        posting_2 = new Posting();
        posting_2.setCreationDate(LocalDateTime.now());
        posting_2.setPrice(BigDecimal.valueOf(43200));
        posting_2.setDescription("GHKLMNOP");
        postingUuid_2 = repository.saveAndFlush(posting_2).getId();
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(repository);
    }

    @Test
    public void givenUUID_whenGettingList_thenCorrect() {
        PostingSpecification spec =
                new PostingSpecification(new SearchCriteria("id", ":", postingUuid_1));

        List<Posting> results = repository.findAll(spec);

        Assertions.assertFalse(results.isEmpty());
    }

    @Test
    public void givenDate_whenGettingList_thenCorrect() {
        PostingSpecification spec =
                new PostingSpecification(new SearchCriteria("creationDate", ":", posting_1.getCreationDate()));

        List<Posting> results = repository.findAll(spec);

        Assertions.assertFalse(results.isEmpty());
    }
    @Test
    public void givenIncorrectUUID_whenGettingList_thenCorrect() {
        PostingSpecification spec =
                new PostingSpecification(new SearchCriteria("id", ":", UUID.randomUUID()));

        List<Posting> results = repository.findAll(spec);

        Assertions.assertTrue(results.isEmpty());
    }
    @Test
    public void givenPrice_whenGettingList_thenCorrect() {
        PostingSpecification spec =
                new PostingSpecification(new SearchCriteria("price", ">", "30000"));

        List<Posting> results = repository.findAll(spec);

        Assertions.assertEquals(1, results.size());
    }
}
