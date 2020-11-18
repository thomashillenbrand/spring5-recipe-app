package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByUomTeaspoon() {
        Optional<UnitOfMeasure> teaspoonOptional = unitOfMeasureRepository.findByUom("Teaspoon");
        assertEquals("Teaspoon", teaspoonOptional.get().getUom());

    }
    @Test
    void findByUomCup() {
        Optional<UnitOfMeasure> cupOptional = unitOfMeasureRepository.findByUom("Cup");
        assertEquals("Cup", cupOptional.get().getUom());

    }
}