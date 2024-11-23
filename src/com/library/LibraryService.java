package com.library;

import java.sql.*;
import java.util.Scanner;

public class LibraryService {

    public void  addBook() {
        try (Connection conn = DB_Connect.getConnection();
        Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Book author: ");
            String author = scanner.nextLine();
            System.out.print("Enter Book Genre: ");
            String genre = scanner.nextLine();

            String query = "INSERT INTO books (title, author, genre) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, genre);
            stmt.executeUpdate();
            System.out.println("Book Added Successfully!");

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewAllBooks() {
        try (Connection conn = DB_Connect.getConnection()){
            String query = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Books in the library: ");

            while (rs.next()) {
               System.out.println(rs.getInt("id") + "|" + rs.getString("title") + "|"
                       + rs.getString("author") + "|" + rs.getString("genre") + "| Issued: "
                       + rs.getString("is_issued"));
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void issueBook() {
        try (Connection conn = DB_Connect.getConnection();
        Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter book ID to issue: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();

            //check if the book is available
            String checkQuery = "SELECT is_issued FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("is_issued")) {
                System.out.println("Book is already issued!");
                return;
            }

            //issue the book
            String issueQuery = "INSERT INTO issued_books (book_id, user_id, issue_date) VALUES (?, ?, CURDATE())";
            PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
            issueStmt.setInt(1, bookId);
            issueStmt.setInt(2, userId);
            issueStmt.executeUpdate();

            //Update book status
            String updateQuery = "UPDATE books SET is_issued = TRUE WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            System.out.println("Book issued successfully!");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
