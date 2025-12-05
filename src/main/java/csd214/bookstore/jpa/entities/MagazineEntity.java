package csd214.bookstore.jpa.entities;
import jakarta.persistence.*;
import java.util.Date;
@Entity @DiscriminatorValue("MAGAZINE")
public class MagazineEntity extends PublicationEntity {
    private int orderQty;
    @Temporal(TemporalType.DATE) private Date currentIssue;
    public MagazineEntity() {}
    public MagazineEntity(String t, double p, int c, int o, Date d) { super(t, p, c); this.orderQty = o; this.currentIssue = d; }
    public int getOrderQty() { return orderQty; }
    public void setOrderQty(int o) { this.orderQty = o; }
    public Date getCurrentIssue() { return currentIssue; }
    public void setCurrentIssue(Date d) { this.currentIssue = d; }
    @Override public String toString() { return "Mag{issue=" + currentIssue + ", " + super.toString() + "}"; }
}
