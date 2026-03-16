package dtu.library.app;
import java.util.ArrayList;

import dtu.library.app.Customer;
import io.cucumber.java.bs.A;

public class MarriageAgency {
    private ArrayList<Customer> customers;


    public MarriageAgency(){
        this.customers = new ArrayList<Customer>();
        customers.add(new Customer("A", 30));
        customers.add(new Customer("B", 25));
        customers.add(new Customer("C", 41));
    }


    public boolean findMatch(Customer c1){
        for (Customer customer: customers){
            if (Math.abs(c1.getAge() - customer.getAge()) < 10)
                System.out.println(c1.getAge() - customer.getAge());
                return true;
        }
        return false;
    }

    public Boolean customerExist(int i){
        for (Customer customer: customers){
            if (customer.getAge() == i)
                return true;
        }
        return false;
    }

    public Customer currentCustomer(int i){
        for (Customer customer: customers){
            if (customer.getAge() == i)
                return customer;
        }
        return null;
    }
}
