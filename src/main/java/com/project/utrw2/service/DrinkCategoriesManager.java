package com.project.utrw2.service;

import com.project.utrw2.domain.Category;
import com.project.utrw2.domain.Drink;
import com.project.utrw2.domain.Drink_Categories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DrinkCategoriesManager extends CrudRepository<Drink_Categories, Long> {

    @Transactional(rollbackFor = Exception.class)
    <S extends Drink_Categories> S save(S drink_categories);

    @Transactional(rollbackFor = Exception.class)
    void delete(Drink_Categories drink);

    Drink_Categories findById(long id);

    Drink_Categories getOneById(long id);

    List<Drink_Categories> findAll();

    List<Drink_Categories> findAllByCategory(Category c);

    List<Drink_Categories> findAllByDrink(Drink d);

    void deleteAll();
}
