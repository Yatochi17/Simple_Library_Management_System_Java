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
}
