package com.ecommerce.test;

import com.ecommerce.dao.OrderDetailsDAO;

public class OrderDetailsDAOTest {

    public static void main(String[] args) {

        OrderDetailsDAO orderDetailsDAO =
                new OrderDetailsDAO();

        orderDetailsDAO.displayOrderDetails(2);
    }
}