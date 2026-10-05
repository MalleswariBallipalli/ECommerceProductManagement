package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidQuantityException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.util.DBConnection;

public class OrderService {

    public boolean placeOrder(
            int customerId,
            int productId,
            int quantity) {

        Connection connection = null;

        try {

            // ==========================================
            // 1. VALIDATE QUANTITY
            // ==========================================

            validateQuantity(quantity);

            // ==========================================
            // 2. GET DATABASE CONNECTION
            // ==========================================

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            // ==========================================
            // 3. VALIDATE CUSTOMER
            // ==========================================

            validateCustomer(
                    connection,
                    customerId
            );

            // ==========================================
            // 4. GET PRODUCT DETAILS
            // ==========================================

            ProductInfo product =
                    getProduct(
                            connection,
                            productId
                    );

            // ==========================================
            // 5. VALIDATE STOCK
            // ==========================================

            validateStock(
                    product.stockQuantity,
                    quantity
            );

            // ==========================================
            // 6. CALCULATE TOTAL
            // ==========================================

            double totalAmount =
                    product.price * quantity;

            // ==========================================
            // 7. INSERT ORDER
            // ==========================================

            int orderId =
                    insertOrder(
                            connection,
                            customerId,
                            totalAmount
                    );

            // ==========================================
            // 8. INSERT ORDER ITEM
            // ==========================================

            insertOrderItem(
                    connection,
                    orderId,
                    productId,
                    quantity,
                    product.price
            );

            // ==========================================
            // 9. REDUCE STOCK
            // ==========================================

            reduceStock(
                    connection,
                    productId,
                    quantity
            );

            // ==========================================
            // 10. COMMIT TRANSACTION
            // ==========================================

            connection.commit();

            // ==========================================
            // 11. SUCCESS MESSAGE
            // ==========================================

            System.out.println();
            System.out.println(
                    "Order placed successfully!"
            );

            System.out.println(
                    "Generated Order ID: "
                    + orderId
            );

            System.out.println(
                    "Product: "
                    + product.productName
            );

            System.out.println(
                    "Quantity: "
                    + quantity
            );

            System.out.println(
                    "Unit Price: "
                    + product.price
            );

            System.out.println(
                    "Total Amount: "
                    + totalAmount
            );

            System.out.println(
                    "Remaining Stock: "
                    + (product.stockQuantity - quantity)
            );

            return true;

        } catch (
                CustomerNotFoundException
                | ProductNotFoundException
                | InsufficientStockException
                | InvalidQuantityException e) {

            // ==========================================
            // BUSINESS VALIDATION ERROR
            // ==========================================

            rollback(connection);

            System.out.println();
            System.out.println(
                    "Order placement failed!"
            );

            System.out.println(
                    "Reason: "
                    + e.getMessage()
            );

            return false;

        } catch (SQLException e) {

            // ==========================================
            // DATABASE ERROR
            // ==========================================

            rollback(connection);

            System.out.println();
            System.out.println(
                    "Database error while placing order!"
            );

            System.out.println(
                    "Reason: "
                    + e.getMessage()
            );

            return false;

        } finally {

            closeConnection(connection);
        }
    }

    // =====================================================
    // VALIDATE QUANTITY
    // =====================================================

    private void validateQuantity(int quantity)
            throws InvalidQuantityException {

        if (quantity <= 0) {

            throw new InvalidQuantityException(
                    "Quantity must be greater than zero."
            );
        }
    }

    // =====================================================
    // VALIDATE CUSTOMER
    // =====================================================

    private void validateCustomer(
            Connection connection,
            int customerId)
            throws SQLException,
            CustomerNotFoundException {

        String sql =
                "SELECT customer_id "
                + "FROM customers "
                + "WHERE customer_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    customerId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new CustomerNotFoundException(
                            "Customer not found: "
                            + customerId
                    );
                }
            }
        }
    }

    // =====================================================
    // GET PRODUCT
    // =====================================================

    private ProductInfo getProduct(
            Connection connection,
            int productId)
            throws SQLException,
            ProductNotFoundException {

        String sql =
                "SELECT product_name, price, stock_quantity "
                + "FROM products "
                + "WHERE product_id = ? "
                + "AND status = 'ACTIVE'";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    productId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new ProductNotFoundException(
                            "Product not found: "
                            + productId
                    );
                }

                String productName =
                        resultSet.getString(
                                "product_name"
                        );

                double price =
                        resultSet.getDouble(
                                "price"
                        );

                int stockQuantity =
                        resultSet.getInt(
                                "stock_quantity"
                        );

                return new ProductInfo(
                        productName,
                        price,
                        stockQuantity
                );
            }
        }
    }

    // =====================================================
    // VALIDATE STOCK
    // =====================================================

    private void validateStock(
            int availableStock,
            int requestedQuantity)
            throws InsufficientStockException {

        if (requestedQuantity > availableStock) {

            throw new InsufficientStockException(
                    "Insufficient stock. "
                    + "Available stock: "
                    + availableStock
                    + ", Requested quantity: "
                    + requestedQuantity
            );
        }
    }

    // =====================================================
    // INSERT ORDER
    // =====================================================

    private int insertOrder(
            Connection connection,
            int customerId,
            double totalAmount)
            throws SQLException {

        String sql =
                "INSERT INTO orders "
                + "(customer_id, total_amount, status) "
                + "VALUES (?, ?, 'PLACED')";

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    customerId
            );

            statement.setDouble(
                    2,
                    totalAmount
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 0) {

                throw new SQLException(
                        "Order creation failed."
                );
            }

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (!keys.next()) {

                    throw new SQLException(
                            "Order ID could not be generated."
                    );
                }

                return keys.getInt(1);
            }
        }
    }

    // =====================================================
    // INSERT ORDER ITEM
    // =====================================================

    private void insertOrderItem(
            Connection connection,
            int orderId,
            int productId,
            int quantity,
            double unitPrice)
            throws SQLException {

        String sql =
                "INSERT INTO order_items "
                + "(order_id, product_id, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    orderId
            );

            statement.setInt(
                    2,
                    productId
            );

            statement.setInt(
                    3,
                    quantity
            );

            statement.setDouble(
                    4,
                    unitPrice
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 0) {

                throw new SQLException(
                        "Order item creation failed."
                );
            }
        }
    }

    // =====================================================
    // REDUCE STOCK
    // =====================================================

    private void reduceStock(
            Connection connection,
            int productId,
            int quantity)
            throws SQLException {

        String sql =
                "UPDATE products "
                + "SET stock_quantity = "
                + "stock_quantity - ? "
                + "WHERE product_id = ? "
                + "AND stock_quantity >= ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    quantity
            );

            statement.setInt(
                    2,
                    productId
            );

            statement.setInt(
                    3,
                    quantity
            );

            int rows =
                    statement.executeUpdate();

            if (rows == 0) {

                throw new SQLException(
                        "Stock update failed."
                );
            }
        }
    }

    // =====================================================
    // ROLLBACK
    // =====================================================

    private void rollback(
            Connection connection) {

        if (connection != null) {

            try {

                connection.rollback();

            } catch (SQLException e) {

                System.out.println(
                        "Rollback failed: "
                        + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // CLOSE CONNECTION
    // =====================================================

    private void closeConnection(
            Connection connection) {

        if (connection != null) {

            try {

                connection.setAutoCommit(true);

                connection.close();

            } catch (SQLException e) {

                System.out.println(
                        "Connection closing failed: "
                        + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // PRODUCT INFO CLASS
    // =====================================================

    private static class ProductInfo {

        private final String productName;
        private final double price;
        private final int stockQuantity;

        private ProductInfo(
                String productName,
                double price,
                int stockQuantity) {

            this.productName = productName;
            this.price = price;
            this.stockQuantity = stockQuantity;
        }
    }
}