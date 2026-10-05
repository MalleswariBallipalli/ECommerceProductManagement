package com.ecommerce.test;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;

public class ProductDAOTest {

    public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO();

        Product product = new Product(
                "Nova Laptop",
                "Electronics",
                55000.00,
                10,
                "High performance business laptop",
                "ACTIVE"
        );

        boolean result = productDAO.addProduct(product);

        if (result) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Product addition failed!");
        }
    }
}