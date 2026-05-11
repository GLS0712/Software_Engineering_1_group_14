package dtu.softwareHuset;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import dtu.softwareHuset.app.Company;

public class WhiteBoxTest2 {

    private Company company;

    @Before
    public void setUp() {
        company = new Company();
    }

    // T1 — valid input, smallest valid value (lower boundary)
    @Test
    public void T1_minimumHoursAccepted() {
        company.validHourInput(0.5);
        // No exception means success
    }

    // T2 — valid input, largest valid value (upper boundary)
    @Test
    public void T2_maximumHoursAccepted() {
        company.validHourInput(24.0);
    }

    // T3 — valid input, typical mid-range value
    @Test
    public void T3_typicalHoursAccepted() {
        company.validHourInput(7.5);
    }

    // T4 — zero hours (boundary of "positive" rule)
    @Test
    public void T4_zeroHoursRejected() {
        try {
            company.validHourInput(0.0);
            fail("Expected IllegalArgumentException for zero hours");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours must be positive", e.getMessage());
        }
    }

    // T5 — negative hours
    @Test
    public void T5_negativeHoursRejected() {
        try {
            company.validHourInput(-1.0);
            fail("Expected IllegalArgumentException for negative hours");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours must be positive", e.getMessage());
        }
    }

    // T6 — just above the upper boundary
    @Test
    public void T6_excessiveHoursRejected() {
        try {
            company.validHourInput(24.5);
            fail("Expected IllegalArgumentException for hours above 24");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours cannot exceed 24", e.getMessage());
        }
    }

    // T7 — far above the upper boundary
    @Test
    public void T7_farExcessiveHoursRejected() {
        try {
            company.validHourInput(100.0);
            fail("Expected IllegalArgumentException for hours above 24");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours cannot exceed 24", e.getMessage());
        }
    }

    // T8 — non-half-hour increment (small fraction)
    @Test
    public void T8_quarterHourRejected() {
        try {
            company.validHourInput(2.25);
            fail("Expected IllegalArgumentException for non-half-hour value");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours must be in half-hour increments", e.getMessage());
        }
    }

    // T9 — non-half-hour increment (close to half)
    @Test
    public void T9_nearHalfHourRejected() {
        try {
            company.validHourInput(2.3);
            fail("Expected IllegalArgumentException for non-half-hour value");
        } catch (IllegalArgumentException e) {
            assertEquals("Hours must be in half-hour increments", e.getMessage());
        }
    }
}