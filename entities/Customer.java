package entities;

import DAO.DAOCustomer;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    

    public void addBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        this.balance += amount;
        System.out.println("Added " + amount + " to balance.");
    }

    public Order checkout() throws Exception {
        if(balance>=cart.calculateTotalPrice()){
        balance -= cart.calculateTotalPrice();
        Order o= new Order(this,cart, Order.PaymentMethod.CREDIT_CARD);
        this.addOrder(o);
        clearCart(cart);
        return o;}
        else throw new Exception("Insuffient balance");
    }

    public enum Gender {
        MALE, FEMALE,
    }

    private double balance;
    private String Address;
    private Gender Gender;
    private List<String> Interests;
    private Cart cart;
    private static final DAOCustomer daoCustomer = new DAOCustomer();
    public Customer() {cart= new Cart();}
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }


    // Constructor
    public Customer(String username, String password, String dateOfBirth,String address, Gender gender, List<String> interests) {
        super(username, password, dateOfBirth);

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        if (interests == null || interests.isEmpty()) {
            throw new IllegalArgumentException("Interests cannot be null or empty.");
        }

        balance = 0;
        this.Address = address;
        this.Gender = gender;
        this.Interests = new ArrayList<>(interests);
        cart= new Cart();
    }

    // Getters and Setters
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance must be positive.");
        }
        this.balance = balance;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        if (Address == null || Address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.Address = Address;
    }

    public Gender getGender() {
        return Gender;
    }

    public void setGender(Gender Gender) {
        if (Gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        this.Gender = Gender;
    }

    public List<String> getInterests() {
        return new ArrayList<>(Interests);
    }

    public void setInterests(List<String> Interests) {
        if (Interests == null || Interests.isEmpty()) {
            throw new IllegalArgumentException("Interests cannot be null or empty.");
        }
        this.Interests = new ArrayList<>(Interests);
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + getUserName() + '\'' +
                ", balance=" + balance +
                ", address='" + Address + '\'' +
                ", gender=" + Gender +
                ", interests=" + Interests +
                '}';
    }

    public void addProductToCart( Product product, int quantity) {
        cart.addCartItem(product, quantity);
        System.out.println("Product added to cart: " + product.getName() + " (x" + quantity + ")");
    }

    public void viewCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        System.out.println(cart);
    }

    public void updateProductQuantity(Cart cart, Product product, int newQuantity) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        if (product == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Product cannot be null, and quantity must be positive.");
        }

        for(Cart.CartItem i:cart.getItems()){
            if(i.getProduct()==product){
                i.setQuantity(newQuantity);
                return;
            }
        }
        throw new IllegalArgumentException("Product not in Cart");
    }

    public void removeProductFromCart(Product product) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        cart.removeCartItem(product);
        System.out.println("Product removed from cart: " + product.getName());
    }

    public void clearCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }

        cart.getItems().clear();
        System.out.println("Cart cleared successfully.");
    }

    public void displayTotalPrice(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        double totalPrice = cart.calculateTotalPrice();
        System.out.println("Total Price of cart: $" + totalPrice);
    }
}
