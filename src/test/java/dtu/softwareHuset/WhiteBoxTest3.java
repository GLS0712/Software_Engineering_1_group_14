package dtu.softwareHuset;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

import dtu.softwareHuset.app.Company;

public class WhiteBoxTest3 {

    private Company company;

    @Before
    public void setUp() {
        company = new Company();
    }

    // T1 — null date: first branch taken, NullPointerException guard fires
    @Test
    public void T1_nullDateRejected() {
        try {
            company.validDateInput(null);
            fail("Expected IllegalArgumentException for null date");
        } catch (IllegalArgumentException e) {
            assertEquals("Date cannot be null", e.getMessage());
        }
    }

    // T2 — future date: first branch false, second branch true
    @Test
    public void T2_futureDateRejected() {
        try {
            company.validDateInput(LocalDate.now().plusDays(1));
            fail("Expected IllegalArgumentException for future date");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot log time on a future date", e.getMessage());
        }
    }

    // T3 — exactly today: boundary where isAfter() returns false
    @Test
    public void T3_todayAccepted() {
        company.validDateInput(LocalDate.now()); // no exception = pass
    }

    // T4 — past date: both branches false, nominal valid path
    @Test
    public void T4_pastDateAccepted() {
        company.validDateInput(LocalDate.now().minusDays(1)); // no exception = pass
    }
}
