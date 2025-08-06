package model;

import jakarta.persistence.*;
import lombok.*;
import model.enums.PRODUCT_STATUS;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type_id")
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private PRODUCT_STATUS status;
}
