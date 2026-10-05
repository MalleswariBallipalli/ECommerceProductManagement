package com.ecommerce.test;

import com.ecommerce.dao.ProductDAO;

public class ProductStockTest {

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO();

        int productId = 1;
        int quantity = 2;

        boolean available =
                productDAO.isStockAvailable(
                        productId,
                        quantity
                );

        if (available) {

            boolean reduced =
                    productDAO.reduceStock(
                            productId,
                            quantity
                    );

            if (reduced) {

                System.out.println(
                        "Stock reduced successfully!"
                );

            } else {

                System.out.println(
                        "Stock reduction failed!"
                );
            }

        } else {

            System.out.println(
                    "Insufficient stock!"
            );
        }
    }
}