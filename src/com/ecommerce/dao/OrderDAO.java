
package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.model.Order;
import com.ecommerce.util.DBConnection;

public class OrderDAO {

    // ADD ORDER
    public int addOrder(Order order) {

        String sql = "INSERT INTO orders "
                + "(customer_id, total_amount, status) "
                + "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getCustomerId());
            statement.setDouble(2, order.getTotalAmount());
            statement.setString(3, order.getStatus());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                try (ResultSet keys =
                             statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error adding order: " + e.getMessage());
        }

        return -1;
    }

    // GET ORDER BY ID
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders "
                + "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapOrder(resultSet);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching order: " + e.getMessage());
        }

        return null;
    }

    // GET ALL ORDERS
    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                + "ORDER BY order_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                orders.add(mapOrder(resultSet));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching orders: " + e.getMessage());
        }

        return orders;
    }

    // UPDATE ORDER STATUS
    public boolean updateOrderStatus(
            int orderId, String status) {

        // Convert input to uppercase
        status = status.trim().toUpperCase();

        // Validate order status
        if (!status.equals("PLACED")
                && !status.equals("SHIPPED")
                && !status.equals("DELIVERED")
                && !status.equals("CANCELLED")) {

            System.out.println("Invalid order status!");
            System.out.println(
                    "Allowed statuses: "
                    + "PLACED, SHIPPED, DELIVERED, CANCELLED");

            return false;
        }

        String sql = "UPDATE orders SET status = ? "
                + "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, orderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error updating order status: "
                            + e.getMessage());

            return false;
        }
    }

    // MAP RESULTSET TO ORDER
    private Order mapOrder(ResultSet resultSet)
            throws SQLException {

        Order order = new Order();

        order.setOrderId(
                resultSet.getInt("order_id"));

        order.setCustomerId(
                resultSet.getInt("customer_id"));

        order.setOrderDate(
                resultSet.getTimestamp("order_date"));

        order.setTotalAmount(
                resultSet.getDouble("total_amount"));

        order.setStatus(
                resultSet.getString("status"));

        return order;
    }
}
