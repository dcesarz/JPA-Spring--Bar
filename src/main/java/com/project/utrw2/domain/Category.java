package com.project.utrw2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries(
        {
                @NamedQuery(name = "category.findAll", query = "select c from Category c"),
                @NamedQuery(name = "category.findByName", query = "select c from Category c where c.name = :name"),
                @NamedQuery(name = "category.findByDesc", query = "select c from Category c where c.desc = :desc"),
                @NamedQuery(name = "category.findByIs_Alcoholic", query = "select c from Category c where c.is_alcoholic = :is_alcoholic")
        }
)
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;

    private String desc;

    private Boolean is_alcoholic;
    @OneToMany(mappedBy = "category")
    @Nullable
    private List<Drink_Categories> drinks;

    public Category(String name, String desc, Boolean isAlcoholic) {
        this.name = name;
        this.desc = desc;
        this.is_alcoholic = isAlcoholic;
    }

    @Override
    public String toString() {
        if (drinks != null) {
            List<Long> drinks_id_list = drinks.stream()
                    .map(Drink_Categories::getId)
                    .collect(Collectors.toList());
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", is_alcoholic=" + is_alcoholic +
                    ", drinks=" + drinks_id_list +
                    '}';
        } else {
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", is_alcoholic=" + is_alcoholic +
                    ", drinks = null }";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId() == category.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
