package com.librarymanagement.librarymanagementassignment1;

public class Book {
    private String bookAuthor;
    private String bookTitle;
    private String bookISBN;
    private String borrowerName; // Set to "none" meaning nobody borrowed.
    private Boolean isAvailable;
    private String borrowId; // Unique ID for borrowed books


    // Constructor
    public Book(String title, String author, String ISBN, Boolean bool, String name) {
        this.bookTitle = title;
        this.bookAuthor = author;
        this.bookISBN = ISBN;
        this.borrowerName = name;
        this.isAvailable = bool;
    }

    // Getter Methods
    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    // Return a book method
    public void returnBook() {
        if (!isAvailable) { // If the book is borrowed
            isAvailable = true; // Reset book to available
            borrowerName = "none"; // Remove borrower name
        }
    }

    // Borrow a book method
    public void borrowBook(String name) {
        if (isAvailable) { // If the book is available
            isAvailable = false;
            borrowerName = name;
        }
    }

    // Get book availability as a string
    public String getBookAvailability() {
        return isAvailable ? "Available" : "Borrowed";
    }

    // Getter and Setter for borrowId
    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    // Display book details
    public String displayBook() {
        return "Title: " + bookTitle + "\n" +
                "Author: " + bookAuthor + "\n" +
                "ISBN: " + bookISBN + "\n" +
                "Status: " + getBookAvailability() + "\n" +
                "Borrower Name: " + borrowerName + "\n";
    }
}
