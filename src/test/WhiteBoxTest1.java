package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Company;
import Employee;

public class WhiteBoxTest1 {
    Company company = new Company();

    @Test (expected = IllegalAccessError.class)
    public void testInputDataSetA(){
        this.company.setInitailsForEmployee(new Employee("John Doe"), "jodo");
    }
    @Test (expected = IllegalAccessError.class)
    public void testInputDataSetB(){
        Employee employee1 = new Employee("Hubert Baumeister");
        this.company.hireEmployee(employee1);
        this.company.setInitailsForEmployee(employee1, "huba");
        Employee employee2 = new Employee("John Doe");
        this.company.hireEmployee(employee2);
        this.company.setInitailsForEmployee(employee2, "huba");
    }

    @Test
    public void testInputDataSetC(){
        Employee employee2 = new Employee("John Doe");
        this.company.hireEmployee(employee2);
        this.company.setInitailsForEmployee(employee2, "jodo");
        assertEquals("jodo", employee2.getInitials());
    }

}
