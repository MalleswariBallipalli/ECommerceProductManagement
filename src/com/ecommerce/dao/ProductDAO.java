package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.model.Product;
import com.ecommerce.util.DBConnection;

public class ProductDAO {

    // ================= ADD PRODUCT =================

    public boolean addProduct(Product product) {

        String sql = "INSERT INTO products "
                + "(product_name, category, price, stock_quantity, description, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setString(5, product.getDescription());
            statement.setString(6, product.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding product: " + e.getMessage()
            );

            return false;
        }
    }

    // ================= GET ALL PRODUCTS =================

    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products "
                + "ORDER BY product_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                products.add(mapProduct(resultSet));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching products: "
                    + e.getMessage()
            );
        }

        return products;
    }

    // ================= SEARCH BY CATEGORY =================

    public List<Product> searchByCategory(String category) {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products "
                + "WHERE category = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, category);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    products.add(mapProduct(resultSet));
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error searching products: "
                    + e.getMessage()
            );
        }

        return products;
    }

    // ================= GET PRODUCT BY ID =================

    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products "
                + "WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapProduct(resultSet);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching product: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // ================= CHECK STOCK =================

    public boolean isStockAvailable(
            int productId,
            int quantity) {

        String sql = "SELECT stock_quantity FROM products "
                + "WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    int availableStock =
                            resultSet.getInt("stock_quantity");

                    return availableStock >= quantity;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error checking stock: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // ================= UPDATE STOCK =================

    public boolean reduceStock(
            int productId,
            int quantity) {

        String sql = "UPDATE products "
                + "SET stock_quantity = stock_quantity - ? "
                + "WHERE product_id = ? "
                + "AND stock_quantity >= ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating stock: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // ================= MAP PRODUCT =================

    private Product mapProduct(
            ResultSet resultSet) throws SQLException {

        Product product = new Product();

        product.setProductId(
                resultSet.getInt("product_id")
        );

        product.setProductName(
                resultSet.getString("product_name")
        );

        product.setCategory(
                resultSet.getString("category")
        );

        product.setPrice(
                resultSet.getDouble("price")
        );

        product.setStockQuantity(
                resultSet.getInt("stock_quantity")
        );

        product.setDescription(
                resultSet.getString("description")
        );

        product.setStatus(
                resultSet.getString("status")
        );

        return product;
    }
}