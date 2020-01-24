package com.project.utrw2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drink {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;

    private String desc;

    private BigDecimal price;

    private Boolean isAvailable;
    @OneToMany(mappedBy = "drink")
    @Nullable
    private List<Drink_Categories> categories;

    public Drink(String name, String desc, BigDecimal price, Boolean isAvailable) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        if (categories != null) {
            List<Long> drinks_id_list = categories.stream()
                    .map(Drink_Categories::getId)
                    .collect(Collectors.toList());
            return "Drink{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", price=" + price +
                    ", isAvailable=" + isAvailable +
                    ", categories= " + drinks_id_list + "}";
        } else {
            return "Drink{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", price=" + price +
                    ", isAvailable=" + isAvailable +
                    ", categories= null}";
        }
    }
}
