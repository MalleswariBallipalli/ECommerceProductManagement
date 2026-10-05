package com.ecommerce.test;

import com.ecommerce.dao.CustomerDAO;
import com.ecommerce.model.Customer;

public class CustomerDAOTest {

    public static void main(String[] args) {

        CustomerDAO customerDAO = new CustomerDAO();

        Customer customer = new Customer(
                "Arjun Kumar",
                "arjun@gmail.com",
                "9876543210",
                "Hyderabad, Telangana"
        );

        boolean result = customerDAO.addCustomer(customer);

        if (result) {
            System.out.println(
                    "Customer added successfully!"
            );
        } else {
            System.out.println(
                    "Customer addition failed!"
            );
        }
    }
}