package com.library;

import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Exit");
            System.out.println("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> service.addBook();
                case 2 -> service.viewAllBooks();
                case 3 -> service.issueBook();
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }

                default -> System.out.println("Invalid option. Try again.");
            }

        }
    }
}
