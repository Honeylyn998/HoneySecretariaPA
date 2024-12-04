
package it2csecretariapa;

import java.util.Scanner;

public class Reports {
      // Method to generate the general report
    public void generateGeneralReport() {
        Config conf = new Config();

        String qry = "SELECT "
                   + "SUM(p_due) AS TotalDue, "
                   + "SUM(p_ramount) AS TotalReceived, "
                   + "SUM(p_change) AS TotalChange "
                   + "FROM pawntransaction";

        System.out.println("=========================================");
        System.out.println("General Report: Summary of Transactions");
        System.out.println("=========================================");
        String[] headers = {"Total Due", "Total Received", "Total Change"};
        String[] columns = {"TotalDue", "TotalReceived", "TotalChange"};
        conf.viewRecord(qry, headers, columns);
        System.out.println("-----------------------------------------");
    }

    // Method to generate the customer-specific report
    public void generateCustomerReport() {
    Config conf = new Config();
    Scanner sc = new Scanner(System.in);
    PawnTransaction pt = new PawnTransaction();
    
    // View all pawn transactions (instead of customers)
    pt.viewPawnTransaction();  // This method should list all pawn transactions and their IDs.
    
    System.out.print("Enter Pawn Transaction ID for Individual Reports ");
    int p_id = sc.nextInt();  // Get the Transaction ID

    // Query to fetch the customer details based on the Pawn Transaction ID (p_id)
    String customerQry = "SELECT c.c_id, c.c_fname, c.c_lname, c.c_address, c.c_phonenum, "
                       + "p.p_id, p.i_id, i.i_iname,  i.i_description, p.p_date, p.p_due, p.p_ramount "
                       + "FROM pawntransaction p "
                       + "JOIN customer c ON c.c_id = p.c_id "
                       + "JOIN itempawn i ON i.i_id = p.i_id "
                       + "WHERE p.p_id = ?"; // Using Pawn Transaction ID (p_id)

    // Fields to retrieve from the query
    String[] customerFields = {"c_id", "c_fname", "c_lname", "c_address", "c_phonenum", "p_id", "i_iname","i_description", "p_date", "p_due", "p_ramount"};
    String[] customerDetails = conf.getSingleRecord(customerQry, customerFields, p_id);

    if (customerDetails == null) {
        System.out.println("No transaction found with ID: " + p_id);
        return;
    }

    // Display customer details
    System.out.println("===============================================");
    System.out.println("Individual Report for Customer: " + customerDetails[0]);
    System.out.println("===============================================");
    System.out.println("Name           : " + customerDetails[2] + ", " + customerDetails[1]);
    System.out.println("Contact        : " + customerDetails[4]);
    System.out.println("Address        : " + customerDetails[3]);
    System.out.println("===============================================");

    // Fetch the c_id from the customerDetails array
    int c_id = Integer.parseInt(customerDetails[0]);  // Get the customer ID from the result

    // Query to fetch customer transactions
    String transactionsQry = "SELECT p_id, i_iname, i_description, p_date, p_due, p_ramount "
                           + "FROM pawntransaction "
                           + "LEFT JOIN itempawn ON itempawn.i_id = pawntransaction.i_id "
                           + "WHERE pawntransaction.c_id = ?";
    String[] transactionFields = {"p_id", "i_iname","i_description", "p_date", "p_due", "p_ramount"};
    String[][] transactions = conf.getMultipleRecords(transactionsQry, transactionFields, c_id);

    // Check if transactions were found
    if (transactions == null || transactions.length == 0) {
        System.out.println("No transactions found for Customer ID: " + c_id);
    } else {
        // Display transactions in a table
        System.out.println("Transactions for Customer ID: " + c_id);
        System.out.println("========================================================================================");
        System.out.printf("| %-15s | %-20s | %-20s | %-12s | %-12s | %-12s |\n",
                "Transaction ID", "Item","Description", "Date", "Due", "Amount");
        System.out.println("----------------------------------------------------------------------------------------");

        for (String[] transaction : transactions) {
            System.out.printf("| %-15s | %-20s | %-20s | %-12s | %-12s | %-12s |\n",
                    transaction[0], transaction[1], transaction[2],transaction[3], transaction[4], transaction[5]);
        }

      
   
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("End of Report");
        System.out.println("========================================================================================");
    }
  }
    // Method to generate the item-specific report
    public void generateItemPawnedReport() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Item ID: ");
        int i_id = sc.nextInt();

        String qry = "SELECT p_id, c_lname, i_iname, i_description, p_quantity, p_due, p_ramount, p_change, p_date "
                   + "FROM pawntransaction "
                   + "LEFT JOIN customer ON customer.c_id = pawntransaction.c_id "
                   + "LEFT JOIN itempawn ON itempawn.i_id = pawntransaction.i_id "
                   + "WHERE itempawn.i_id = ?";

        System.out.println("===================================================");
        System.out.println("Specific Report: Details for Item ID " + i_id);
        System.out.println("===================================================");
        String[] headers = {"Transaction ID", "Customer", "Item","Item Description" ,"Quantity", "Due", "Received", "Change", "Date"};
        String[] columns = {"p_id", "c_lname", "i_iname","i_description", "p_quantity", "p_due", "p_ramount", "p_change", "p_date"};
        conf.viewRecord(qry, headers, columns, i_id);
        System.out.println("---------------------------------------------------");
    }

    // Main menu for report generation
    public void reportMenu() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("===================================");
            System.out.println("          REPORT MENU              ");
            System.out.println("===================================");
            System.out.println("1. Generate General Report");
            System.out.println("2. Generate Specific Reports");
            System.out.println("3. Generate Report for a Specific Item");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    generateGeneralReport();
                    break;
                case 2:
                    generateCustomerReport();
                    break;
                case 3:
                    generateItemPawnedReport();
                    break;
                case 4:
                    System.out.println("Exiting Report Generation Panel...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("Do you want to generate another report? (yes/no)");
            response = sc.next();
        } while (response.equalsIgnoreCase("yes"));
    }
}
    

