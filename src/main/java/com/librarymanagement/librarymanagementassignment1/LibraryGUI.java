package com.librarymanagement.librarymanagementassignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.UUID;

public class LibraryGUI extends Application {

    private Library library = new Library(); // Library instance
    private FileManagement fileManager = new FileManagement();
    private TableView<Book> table = new TableView<>(); // Table for displaying books
    private String filename = "src/main/resources/com/librarymanagement/librarymanagementassignment1/books.csv";

    @Override
    public void start(Stage primaryStage) {
        // Load data from the file
        fileManager.readFile(filename, library);

        // Create UI components
        Label titleLabel = new Label("Double-click on a Book Title to Borrow or Return");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.getStyleClass().add("title-label"); // Add CSS class for styling

        table.setEditable(false);
        table.getStyleClass().add("table-view"); // Apply CSS styling for the table
        initializeTable();

        VBox titleBox = new VBox();
        titleBox.setAlignment(Pos.CENTER); // Align VBox content to the center
        titleBox.getChildren().add(titleLabel);

        // ComboBox for search
        ComboBox<String> searchCriteriaBox = new ComboBox<>();
        searchCriteriaBox.getItems().addAll("Title", "Author", "ISBN");
        searchCriteriaBox.setValue("Title"); // Default value
        searchCriteriaBox.getStyleClass().add("combo-box"); // Apply ComboBox styling

        TextField searchField = new TextField();
        searchField.setPromptText("Enter your search query here");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("text-field"); // Apply TextField styling

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button"); // Apply Button styling

        Button addBookButton = new Button("Add Book");
        addBookButton.getStyleClass().add("button"); // Apply Button styling

        Button displayAllBooks = new Button("Display All Books");
        displayAllBooks.getStyleClass().add("button"); // Apply Button styling

        // Event Handlers
        searchButton.setOnAction(e -> handleSearch(searchCriteriaBox.getValue(), searchField.getText()));
        addBookButton.setOnAction(e -> showAddBookWindow());
        displayAllBooks.setOnAction(e -> displayBooks());

        // Layout setup
        HBox searchBox = new HBox(10, searchCriteriaBox, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER); // Center-align the search box
        searchBox.setSpacing(10); // Add spacing between elements

        HBox buttonBox = new HBox(10, addBookButton, displayAllBooks); // Group buttons together
        buttonBox.setAlignment(Pos.CENTER); // Center-align the buttons
        buttonBox.setSpacing(10); // Add spacing

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER); // Center the main layout content
        layout.getChildren().addAll(titleBox, table, searchBox, buttonBox);

        Scene scene = new Scene(layout, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/com/librarymanagement/librarymanagementassignment1/styles.css").toExternalForm());

        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show all books at startup
        updateTable(convertList());
    }

    private void initializeTable() {
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));

        TableColumn<Book, String> availabilityColumn = new TableColumn<>("Book Availability");
        availabilityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookAvailability()));
        availabilityColumn.setStyle("-fx-alignment: CENTER;");

        titleColumn.setPrefWidth(450);
        availabilityColumn.setPrefWidth(450);

        table.getColumns().addAll(titleColumn, availabilityColumn);

        // Double-click event for book details
        table.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            // Check if clicked twice and row is not empty
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Book selectedBook = row.getItem();
                    showBookDetailsWindow(selectedBook);
                }
            });
            return row;
        });
    }

    private ObservableList<Book> convertList() {
        return FXCollections.observableArrayList(library.getAllBooks());
    }

    private void updateTable(ObservableList<Book> books) {
        table.setItems(books);
    }

    private void forceUpdateTable() {
        ObservableList<Book> updatedList = convertList(); // Get the updated list
        Platform.runLater(() -> {
            table.getItems().clear();
            table.setItems(updatedList); // Set the updated list to the TableView
        });
    }

    private void displayBooks() {
        updateTable(convertList()); // Reload data from the library
    }

    private void handleSearch(String criteria, String query) {
        if (query.trim().isEmpty()) {
            showAlert("Error", "Search field cannot be empty!");
            return;
        }

        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        String result = "";
        boolean found = false;

        switch (criteria) {
            case "Title":
                result = library.titleSearch(query);
                break;
            case "Author":
                result = library.authorSearch(query);
                break;
            case "ISBN":
                result = library.isbnSearch(query);
                break;
        }

        for (Book book : library.getAllBooks()) {
            if (criteria.equals("Title") && book.getBookTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
                found = true;
            } else if (criteria.equals("Author") && book.getBookAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
                found = true;
            } else if (criteria.equals("ISBN") && book.getBookISBN().contains(query)) {
                filteredBooks.add(book);
                found = true;
            }
        }

        if (!found) {
            showAlert("Book Not Found", result);
            return;
        } else {
            showAlert("Search Found", result);
        }
        updateTable(filteredBooks);
    }

    private void showBookDetailsWindow(Book book) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Book Details");
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label bookDetails = new Label(book.displayBook() + "\nBorrow ID: " +
                (book.getBorrowId() != null ? book.getBorrowId() : "Not Borrowed"));
        bookDetails.getStyleClass().add("small-label");

        Label bookdetails = new Label(book.displayBook());
        bookdetails.getStyleClass().add("small-label");

        Button borrowButton = new Button("Borrow Book");
        borrowButton.getStyleClass().add("button");

        Button returnButton = new Button("Return Book");
        returnButton.getStyleClass().add("button");

        borrowButton.setDisable(!book.getIsAvailable());
        returnButton.setDisable(book.getIsAvailable());

        HBox buttonBox = new HBox(15, borrowButton, returnButton);
        buttonBox.setAlignment(Pos.CENTER);

        borrowButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Borrow Book");
            dialog.setHeaderText("Enter your name to borrow the book:");
            dialog.showAndWait().ifPresent(name -> {
                if (name.isEmpty()) {
                    showAlert("Error", "Name cannot be empty!");
                } else {
                    String borrowId = generateBorrowId(); // Generate unique ID
                    book.borrowBook(name);
                    book.setBorrowId(borrowId); // Assign borrow ID
                    fileManager.writeFile(filename, library); // Save to file
                    showAlert("Success", "Book borrowed successfully! Your Borrow ID: " + borrowId);
                    detailStage.close();
                }
            });
        });


        returnButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Return Book");
            dialog.setHeaderText("Enter your Borrow ID:");
            dialog.showAndWait().ifPresent(borrowId -> {
                if (borrowId.isEmpty()) {
                    showAlert("Error", "Borrow ID cannot be empty!");
                } else if (borrowId.equals(book.getBorrowId())) {
                    book.returnBook();
                    library.removeBorrowId(borrowId); // Remove ID from the set
                    book.setBorrowId(null); // Clear the borrow ID
                    fileManager.writeFile(filename, library);
                    showAlert("Success", "Book returned successfully!");
                    detailStage.close();
                } else {
                    showAlert("Error", "Invalid Borrow ID! Book cannot be returned.");
                }
            });
        });

        layout.getChildren().addAll(bookdetails, buttonBox);
        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/librarymanagement/librarymanagementassignment1/styles.css").toExternalForm());
        detailStage.setScene(scene);
        detailStage.show();
    }

    public String generateBorrowId() {
        long timestamp = System.currentTimeMillis(); // Get current time in milliseconds
        int randomNum = (int) (Math.random() * 10000); // Generate a small random number
        return "BID" + timestamp + randomNum; // Combine both to form the ID
    }

    private void showAddBookWindow() {
        Stage addStage = new Stage();
        addStage.setTitle("Add New Book");
        VBox layout = new VBox(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.getStyleClass().add("text-field");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.getStyleClass().add("text-field");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        isbnField.getStyleClass().add("text-field");

        Button addButton = new Button("Add Book");
        addButton.getStyleClass().add("button");

        HBox buttonBox = new HBox(addButton);
        buttonBox.setAlignment(Pos.CENTER);

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                showAlert("Error", "All fields must be filled!");
            } else if (library.isTitleOrISBNExist(title, isbn)) {
                showAlert("Error", "Book with this Title or ISBN already exists!");
            } else {
                library.addBook(title, author, isbn, true, "none");
                fileManager.writeFile(filename, library);
                updateTable(convertList());
                showAlert("Success", "Book added successfully!");
                addStage.close();
            }
        });

        layout.getChildren().addAll(titleField, authorField, isbnField, buttonBox);

        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/librarymanagement/librarymanagementassignment1/styles.css").toExternalForm());
        addStage.setScene(scene);
        addStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/librarymanagement/librarymanagementassignment1/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
