package com.project.utrw2.service;

import com.project.utrw2.domain.Category;
import com.project.utrw2.domain.Drink;
import com.project.utrw2.domain.Drink_Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryManagerImplTest {

    private CategoryManager cm;
    private DrinkManager dm;
    private DrinkCategoriesManager dcm;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setCm(CategoryManager cm) {
        this.cm = cm;
    }

    @Autowired
    public void setDm(DrinkManager dm) {
        this.dm = dm;
    }

    @Autowired
    public void setDcm(DrinkCategoriesManager dcm) {
        this.dcm = dcm;
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    @BeforeEach
    void setUp() {
        cm.save(new Category("name1", "desc1", false));
        cm.save(new Category("name3", "desc3", true));
        cm.save(new Category("new_cat", "desc4", false));
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(cm).isNotNull();
        assertThat(dm).isNotNull();
        assertThat(dcm).isNotNull();
    }

    @Test
    void refresh() {
        //TODO
    }

    @Test
    void findAllCategories() {
        List<Category> result = cm.findAllCategories();
        assertThat(result.size() == 3);
    }


    @Test
    void findByNameCategory() {
        List<Category> result = cm.findByNameCategory("name3");
        assertThat(result.size() == 1);
        assertThat(result.get(0).getDesc().equals("desc3"));
    }


    @Test
    void findByDescCategory() {
        List<Category> result = cm.findByDescCategory("desc3");
        assertThat(result.size() == 1);
        assertThat(result.get(0).getName().equals("name3"));
    }

    @Test
    void findByIsAlcoholicCategory() {
        List<Category> result = cm.findByIsAlcoholicCategory(false);
        assertThat(result.size() == 2);
    }

    @Test
    void findAllCategoriesWhoseNameStartsWith() {
        List<Category> result = cm.findAllCategoriesWhoseNameStartsWith("name");
        assertThat(result.size() == 2);
        assertThat(result.get(0).getName().equals("name1"));
    }

    @Test
    void findAllCategoriesWithNoDrinksAssigned() {
        List<Category> result = cm.findAllCategoriesWithNoDrinksAssigned();
        assertThat(result.size() == 3);
        Category c = cm.findByName("name1");
        Drink d = new Drink("name2", "desc2", new BigDecimal(12.12), true);
        dm.save(d);
        dcm.save(new Drink_Categories(d, c));
        result = cm.findAllCategoriesWithNoDrinksAssigned();
        assertThat(result.size() == 2);
    }

    @Test
    void findAllCategoriesWithEvenNumbersId() {
        cm.save(new Category("name5", "desc5", true));
        List<Category> result = cm.findAllCategoriesWithEvenNumbersId();
        for (Category c : result) {
            assertThat(c.getId() % 2 == 0);
        }
    }
}