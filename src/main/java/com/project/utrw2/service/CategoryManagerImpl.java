package com.project.utrw2.service;

import com.project.utrw2.domain.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CategoryManagerImpl implements CategoryManagerCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Category> findAllCategories() {
        TypedQuery<Category> query = em.createNamedQuery("category.findAll", Category.class);
        return query.getResultList();
        //return results;
    }

    @Override
    public List<Category> findByNameCategory(String name) {
        TypedQuery<Category> query = em.createNamedQuery("category.findByName", Category.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Category> findByDescCategory(String desc) {
        TypedQuery<Category> query = em.createNamedQuery("category.findByDesc", Category.class);
        query.setParameter("desc", desc);
        return query.getResultList();
    }

    @Override
    public List<Category> findByIsAlcoholicCategory(Boolean isAlcoholic) {
        TypedQuery<Category> query = em.createNamedQuery("category.findByIs_Alcoholic", Category.class);
        query.setParameter("is_alcoholic", isAlcoholic);
        return query.getResultList();
    }


}
