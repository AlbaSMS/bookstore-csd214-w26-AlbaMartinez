package csd214.bookstore.jpa.entities;
import jakarta.persistence.*;
import java.util.Date;
@Entity @DiscriminatorValue("DISCMAG")
public class DiscMagEntity extends MagazineEntity {
    private boolean hasDisc;
    public DiscMagEntity() {}
    public DiscMagEntity(String t, double p, int c, int o, Date d, boolean h) { super(t, p, c, o, d); this.hasDisc = h; }
    public boolean isHasDisc() { return hasDisc; }
    public void setHasDisc(boolean h) { this.hasDisc = h; }
    @Override public String toString() { return "DiscMag{disc=" + hasDisc + ", " + super.toString() + "}"; }
}
