package com.ecommerce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.model.OrderItem;
import com.ecommerce.util.DBConnection;

public class OrderItemDAO {

    // ADD ORDER ITEM
    public boolean addOrderItem(OrderItem item) {

        String sql = "INSERT INTO order_items "
                + "(order_id, product_id, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, item.getOrderId());
            statement.setInt(2, item.getProductId());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getUnitPrice());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding order item: "
                            + e.getMessage());

            return false;
        }
    }

    // GET ITEMS BY ORDER ID
    public List<OrderItem> getItemsByOrderId(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items "
                + "WHERE order_id = ? "
                + "ORDER BY order_item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    items.add(mapOrderItem(resultSet));
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching order items: "
                            + e.getMessage());
        }

        return items;
    }

    // GET ALL ORDER ITEMS
    public List<OrderItem> getAllOrderItems() {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items "
                + "ORDER BY order_item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapOrderItem(resultSet));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error fetching order items: "
                            + e.getMessage());
        }

        return items;
    }

    // MAP RESULTSET TO ORDER ITEM
    private OrderItem mapOrderItem(ResultSet resultSet)
            throws SQLException {

        OrderItem item = new OrderItem();

        item.setOrderItemId(
                resultSet.getInt("order_item_id"));

        item.setOrderId(
                resultSet.getInt("order_id"));

        item.setProductId(
                resultSet.getInt("product_id"));

        item.setQuantity(
                resultSet.getInt("quantity"));

        item.setUnitPrice(
                resultSet.getDouble("unit_price"));

        return item;
    }
}