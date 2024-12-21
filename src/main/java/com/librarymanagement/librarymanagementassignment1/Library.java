package com.librarymanagement.librarymanagementassignment1;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Library {
    private ArrayList<Book> bookList; // ArrayList object

    public Library() {
        this.bookList = new ArrayList<>(); // Constructor
    }

    public ArrayList<Book> getAllBooks() {
        return this.bookList; // Return all books in list
    }
    private Set<String> borrowIds = new HashSet<>(); // Track all generated borrow IDs

    private String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                result.append(words[i].substring(0, 1).toUpperCase())
                        .append(words[i].substring(1).toLowerCase());
                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }
        public boolean isBorrowIdUnique(String id) {
        return !borrowIds.contains(id); // Check if the ID is unique
        }

        public void addBorrowId(String id) {
            borrowIds.add(id); // Add the ID to the set
        }

        public void removeBorrowId(String id) {
            borrowIds.remove(id); // Remove the ID when a book is returned
        }


    // Check if the title or ISBN already exists in the library,isbn or title must be unique
    public boolean isTitleOrISBNExist(String title, String isbn) {
        for (Book book : getAllBooks()) {
            if (book.getBookTitle().equalsIgnoreCase(title) || book.getBookISBN().equals(isbn)) {
                return true; // Found duplicate
            }
        }
        return false; // No duplicate found
    }

    // Adding book method
    public Book addBook(String title, String author, String isbn, Boolean isAvailable, String borrowerName) {
        title = capitalizeWords(title);
        author = capitalizeWords(author);

        Book newBook = new Book(title, author, isbn, isAvailable, borrowerName);
        this.bookList.add(newBook);
        return newBook;
        //return "Book successfully added: Title: " + title + ", Author: " + author + ", ISBN: " + isbn;
    }



    // Search by title
    public String titleSearch(String title) {
        String result = "";
        boolean found = false;

        for (Book book : bookList) {
            if (book.getBookTitle().equalsIgnoreCase(title)) {
                result = "Book with title: " + book.getBookTitle() + " found!";
                found = true;
                break; // Return early since book titles are unique.
            }
        }

        if (!found) {
            result = "No book found with the title: " + title + "\n";
        }

        return result;
    }

    // Author search
    public String authorSearch(String author) {
        String result = "";
        boolean found = false;

        for (Book book : bookList) {
            if (book.getBookAuthor().equalsIgnoreCase(author)) {
                result = "Book by author: " + book.getBookAuthor() + " found!";
                found = true;
                break;
            }
        }

        if (!found) {
            result = "No books found by the author: " + author + "\n";
        }

        return result;
    }

    // ISBN search
    public String isbnSearch(String isbn) {
        String result = "";
        boolean found = false;

        for (Book book : bookList) {
            if (book.getBookISBN().equals(isbn)) {
                result = "Book with ISBN: " + book.getBookISBN() + " found!";
                found = true;
            }
        }

        if (!found) {
            result = "No book found with the ISBN: " + isbn + "\n";
        }

        return result;
    }

    // Display all books
    public String displayAllBooks() {
        String result = "All Books in the Library:\n";

        if (bookList.isEmpty()) {
            return "No books available in the library.";
        }

        for (Book book : bookList) {
            result += book.displayBook() + "\n";
        }

        return result;
    }
}


