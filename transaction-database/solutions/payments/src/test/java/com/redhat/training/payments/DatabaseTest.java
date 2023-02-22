package com.redhat.training.payments;

import io.agroal.pool.DataSource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.RoutesBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

@QuarkusTest
class DatabaseTest {

    @Inject
    protected EntityManager entityManager;

    @BeforeEach
    void waitForCompletion() throws Exception {
        Thread.sleep(1000);
    }

    @Test
    void testAnalysisIsSetAsCompletedInDB() {
        List<PaymentAnalysis> paymentAnalysis = entityManager.createQuery("SELECT p FROM PaymentAnalysis p",
                                                                          PaymentAnalysis.class)
                                                             .getResultList();
        paymentAnalysis.forEach(each -> {
            Assertions.assertEquals("Completed", each.getStatus());
        });
    }

    @Test
    void testFraudScoreIsCorrectInDB() {
        Double totalScore = entityManager.createQuery("SELECT sum(p.score) FROM PaymentAnalysis p",
                                                      Double.class)
                                         .getSingleResult();
        Assertions.assertEquals(1.256, totalScore);
    }
}
