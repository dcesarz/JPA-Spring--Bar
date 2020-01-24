package com.project.utrw2;

import com.project.utrw2.domain.Category;
import com.project.utrw2.domain.Drink;
import com.project.utrw2.domain.Drink_Categories;
import com.project.utrw2.service.CategoryManager;
import com.project.utrw2.service.DrinkCategoriesManager;
import com.project.utrw2.service.DrinkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PersistenceImplementation {

    private CategoryManager cm;
    private DrinkManager dm;
    private DrinkCategoriesManager dcm;

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

    @EventListener(ApplicationReadyEvent.class)
    public void methods_on_app_ready() {
        cm.save(new Category("name1", "desc1", false));
        dm.save(new Drink("name2", "desc2", new BigDecimal(12.12), true));
        cm.save(new Category("name3", "desc3", true));
        cm.save(new Category("new_cat", "desc4", false));
        dcm.save(new Drink_Categories(dm.findByName("name2"), cm.findByName("new_cat")));
        dm.save(new Drink("name4", "desc5", new BigDecimal("21.21"), true));
        cm.save(new Category("name4", "desc5", true));
        System.out.println(cm.findAll());
        System.out.println(dm.findAll());
        System.out.println(dcm.findAll());
    }
}
