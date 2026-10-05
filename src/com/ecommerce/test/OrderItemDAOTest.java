package com.ecommerce.test;

import com.ecommerce.dao.OrderItemDAO;
import com.ecommerce.model.OrderItem;

public class OrderItemDAOTest {

    public static void main(String[] args) {

        OrderItemDAO orderItemDAO = new OrderItemDAO();

        OrderItem item = new OrderItem(
                1,
                1,
                1,
                55000.00
        );

        boolean result =
                orderItemDAO.addOrderItem(item);

        if (result) {

            System.out.println(
                    "Order item added successfully!"
            );

        } else {

            System.out.println(
                    "Order item addition failed!"
            );
        }
    }
}