package com.teorerras.buynowdotcom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore // infinite loop since products call categories that call products that call categories....
    @OneToMany(mappedBy = "category") // cascade all maybe
    private List<Product> productList;

    public Category(String name) {
        this.name = name;
    }
}
