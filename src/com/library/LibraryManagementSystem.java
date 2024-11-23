package com.library;

import java.util.Scanner;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        Scanner scanner = new Scanner(System.in); // A single Scanner instance for the program

        while (true) {
            try {
                // Display the main menu
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Book");
                System.out.println("2. View All Books");
                System.out.println("3. Issue Book");
                System.out.println("4. Return Book");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                // Read user input
                String input = scanner.nextLine().trim();

                // Validate input is numeric
                if (!isNumeric(input)) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    continue; // Return to the menu
                }

                int choice = Integer.parseInt(input); // Parse input to integer

                // Process user choice
                switch (choice) {
                    case 1 -> {
                        service.addBook();
                        System.out.println("Book added successfully. Returning to the main menu...");
                    }
                    case 2 -> {
                        service.viewAllBooks();
                        System.out.println("Returning to the main menu...");
                    }
                    case 3 -> {
                        service.issueBook();
                        System.out.println("Returning to the main menu...");
                    }
                    case 4 -> {
                        service.returnBook();
                        System.out.println("Returning to the main menu...");
                    }
                    case 5 -> {
                        System.out.println("Exiting... Goodbye!");
                        scanner.close(); // Close the scanner
                        System.exit(0); // Exit the program
                    }
                    default -> System.out.println("Invalid choice. Please select a number between 1 and 5.");
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }

    /**
     * Utility method to check if a string is numeric
     */
    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}