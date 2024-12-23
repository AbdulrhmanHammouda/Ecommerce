package Design;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Service.UserManager;
import entities.*;
import java.util.Arrays;

public class ECommerceGUI extends Application {
    private Stage primaryStage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene adminDashboard;
    private Scene customerDashboard;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("E-Commerce System");

        createLoginScene();
        createRegistrationScene();

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createLoginScene() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("E-Commerce System Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register New Account");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            try {
                currentUser = UserManager.loginUser(usernameField.getText(), passwordField.getText());
                if (currentUser instanceof Admin) {
                    createAdminDashboard((Admin) currentUser);
                    primaryStage.setScene(adminDashboard);
                } else if (currentUser instanceof Customer) {
                    createCustomerDashboard((Customer) currentUser);
                    primaryStage.setScene(customerDashboard);
                }
            } catch (Exception ex) {
                messageLabel.setText("Invalid username or password");
            }
        });

        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));

        loginLayout.getChildren().addAll(
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton,
                registerButton,
                messageLabel
        );

        loginScene = new Scene(loginLayout, 800, 600);
    }

    private void createRegistrationScene() {
        VBox registrationLayout = new VBox(10);
        registrationLayout.setPadding(new Insets(20));
        registrationLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Register New Account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Customer", "Admin");
        userTypeCombo.setValue("Select");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth (YYYY-MM-DD)");

        // Customer specific fields
        VBox customerFields = new VBox(10);
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        ComboBox<Customer.Gender> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Customer.Gender.values());
        TextField interestsField = new TextField();
        interestsField.setPromptText("Interests (comma-separated)");

        // Admin specific fields
        VBox adminFields = new VBox(10);
        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        TextField workingHoursField = new TextField();
        workingHoursField.setPromptText("Working Hours");

        userTypeCombo.setOnAction(e -> {
            if (userTypeCombo.getValue().equals("Customer")) {
                registrationLayout.getChildren().remove(adminFields);
                if (!registrationLayout.getChildren().contains(customerFields)) {
                    registrationLayout.getChildren().add(customerFields);
                }
            } else {
                registrationLayout.getChildren().remove(customerFields);
                if (!registrationLayout.getChildren().contains(adminFields)) {
                    registrationLayout.getChildren().add(adminFields);
                }
            }
        });

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        registerButton.setOnAction(e -> {
            try {
                if(userTypeCombo.getValue().equals("Select")){
                    throw new Exception("Please select user type");
                }
                else if (userTypeCombo.getValue().equals("Customer")) {
                    Customer customer = new Customer(
                            usernameField.getText(),
                            passwordField.getText(),
                            dobField.getText(),
                            addressField.getText(),
                            genderCombo.getValue(),
                            Arrays.asList(interestsField.getText().split(","))
                    );
                    UserManager.registerUser(customer);
                } else {
                    Admin admin = new Admin(
                            usernameField.getText(),
                            passwordField.getText(),
                            dobField.getText(),
                            roleField.getText(),
                            Integer.parseInt(workingHoursField.getText())
                    );
                    UserManager.registerUser(admin);
                }
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Registration successful!");
                primaryStage.setScene(loginScene);
            } catch (Exception ex) {
                messageLabel.setText("Registration failed: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(loginScene));

        customerFields.getChildren().addAll(
                new Label("Address:"),
                addressField,
                new Label("Gender:"),
                genderCombo,
                new Label("Interests:"),
                interestsField
        );

        adminFields.getChildren().addAll(
                new Label("Role:"),
                roleField,
                new Label("Working Hours:"),
                workingHoursField
        );

        registrationLayout.getChildren().addAll(
                titleLabel,
                new Label("User Type:"),
                userTypeCombo,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Date of Birth:"),
                dobField,
                registerButton,
                backButton,
                messageLabel
        );
        registerScene =new Scene(registrationLayout, 800,600);
    }

    private void createAdminDashboard(Admin admin) {
        TabPane tabPane = new TabPane();

        // Products Tab
        Tab productsTab = new Tab("Products");
        productsTab.setClosable(false);
        VBox productsLayout = new VBox(10);
        productsLayout.setPadding(new Insets(10));

        TableView<Product> productTable = new TableView<>();
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getName()));

        productTable.getColumns().addAll(idCol, nameCol, descCol, priceCol, quantityCol, categoryCol);
        productTable.setItems(FXCollections.observableArrayList(Database.products));

        Button addProductButton = new Button("Add Product");
        Button editProductButton = new Button("Edit Product");
        Button deleteProductButton = new Button("Delete Product");

        HBox productButtons = new HBox(10);
        productButtons.getChildren().addAll(addProductButton, editProductButton, deleteProductButton);

        productsLayout.getChildren().addAll(productTable, productButtons);
        productsTab.setContent(productsLayout);

        // Orders Tab
        Tab ordersTab = new Tab("Orders");
        ordersTab.setClosable(false);
        VBox ordersLayout = new VBox(10);
        ordersLayout.setPadding(new Insets(10));

        TableView<Order> orderTable = new TableView<>();
        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCustomer().getUserName()));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.getColumns().addAll(orderIdCol, customerCol, totalCol, statusCol);
        orderTable.setItems(FXCollections.observableArrayList(Database.orders));

        Button viewOrderButton = new Button("View Order Details");
        Button updateStatusButton = new Button("Update Status");

        HBox orderButtons = new HBox(10);
        orderButtons.getChildren().addAll(viewOrderButton, updateStatusButton);

        ordersLayout.getChildren().addAll(orderTable, orderButtons);
        ordersTab.setContent(ordersLayout);

        // Categories Tab
        Tab categoriesTab = new Tab("Categories");
        categoriesTab.setClosable(false);
        VBox categoriesLayout = new VBox(10);
        categoriesLayout.setPadding(new Insets(10));

        TableView<Category> categoryTable = new TableView<>();
        TableColumn<Category, Integer> categoryIdCol = new TableColumn<>("ID");
        TableColumn<Category, String> categoryNameCol = new TableColumn<>("Name");

        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        categoryTable.getColumns().addAll(categoryIdCol, categoryNameCol);
        categoryTable.setItems(FXCollections.observableArrayList(Database.categories));

        Button addCategoryButton = new Button("Add Category");
        Button editCategoryButton = new Button("Edit Category");
        Button deleteCategoryButton = new Button("Delete Category");

        HBox categoryButtons = new HBox(10);
        categoryButtons.getChildren().addAll(addCategoryButton, editCategoryButton, deleteCategoryButton);

        categoriesLayout.getChildren().addAll(categoryTable, categoryButtons);
        categoriesTab.setContent(categoriesLayout);

        tabPane.getTabs().addAll(productsTab, ordersTab, categoriesTab);
        adminDashboard = new Scene(tabPane, 800, 600);
    }

    private void createCustomerDashboard(Customer customer) {
        TabPane tabPane = new TabPane();

        // Products Tab
        Tab productsTab = new Tab("Products");
        productsTab.setClosable(false);
        VBox productsLayout = new VBox(10);
        productsLayout.setPadding(new Insets(10));

        TableView<Product> productTable = new TableView<>();
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getName()));

        productTable.getColumns().addAll(nameCol, descCol, priceCol, categoryCol);
        productTable.setItems(FXCollections.observableArrayList(Database.products));

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, 1);
        Button addToCartButton = new Button("Add to Cart");
        TableView<Cart.CartItem> cartTable = new TableView<>();
        TableColumn<Cart.CartItem,String> cartNameCol = new TableColumn<>("Name");
        TableColumn<Cart.CartItem, Double> cartPriceCol = new TableColumn<>("Price");
        TableColumn<Cart.CartItem, Integer> cartQuantityCol = new TableColumn<>("Quantity");

        cartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        cartTable.getColumns().addAll(cartNameCol, cartPriceCol, cartQuantityCol);

        Label totalLabel = new Label("Total: $0.00");
        Button checkoutButton = new Button("Checkout");
        Button removeFromCartButton = new Button("Remove Selected");
        updateCartDisplay(cartTable, customer, totalLabel);

        addToCartButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                customer.addProductToCart(selectedProduct, quantitySpinner.getValue());
                updateCartDisplay(cartTable, customer, totalLabel);
            }
        });


        HBox productControls = new HBox(10);
        productControls.getChildren().addAll(new Label("Quantity:"), quantitySpinner, addToCartButton);
        productsLayout.getChildren().addAll(productTable, productControls);
        productsTab.setContent(productsLayout);

        // Cart Tab
        Tab cartTab = new Tab("Cart");
        cartTab.setClosable(false);
        VBox cartLayout = new VBox(10);
        cartLayout.setPadding(new Insets(10));

        TableView<Order> orderTable = new TableView<>();
        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        removeFromCartButton.setOnAction(e -> {
            Cart.CartItem selectedProduct = cartTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                customer.removeProductFromCart(selectedProduct.getProduct());
                updateCartDisplay(cartTable, customer, totalLabel);
            }
        });

        HBox cartButtons = new HBox(10);
        cartButtons.getChildren().addAll(removeFromCartButton, checkoutButton);
        cartLayout.getChildren().addAll(cartTable, totalLabel, cartButtons);
        cartTab.setContent(cartLayout);

        // Orders Tab
        Tab ordersTab = new Tab("Orders");
        ordersTab.setClosable(false);
        VBox ordersLayout = new VBox(10);
        ordersLayout.setPadding(new Insets(10));



        orderTable.getColumns().addAll(orderIdCol, totalCol, statusCol, dateCol);

        Button viewOrderDetailsButton = new Button("View Details");
        viewOrderDetailsButton.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showOrderDetailsDialog(selectedOrder);
            }
        });

        ordersLayout.getChildren().addAll(orderTable, viewOrderDetailsButton);
        ordersTab.setContent(ordersLayout);

        // Profile Tab
        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username: " + customer.getUserName());
        Label balanceLabel = new Label("Balance: $" + customer.getBalance());
        Label addressLabel = new Label("Address: " + customer.getAddress());

        checkoutButton.setOnAction(e -> {
            try {
                Order order = customer.checkout();
                updateOrderTable(orderTable, customer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully!");
                cartTable.setItems(FXCollections.observableArrayList()); // Clear cart table
                totalLabel.setText("Total: $0.00");
                updateBalanceInProfile( balanceLabel ,customer.getBalance());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Checkout failed: " + ex.getMessage());
            }
        });

        Button editProfileButton = new Button("Edit Profile");
        Button addBalanceButton = new Button("Add Balance");

        editProfileButton.setOnAction(e -> showEditProfileDialog(customer));
        addBalanceButton.setOnAction(e -> showAddBalanceDialog(customer, balanceLabel));

        HBox profileButtons = new HBox(10);
        profileButtons.getChildren().addAll(editProfileButton, addBalanceButton);

        profileLayout.getChildren().addAll(
                usernameLabel,
                balanceLabel,
                addressLabel,
                profileButtons
        );
        profileTab.setContent(profileLayout);

        tabPane.getTabs().addAll(productsTab, cartTab, ordersTab, profileTab);
        customerDashboard = new Scene(tabPane, 800, 600);

        // Add a logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            primaryStage.setScene(loginScene);
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(tabPane, logoutButton);
        customerDashboard = new Scene(root, 800, 600);
    }
    private void updateBalanceInProfile(Label balanceLabel, double updatedBalance) {
        balanceLabel.setText(String.format("Balance: $%.2f", updatedBalance));
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateCartDisplay(TableView<Cart.CartItem> cartTable, Customer customer, Label totalLabel) {
        // Create a list of CartItem objects from the cart
        ObservableList<Cart.CartItem> cartItems = FXCollections.observableArrayList();
        for (Cart.CartItem cartItem : customer.getCart().getItems()) {
            cartItems.add(cartItem);  // Add the cart item, which includes product and quantity
        }

        // Set up columns for the cart table
        TableColumn<Cart.CartItem, String> cartNameCol = new TableColumn<>("Name");
        cartNameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProduct().getName())
        );

        TableColumn<Cart.CartItem, Double> cartPriceCol = new TableColumn<>("Price");
        cartPriceCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject()
        );

        TableColumn<Cart.CartItem, Integer> cartQuantityCol = new TableColumn<>("Quantity");
        cartQuantityCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );

        // Update the cart table with the list of cart items
        cartTable.setItems(cartItems);
        cartTable.getColumns().setAll(cartNameCol, cartPriceCol, cartQuantityCol);

        // Calculate the total price of the cart
        double totalPrice = customer.getCart().calculateTotalPrice();

        // Update the total label
        totalLabel.setText(String.format("Total: $%.2f", totalPrice));
    }



    private void showOrderDetailsDialog(Order order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Order Details");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<Cart.CartItem> productsTable = new TableView<>();
        TableColumn<Cart.CartItem, String> nameCol = new TableColumn<>("Product");
        TableColumn<Cart.CartItem, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Cart.CartItem, Integer> quantityCol = new TableColumn<>("Quantity");

        // Set cell value factories for each column
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        priceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        // Add columns to the table
        productsTable.getColumns().addAll(nameCol, priceCol, quantityCol);

        // Ensure that getOrderItems() is not empty
        if (order.getItems() == null || order.getItems().isEmpty()) {
            System.out.println("Order has no items.");
        } else {
            // Set the items for the table (order items)
            productsTable.setItems(FXCollections.observableArrayList(order.getItems()));
        }

        // Add labels for order details
        Label totalLabel = new Label(String.format("Total: $%.2f", order.getTotalPrice()));
        Label statusLabel = new Label("Status: " + order.getStatus());
        Label dateLabel = new Label("Order Date: " + order.getOrderDate());

        content.getChildren().addAll(
                productsTable,
                totalLabel,
                statusLabel,
                dateLabel
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    private void updateOrderTable(TableView<Order> orderTable, Customer customer) {
        // Assuming that each customer has a list of orders (you can adjust this if needed)
        ObservableList<Order> orders = FXCollections.observableArrayList(customer.getOrders());

        // Update the order table with the list of orders
        orderTable.setItems(orders);
    }


    private void showEditProfileDialog(Customer customer) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField addressField = new TextField(customer.getAddress());
        ComboBox<Customer.Gender> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Customer.Gender.values());
        genderCombo.setValue(customer.getGender());

        grid.add(new Label("Address:"), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(new Label("Gender:"), 0, 1);
        grid.add(genderCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                customer.setAddress(addressField.getText());
                customer.setGender(genderCombo.getValue());
                return true;
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showAddBalanceDialog(Customer customer, Label balanceLabel) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Balance");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

        content.getChildren().addAll(
                new Label("Amount to add:"),
                amountField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    customer.addBalance(amount);
                    balanceLabel.setText("Balance: $" + customer.getBalance());
                    return true;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid amount");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}