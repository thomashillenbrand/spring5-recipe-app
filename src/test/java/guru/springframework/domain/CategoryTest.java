package guru.springframework.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    Category category;

    @BeforeEach // runs before each test method
    public void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        Long idVal = 4L;
        category.setId(idVal);
        assertEquals(idVal, category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}