package com.project.utrw2.service;

import com.project.utrw2.domain.Drink;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DrinkManager extends CrudRepository<Drink, Long> {

    <S extends Drink> S save(S drink);

    Drink findByName(String name);

    Drink findByNameIgnoreCaseContaining(String name);

    void delete(Drink drink);

    Drink findById(long id);

    Drink getOneById(long id);

    List<Drink> findAll();

    void deleteAll();

    @Modifying
    @Transactional
    @Query("delete from Drink d where d.price < ?1")
    void deleteDrinksBelowPrice(BigDecimal price);

}
