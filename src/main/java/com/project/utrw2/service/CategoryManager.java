package com.project.utrw2.service;

import com.project.utrw2.domain.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryManager extends CrudRepository<Category, Long>, CategoryManagerCustom {

    @Transactional(rollbackFor = Exception.class)
    <S extends Category> S save(S category);

    Category findByName(String name);

    Category findByNameIgnoreCaseContaining(String name);

    @Transactional(rollbackFor = Exception.class)
    void delete(Category category);

    Category findById(long id);

    Category getOneById(long id);

    List<Category> findByNameEndingWith(String name);

    List<Category> findByNameStartingWith(String name);

    List<Category> findAll();

    void deleteAll();

    @Query(value = "SELECT c FROM Category c WHERE c.name LIKE :pattern% ORDER BY c.name")
    List<Category> findAllCategoriesWhoseNameStartsWith(@Param("pattern") String pattern);

    @Query(value = "SELECT c FROM Category c WHERE c.drinks IS EMPTY")
    List<Category> findAllCategoriesWithNoDrinksAssigned();

    @Query(value = "SELECT c FROM Category c WHERE MOD(c.id,2)=0")
    List<Category> findAllCategoriesWithEvenNumbersId();
}
