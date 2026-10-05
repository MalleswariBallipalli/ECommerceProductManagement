package com.ecommerce.app;

import java.util.List;
import java.util.Scanner;

import com.ecommerce.dao.CustomerDAO;
import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.OrderDetailsDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.service.OrderService;

public class ECommerceApplication {

    private static final Scanner scanner = new Scanner(System.in);

    private static final ProductDAO productDAO = new ProductDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();

    // Transaction-based order service
    private static final OrderService orderService = new OrderService();

    public static void main(String[] args) {

        int choice;

        do {

            displayMenu();

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addProduct();
                    break;

                case 2:
                    viewProducts();
                    break;

                case 3:
                    searchProducts();
                    break;

                case 4:
                    addCustomer();
                    break;

                case 5:
                    viewCustomers();
                    break;

                case 6:
                    placeOrder();
                    break;

                case 7:
                    viewOrders();
                    break;

                case 8:
                    updateOrderStatus();
                    break;

                case 9:
                    viewOrderDetails();
                    break;

                case 10:
                    System.out.println();
                    System.out.println(
                            "Thank you for using E-Commerce Management System."
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }

        } while (choice != 10);

        scanner.close();
    }

    // =====================================================
    // DISPLAY MENU
    // =====================================================

    private static void displayMenu() {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("     E-COMMERCE PRODUCT MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Search Product by Category");
        System.out.println("4. Add Customer");
        System.out.println("5. View All Customers");
        System.out.println("6. Place Order");
        System.out.println("7. View All Orders");
        System.out.println("8. Update Order Status");
        System.out.println("9. View Order Details");
        System.out.println("10. Exit");

        System.out.println("==============================================");
    }

    // =====================================================
    // ADD PRODUCT
    // =====================================================

    private static void addProduct() {

        System.out.println();
        System.out.println("--- Add Product ---");

        System.out.print("Product Name: ");
        String name = scanner.nextLine();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        System.out.print("Price: ");
        double price = scanner.nextDouble();

        System.out.print("Stock Quantity: ");
        int stock = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Status: ");
        String status = scanner.nextLine();

        Product product = new Product(
                name,
                category,
                price,
                stock,
                description,
                status
        );

        if (productDAO.addProduct(product)) {

            System.out.println(
                    "Product added successfully!"
            );

        } else {

            System.out.println(
                    "Product addition failed!"
            );
        }
    }

    // =====================================================
    // VIEW PRODUCTS
    // =====================================================

    private static void viewProducts() {

        System.out.println();
        System.out.println("--- Product List ---");

        List<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {

            System.out.println(
                    "No products found."
            );

            return;
        }

        for (Product product : products) {
            System.out.println(product);
        }
    }

    // =====================================================
    // SEARCH PRODUCTS BY CATEGORY
    // =====================================================

    private static void searchProducts() {

        System.out.println();
        System.out.println("--- Search Products ---");

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        List<Product> products =
                productDAO.searchByCategory(category);

        if (products.isEmpty()) {

            System.out.println(
                    "No products found."
            );

            return;
        }

        for (Product product : products) {
            System.out.println(product);
        }
    }

    // =====================================================
    // ADD CUSTOMER
    // =====================================================

    private static void addCustomer() {

        System.out.println();
        System.out.println("--- Add Customer ---");

        System.out.print("Customer Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(
                name,
                email,
                phone,
                address
        );

        if (customerDAO.addCustomer(customer)) {

            System.out.println(
                    "Customer added successfully!"
            );

        } else {

            System.out.println(
                    "Customer addition failed!"
            );
        }
    }

    // =====================================================
    // VIEW CUSTOMERS
    // =====================================================

    private static void viewCustomers() {

        System.out.println();
        System.out.println("--- Customer List ---");

        List<Customer> customers =
                customerDAO.getAllCustomers();

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers found."
            );

            return;
        }

        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    private static void placeOrder() {

        System.out.println();
        System.out.println("--- Place Order ---");

        System.out.print("Customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Product ID: ");
        int productId = scanner.nextInt();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        scanner.nextLine();

        orderService.placeOrder(
                customerId,
                productId,
                quantity
        );
    }

    // =====================================================
    // VIEW ALL ORDERS
    // =====================================================

    private static void viewOrders() {

        System.out.println();
        System.out.println("--- Order List ---");

        List<Order> orders =
                orderDAO.getAllOrders();

        if (orders.isEmpty()) {

            System.out.println(
                    "No orders found."
            );

            return;
        }

        for (Order order : orders) {
            System.out.println(order);
        }
    }

    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    private static void updateOrderStatus() {

        System.out.println();
        System.out.println("--- Update Order Status ---");

        System.out.print("Order ID: ");
        int orderId = scanner.nextInt();

        scanner.nextLine();

        System.out.print("New Status: ");
        String status = scanner.nextLine();

        if (orderDAO.updateOrderStatus(
                orderId,
                status)) {

            System.out.println(
                    "Order status updated successfully!"
            );

        } else {

            System.out.println(
                    "Order status update failed!"
            );
        }
    }

    // =====================================================
    // VIEW ORDER DETAILS
    // =====================================================

    private static void viewOrderDetails() {

        System.out.println();
        System.out.println("--- Order Details ---");

        System.out.print("Enter Order ID: ");
        int orderId = scanner.nextInt();

        scanner.nextLine();

        orderDetailsDAO.displayOrderDetails(orderId);
    }
}