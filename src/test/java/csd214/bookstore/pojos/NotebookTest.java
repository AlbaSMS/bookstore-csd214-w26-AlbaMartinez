package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotebookTest {
    @Test
    void testEquality() {

        Notebook n1 = new Notebook("Labon", 200);
        Notebook n2 = new Notebook("Labon", 200);
        Notebook n3 = new Notebook("Labon", 100);

        assertEquals(n1, n2, "Notebooks with same state should be equal");
        assertEquals(n1.hashCode(), n2.hashCode(), "HashCodes must match");
        assertNotEquals(n1, n3, "Different page count notebooks should not be equal");
    }
}
