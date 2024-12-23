package entities;

import DAO.DAOAdmin;
import DAO.DAOCategory;
import DAO.DAOOrder;
import DAO.DAOProduct;

import java.util.ArrayList;
import java.util.List;

public class Database {
    // Static lists to store data
    public static List<Customer> customers = new ArrayList<>();
    public static List<Admin> admins = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();
    public static List<Cart> carts=new ArrayList<>();
    private final static DAOAdmin daoAdmin = new DAOAdmin();
    private final static DAOOrder daoOrder = new DAOOrder();
    private final static DAOProduct daoProduct = new DAOProduct();
    private final static DAOCategory daoCategory = new DAOCategory();
    // dummy data
    static {
        try {
            // First, create the categories
            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");


            Product laptop=new Product("Laptop", "High-performance laptop", 1200.0, 10, electronics);
            Product product=new Product("Shirt", "Cotton T-shirt", 25.0, 50, clothing);

            // Add customers
            Customer abdo=new Customer(
                    "ABDOOOOOOOOOOOO",
                    "password123",
                    "2002-09-23",
                    "123 Main St",
                    Customer.Gender.MALE,
                    List.of("Electronics")
            );
            Customer Seifo=new Customer(
                    "SEIIIIIIIIIFOOOOOOOOOOO",
                    "ezzzzzzzz123",
                    "2006-11-23",
                    "456 Elm St",
                    Customer.Gender.FEMALE,
                    List.of("Clothing")
            );

            // Add admins
            Admin admin=new Admin(
                    "admin",
                    "adminPass123",
                    "2003-08-23",
                    "Manager",
                    12
            );

        } catch (Exception e) {
            System.err.println("Error initializing dummy data: " + e.getMessage());
        }
    }

}