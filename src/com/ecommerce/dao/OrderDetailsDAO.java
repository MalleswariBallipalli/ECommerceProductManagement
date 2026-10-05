package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecommerce.util.DBConnection;

public class OrderDetailsDAO {

    // ================= ADD ORDER DETAIL =================

    public boolean addOrderDetail(
            int orderId,
            int productId,
            int quantity,
            double unitPrice) {

        String sql = "INSERT INTO order_items "
                + "(order_id, product_id, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.setDouble(4, unitPrice);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding order detail: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // ================= DISPLAY ORDER DETAILS =================

    public void displayOrderDetails(int orderId) {

        String sql =
                "SELECT o.order_id, "
                + "c.customer_name, "
                + "p.product_name, "
                + "p.category, "
                + "oi.quantity, "
                + "oi.unit_price, "
                + "(oi.quantity * oi.unit_price) AS item_total, "
                + "o.total_amount, "
                + "o.status "
                + "FROM orders o "
                + "JOIN customers c "
                + "ON o.customer_id = c.customer_id "
                + "JOIN order_items oi "
                + "ON o.order_id = oi.order_id "
                + "JOIN products p "
                + "ON oi.product_id = p.product_id "
                + "WHERE o.order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    System.out.println();
                    System.out.println(
                            "======================================"
                    );
                    System.out.println(
                            "          ORDER DETAILS"
                    );
                    System.out.println(
                            "======================================"
                    );

                    System.out.println(
                            "Order ID      : "
                            + resultSet.getInt("order_id")
                    );

                    System.out.println(
                            "Customer      : "
                            + resultSet.getString("customer_name")
                    );

                    System.out.println(
                            "Product       : "
                            + resultSet.getString("product_name")
                    );

                    System.out.println(
                            "Category      : "
                            + resultSet.getString("category")
                    );

                    System.out.println(
                            "Quantity      : "
                            + resultSet.getInt("quantity")
                    );

                    System.out.println(
                            "Unit Price    : "
                            + resultSet.getDouble("unit_price")
                    );

                    System.out.println(
                            "Item Total    : "
                            + resultSet.getDouble("item_total")
                    );

                    System.out.println(
                            "Total Amount  : "
                            + resultSet.getDouble("total_amount")
                    );

                    System.out.println(
                            "Status        : "
                            + resultSet.getString("status")
                    );

                    System.out.println(
                            "======================================"
                    );
                }

                if (!found) {

                    System.out.println(
                            "No order details found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching order details: "
                    + e.getMessage()
            );
        }
    }
}