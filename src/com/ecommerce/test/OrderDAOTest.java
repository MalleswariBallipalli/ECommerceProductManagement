package com.ecommerce.test;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.model.Order;

public class OrderDAOTest {

    public static void main(String[] args) {

        OrderDAO orderDAO = new OrderDAO();

        Order order = new Order(
                1,
                55000.00,
                "PLACED"
        );

        int orderId = orderDAO.addOrder(order);

        if (orderId > 0) {

            System.out.println(
                    "Order placed successfully!"
            );

            System.out.println(
                    "Generated Order ID: " + orderId
            );

        } else {

            System.out.println(
                    "Order placement failed!"
            );
        }
    }
}