package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class DataLoaderMySQL implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoaderMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Enter DataLoaderMySQL");

        if(unitOfMeasureRepository.count() == 0L) {
            log.debug("No UnitOfMeasures found, loading in bootstrap data");
            loadUoms();
        }

        if(categoryRepository.count() == 0L) {
            log.debug("No Categories found, loading in bootstrap data");
            loadCategories();
        }

        log.debug("Exit DataLoaderMySQL");
    }

    private void loadCategories() {
        log.debug("Enter loadCategories()");

        Category cat1 = new Category();
        cat1.setDescription("Mexican");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription("American");
        categoryRepository.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription("Fast Food");
        categoryRepository.save(cat3);

        log.debug("Exit loadCategories()");
    }

    private void loadUoms() {
        log.debug("Enter loadUoms()");

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setUom("Pint");
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setUom("Teaspoon");
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setUom("Tablespoon");
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setUom("Cup");
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setUom("Pinch");
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setUom("Ounce");
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setUom("Dash");
        unitOfMeasureRepository.save(uom7);

        log.debug("Exit loadUoms()");
    }
}
