package com.librarymanagement.librarymanagementassignment1;

import java.io.*;
import javafx.scene.control.Alert;

public class FileManagement {


    public void readFile(String filename, Library libObject) {
        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            line = br.readLine(); // Skip header if there's one
            while ((line = br.readLine()) != null) {
                String[] text = line.split(","); // Split by comma
                String title = text[0];
                String author = text[1];
                String isbn = text[2];
                Boolean status = Boolean.parseBoolean(text[3]);
                String name = text[4];
                String borrowId = text.length > 5 ? text[5] : null; // Handle cases where borrowId might be missing

                // Add the book to the library
                Book book = libObject.addBook(title, author, isbn, status, name);

                // If borrowId exists, set it for the book and add it to the library's borrowId set
                if (borrowId != null && !borrowId.isEmpty()) {
                    book.setBorrowId(borrowId);
                    libObject.addBorrowId(borrowId); // Add the borrow ID to the set
                }
            }
            br.close();
        } catch (Exception e) {
            // Show an alert dialog in case of error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Read Error");
            alert.setHeaderText("Unable to Read File");
            alert.setContentText("An error occurred while trying to read the file:\n" + e.getMessage());
            alert.showAndWait(); // wait for user response.
        }
    }


    public void writeFile(String filename, Library libObject) {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            OutputStreamWriter output = new OutputStreamWriter(file);
            BufferedWriter writer = new BufferedWriter(output);

            writer.write("Title,Author,ISBN,Status,BorrowerName"); // Add header
            writer.newLine();

            for (Book book : libObject.getAllBooks()) {
                String line = (book.getBookTitle() + "," + book.getBookAuthor() + "," +
                        book.getBookISBN() + "," + book.getIsAvailable() + "," + book.getBorrowerName() + "," + book.getBorrowId());
                writer.write(line);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

