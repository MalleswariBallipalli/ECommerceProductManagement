package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.model.Customer;
import com.ecommerce.util.DBConnection;

public class CustomerDAO {

    // ================= ADD CUSTOMER =================

    public boolean addCustomer(Customer customer) {

        String sql = "INSERT INTO customers "
                + "(customer_name, email, phone, address) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    customer.getCustomerName()
            );

            statement.setString(
                    2,
                    customer.getEmail()
            );

            statement.setString(
                    3,
                    customer.getPhone()
            );

            statement.setString(
                    4,
                    customer.getAddress()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding customer: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // ================= GET CUSTOMER BY ID =================

    public Customer getCustomerById(int customerId) {

        String sql =
                "SELECT customer_id, customer_name, "
                + "email, phone, address "
                + "FROM customers "
                + "WHERE customer_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Customer(
                            resultSet.getInt(
                                    "customer_id"
                            ),

                            resultSet.getString(
                                    "customer_name"
                            ),

                            resultSet.getString(
                                    "email"
                            ),

                            resultSet.getString(
                                    "phone"
                            ),

                            resultSet.getString(
                                    "address"
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching customer: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // ================= VIEW ALL CUSTOMERS =================

    public List<Customer> getAllCustomers() {

        List<Customer> customers =
                new ArrayList<>();

        String sql =
                "SELECT customer_id, customer_name, "
                + "email, phone, address "
                + "FROM customers "
                + "ORDER BY customer_id";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getInt(
                                "customer_id"
                        ),

                        resultSet.getString(
                                "customer_name"
                        ),

                        resultSet.getString(
                                "email"
                        ),

                        resultSet.getString(
                                "phone"
                        ),

                        resultSet.getString(
                                "address"
                        )
                );

                customers.add(customer);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching customers: "
                    + e.getMessage()
            );
        }

        return customers;
    }
}