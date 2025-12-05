package csd214.bookstore.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("MAGAZINE")
public class MagazineEntity extends PublicationEntity {

    private int orderQty;

    @Temporal(TemporalType.DATE)
    private Date currentIssue;

    public MagazineEntity() {}

    public MagazineEntity(String title, double price, int copies, int orderQty, Date currentIssue) {
        super(title, price, copies);
        this.orderQty = orderQty;
        this.currentIssue = currentIssue;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public Date getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(Date currentIssue) {
        this.currentIssue = currentIssue;
    }

    @Override
    public String toString() {
        return "MagazineEntity{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", issue=" + currentIssue +
                '}';
    }
}
