package com.library;

import java.sql.*;
import java.util.Scanner;

public class LibraryService {

    // Add a new book to the library
    public void addBook() {
        try (Connection conn = DB_Connect.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter book author: ");
            String author = scanner.nextLine();
            System.out.print("Enter book genre: ");
            String genre = scanner.nextLine();

            String query = "INSERT INTO books (title, author, genre) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, genre);

            stmt.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            System.out.println("Error: Unable to add the book.");
            e.printStackTrace();
        }
    }

    // View all books in the library
    public void viewAllBooks() {
        try (Connection conn = DB_Connect.getConnection()) {
            String query = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nID | Title                      | Author            | Genre             | Issued");
            System.out.println("----------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%2d | %-25s | %-15s | %-15s | %s\n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getBoolean("is_issued") ? "Yes" : "No");
            }

        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve books.");
            e.printStackTrace();
        }
    }

    // Issue a book to a user
    public void issueBook() {
        try (Connection conn = DB_Connect.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter the Book ID to issue: ");
            int bookId = scanner.nextInt();
            System.out.print("Enter the User ID: ");
            int userId = scanner.nextInt();

            // Check if the book is already issued
            String checkQuery = "SELECT is_issued FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getBoolean("is_issued")) {
                System.out.println("Book is already issued!");
                return;
            }

            // Issue the book
            String issueQuery = "INSERT INTO issued_books (book_id, user_id, issue_date) VALUES (?, ?, CURDATE())";
            PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
            issueStmt.setInt(1, bookId);
            issueStmt.setInt(2, userId);
            issueStmt.executeUpdate();

            // Update book status to issued
            String updateQuery = "UPDATE books SET is_issued = TRUE WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            System.out.println("Book issued successfully!");

        } catch (SQLException e) {
            System.out.println("Error: Unable to issue the book.");
            e.printStackTrace();
        }
    }

    // Return a book
    public void returnBook() {
        try (Connection conn = DB_Connect.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter the Book ID to return: ");
            int bookId = scanner.nextInt();

            // Check if the book is actually issued
            String checkQuery = "SELECT is_issued FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && !rs.getBoolean("is_issued")) {
                System.out.println("Book is not issued, cannot return.");
                return;
            }

            // Update issued_books table to mark the return
            String returnQuery = "UPDATE issued_books SET return_date = CURDATE() WHERE book_id = ? AND return_date IS NULL";
            PreparedStatement returnStmt = conn.prepareStatement(returnQuery);
            returnStmt.setInt(1, bookId);
            int rowsUpdated = returnStmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Error: No active record found for this book. It might already be returned.");
                return;
            }

            // Update book status to not issued
            String updateQuery = "UPDATE books SET is_issued = FALSE WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            System.out.println("Book returned successfully!");

        } catch (SQLException e) {
            System.out.println("Error: Unable to return the book.");
            e.printStackTrace();
        }
    }
}

