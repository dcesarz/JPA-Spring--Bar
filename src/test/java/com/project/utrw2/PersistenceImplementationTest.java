package com.project.utrw2;

import com.project.utrw2.domain.Category;
import com.project.utrw2.domain.Drink;
import com.project.utrw2.domain.Drink_Categories;
import com.project.utrw2.service.CategoryManager;
import com.project.utrw2.service.DrinkCategoriesManager;
import com.project.utrw2.service.DrinkManager;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PersistenceImplementationTest {


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

   // resolving dependencies in test ; not needed outside of them as spring does it for ourselves.
    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    @BeforeEach
    void setUp() {
        if (dcm.findAll() != null) {
            dcm.deleteAll();
        }
        if (cm.findAll() != null) {
            cm.deleteAll();
        }
        if (dm.findAll() != null) {
            dm.deleteAll();
        }
        cm.save(new Category("name1", "desc1", false));
        dm.save(new Drink("name2", "desc2", new BigDecimal(12.12), true));
        cm.save(new Category("name3", "desc3", true));
        cm.save(new Category("new_cat", "desc4", false));
        dcm.save(new Drink_Categories(dm.findByName("name2"), cm.findByName("new_cat")));
        dm.save(new Drink("name4", "desc5", new BigDecimal(21.21), true));
        cm.save(new Category("name4", "desc5", true));

    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(cm).isNotNull();
        assertThat(dm).isNotNull();
        assertThat(dcm).isNotNull();
    }

    @Test
    void readCategoriesWorks() {
        assertThat(cm.findAll().size() == 4);
    }

    @Test
    void readDrinksWorks() {
        assertThat(dm.findAll().size() == 2);
    }

    @Test
    void readDrink_CategoriesWorks() {
        assertThat(dcm.findAll().size() == 1);
    }

    @Test
    void insertCategoryWorks() {
        cm.save(new Category("new_name", "new_desc", false));
        assertThat(cm.findByName("new_name") != null);
    }

    @Test
    void insertDrinkWorks() {
        dm.save(new Drink("new_name", "new_desc", new BigDecimal("123.34"), false));
        assertThat(dm.findByName("new_name") != null);
    }

    @Test
    void insertDrink_Categoriesworks() {
        dm.save(new Drink("name5", "desc5", new BigDecimal("21.21"), true));
        cm.save(new Category("name6", "desc6", true));
        Drink d = dm.findByName("name5");
        Category c = cm.findByName("name6");
        Drink_Categories dc = new Drink_Categories(d, c);
        dcm.save(dc);
        assertThat(dcm.findAllByCategory(c) != null);
        assertThat(!dcm.findAllByCategory(c).isEmpty());
    }

    @Test
    void resolvingDependenciesworks() {

        dm.save(new Drink("name5", "desc5", new BigDecimal("21.21"), true));
        cm.save(new Category("name6", "desc6", true));
        Drink d = dm.findByName("name5");
        Category c = cm.findByName("name6");
        Drink_Categories dc = new Drink_Categories(d, c);
        dcm.save(dc);
        flushAndClear();
        c = cm.findByName("name6");
        d = dm.findByName("name5");
        assertThat(c.getDrinks() != null);
        assertThat(d.getCategories() != null);
        assertThat(c.getDrinks().contains(dc));
        assertThat(d.getCategories().contains(dc));
    }

    @Test
    void updateCategoryWorks() {
        Category c = cm.findByName("name1");
        c.setDesc("UPDATED");
        c.setIs_alcoholic(!c.getIs_alcoholic());
        cm.save(c);
        c = cm.findByName("name1");
        assertThat(c.getIs_alcoholic());
        assertThat(c.getDesc().equals("UPDATED"));
    }

    @Test
    void updateDrinkWorks() {
        Drink d = dm.findByName("name2");
        d.setDesc("UPDATED");
        d.setIsAvailable(!d.getIsAvailable());
        dm.save(d);
        d = dm.findByName("name2");
        assertThat(!d.getIsAvailable());
        assertThat(d.getDesc().equals("UPDATED"));
    }

    @Test
    void updateDrinkCategoryWorks() {
        Drink_Categories dc = dcm.findAllByCategory(cm.findByName("new_cat")).get(0);
        dc.setDrink(dm.findByName("name4"));
        dc.setCategory(cm.findByName("name4"));
        dcm.save(dc);
        dc = dcm.findAllByCategory(cm.findByName("name4")).get(0);
        assertThat(dc != null);
        dc = dcm.findAllByDrink(dm.findByName("name4")).get(0);
        assertThat(dc != null);
    }

    @Test
    void deleteCategoriesWorks() {
        cm.delete(cm.findByName("name1"));
        assertThat(cm.findAll().size() == 3);
    }

    @Test
    void deleteDrinksWorks() {
        dm.delete(dm.findByName("name4"));
        assertThat(dm.findAll().size() == 1);
    }

    @Test
    void deleteDrink_CategoriesWorks() {
        dcm.delete(dcm.findAllByCategory(cm.findByName("new_cat")).get(0));
        assertThat(dcm.findAll().size() == 0);
    }

    @Test
    @Commit
    void constraintCategoryWorks() {
        dcm.delete(dcm.findAllByCategory(cm.findByName("new_cat")).get(0));
        cm.delete(cm.findByName("new_cat"));
    }

    @Test
    @Commit
    void constraintDrinkWorks() {
        dcm.delete(dcm.findAllByCategory(cm.findByName("new_cat")).get(0));
        dm.delete(dm.findByName("name2"));
    }

    @Test
    void transactionTest() {
        Drink dtest1 = new Drink("named1", "descd1", new BigDecimal(10.10), true);
        Drink dtest2 = new Drink("named2", "descd2", new BigDecimal(9.10), true);


        Category ctest = new Category("namec1", "desc1", true);

        Drink_Categories dctest1 = new Drink_Categories(dtest1, ctest);
        Drink_Categories dctest2 = new Drink_Categories(dtest2, ctest);

        dm.save(dtest1);
        dm.save(dtest2);

        cm.save(ctest);

        dcm.save(dctest1);
        dcm.save(dctest2);

        dcm.deleteAll();

        flushAndClear();

        List<Drink> expected = dm.findAll();
        expected.remove(dm.findByName("named2"));

        dm.deleteDrinksBelowPrice(new BigDecimal(10));

        List<Drink> actual = dm.findAll();

        MatcherAssert.assertThat(actual, is(expected));

    }

    @Test
    void findDrinkByNameIgnoreCaseContainingWorks() {
        dm.save(new Drink("Name_Test", "description", new BigDecimal(11.12), false));
        assertThat(dm.findByName("name_test") == null);
        assertThat(dm.findByNameIgnoreCaseContaining("name_test") != null);
    }

    @Test
    void findCategoryByNameIgnoreCaseContainingWorks() {
        cm.save(new Category("Name_Test", "description", false));
        assertThat(cm.findByName("name_test") == null);
        assertThat(cm.findByNameIgnoreCaseContaining("name_test") != null);
    }

    @Test
    void findCategoryByNameEndingWith() {
        List<Category> expected = new ArrayList<>();
        expected.add(cm.findByName("name1"));
        List<Category> actual = cm.findByNameEndingWith("1");
        MatcherAssert.assertThat(actual, is(expected));
    }

    @Test
    void findCategoryByNameStartingWIth() {
        List<Category> expected = new ArrayList<>();
        expected.add(cm.findByName("name1"));
        expected.add(cm.findByName("name3"));
        expected.add(cm.findByName("name4"));
        List<Category> actual = cm.findByNameStartingWith("name");
        MatcherAssert.assertThat(actual, is(expected));
    }


}