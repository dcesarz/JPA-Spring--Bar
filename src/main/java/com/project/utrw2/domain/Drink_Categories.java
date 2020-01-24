package com.project.utrw2.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Drink_Categories {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Drink_Categories(Drink drink, Category category) {
        this.drink = drink;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Drink_Categories{" +
                "id=" + id +
                ", drink id=" + drink.getId() +
                ", category id=" + category.getId() +
                '}';
    }
}
