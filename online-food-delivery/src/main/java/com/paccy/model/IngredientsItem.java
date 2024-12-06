package com.paccy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany//(mappedBy = "ingredientsItems")
    private List<Food> foods;

    private String name;

    @JsonIgnore
    @ManyToOne
   // @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    //@JoinColumn(name = "ingredients_category_id")
    private IngredientCategory category;

    private boolean inStoke = true;
}
