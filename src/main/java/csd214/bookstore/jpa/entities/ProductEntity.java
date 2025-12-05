package csd214.bookstore.jpa.entities;
import jakarta.persistence.*;
import csd214.bookstore.pojos.SaleableItem;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity implements Serializable, SaleableItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Override public String toString() { return "ProductEntity{id=" + id + "}"; }
}
