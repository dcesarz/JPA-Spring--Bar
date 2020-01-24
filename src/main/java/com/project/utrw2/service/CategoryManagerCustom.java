package com.project.utrw2.service;

import com.project.utrw2.domain.Category;

import java.util.List;

public interface CategoryManagerCustom {

    List<Category> findAllCategories();

    List<Category> findByNameCategory(String name);

    List<Category> findByDescCategory(String desc);

    List<Category> findByIsAlcoholicCategory(Boolean isAlcoholic);

}
