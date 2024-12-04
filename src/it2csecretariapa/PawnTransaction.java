
package it2csecretariapa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

 
public class PawnTransaction {
       public void ptransaction (){ 
        Scanner sc = new Scanner(System.in);
        String response;
        do {
            System.out.println("1. ADD PAWNTRANSACTION");
            System.out.println("2. VIEW PAWNTRANSACTION ");
            System.out.println("3. UPDATE PAWNTRANSACTION");      
            System.out.println("4. DELETE PAWNTRANSACTION");
            System.out.println("5. EXIT");
            System.out.println("Enter action");           
            int act = sc.nextInt();
            PawnTransaction pt = new PawnTransaction();
            Customer cs = new Customer();
            ItemPawn ip = new ItemPawn();
            
            switch(act) {
                case 1: 
                    cs.viewCustomer();    
                    pt.addPawnTransaction();
                    pt.viewPawnTransaction();
                    break;
                
                case 2:            
                    pt.viewPawnTransaction();
                    break;
                
                case 3:    
                    ip.viewItemPawn();
                    pt.updatePawnTransaction();
                    pt.viewPawnTransaction();
                    break;
                
                case 4: 
                    pt.viewPawnTransaction();
                    pt.deletePawnTransaction();
                    pt.viewPawnTransaction();
                    break;
                
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid Selection, Please Try Again.");
            }
            
            System.out.println("Continue? (yes/No)");
            response = sc.next();
        } while(response.equalsIgnoreCase("yes"));
    }
 
    private void addPawnTransaction() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in);
        Customer cs = new Customer();

        int c_id = getValidCustomerID("SELECT ID OF THE SELECTED CUSTOMER: ", conf);

        ItemPawn ip = new ItemPawn();
        ip.viewItemPawn();
        int iid = getValidItemID("Enter ID of Item: ", conf);

        
        double quantity = getValidPositiveDoubleInput("Enter Quantity: ");

        String amountqry = "SELECT i_amount FROM itempawn WHERE i_id = ?";
        double amount = conf.getSingleValue(amountqry, iid);
        double due = amount * quantity;

        System.out.println(".......................................................");        
        System.out.println("Total DUE: " + due);
        System.out.println(".......................................................");

       
        double ramount = getValidDoubleInput("Enter Amount Received: ", due);

        System.out.println("Cash Received: " + ramount);
        double change = ramount - due;
        System.out.println("Change: " + change);

        // Get current date
        LocalDate currdate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy/MM/dd");
        String date = currdate.format(format);

        String pawntransactionqry = "INSERT INTO pawntransaction (c_id, i_id, p_quantity, p_due, p_ramount, p_change, p_date)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        conf.addRecord(pawntransactionqry, c_id, iid, quantity, due, ramount, change, date);
    }

    public void viewPawnTransaction() {
        String qry = "SELECT p_id, c_lname, i_iname, p_due, p_ramount, p_change, p_date FROM pawntransaction"
                    + " LEFT JOIN customer ON customer.c_id = pawntransaction.c_id"
                    + " LEFT JOIN itempawn ON itempawn.i_id = pawntransaction.i_id";
        
        String[] hrds = {"PawnTransaction ID", "Customer", "ItemPawn", "Total", "Amount Received", "Change", " Renewal Date"};
        String[] clmns = {"p_id", "c_lname", "i_iname", "p_due", "p_ramount", "p_change", "p_date"};
        
        Config conf = new Config();
        conf.viewRecord(qry, hrds, clmns);
    }

    private void updatePawnTransaction() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in);

        int p_id = getValidPawnTransactionID("Enter PawnTransaction ID to Update: ", conf);

      
        double quantity = getValidPositiveDoubleInput("Enter New Quantity: ");
        
       
        String amountqry = "SELECT i_amount FROM itempawn WHERE i_id = (SELECT i_id FROM pawntransaction WHERE p_id = ?)";
        double amount = conf.getSingleValue(amountqry, p_id);
        double due = amount * quantity;
        
        System.out.println("New Total Due: " + due);
        
       
        double ramount = getValidDoubleInput("Enter New Amount Received: ", due);
        
        String updateqry = "UPDATE pawntransaction SET p_quantity = ?, p_due = ?, p_ramount = ? WHERE p_id = ?";
        conf.updateRecord(updateqry, quantity, due, ramount, p_id);
        
        System.out.println("PawnTransaction Updated Successfully!");
    }

    private void deletePawnTransaction() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in);

        int p_id = getValidPawnTransactionID("Enter PawnTransaction ID to delete: ", conf);

        String deleteqry = "DELETE FROM pawntransaction WHERE p_id = ?";
        conf.deleteRecord(deleteqry, p_id);

        System.out.println("PawnTransaction Deleted Successfully!");
    }

   
    private int getValidCustomerID(String prompt, Config conf) {
        int c_id = -1;
        Scanner sc = new Scanner(System.in);
        while (c_id <= 0) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                c_id = sc.nextInt();
                if (c_id <= 0 || conf.getSingleValue("SELECT c_id FROM customer WHERE c_id = ?", c_id) == 0) {
                    System.out.println("Invalid customer ID. Please try again.");
                    c_id = -1; 
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.next(); 
            }
        }
        return c_id;
    }

    
    private int getValidItemID(String prompt, Config conf) {
        int iid = -1;
        Scanner sc = new Scanner(System.in);
        while (iid <= 0) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                iid = sc.nextInt();
                if (iid <= 0 || conf.getSingleValue("SELECT i_id FROM itempawn WHERE i_id = ?", iid) == 0) {
                    System.out.println("Invalid item ID. Please try again.");
                    iid = -1; 
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.next();
            }
        }
        return iid;
    }

   
    private int getValidPawnTransactionID(String prompt, Config conf) {
        int p_id = -1;
        Scanner sc = new Scanner(System.in);
        while (p_id <= 0) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                p_id = sc.nextInt();
                if (p_id <= 0 || conf.getSingleValue("SELECT p_id FROM pawntransaction WHERE p_id = ?", p_id) == 0) {
                    System.out.println("Invalid PawnTransaction ID. Please try again.");
                    p_id = -1; 
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.next(); 
            }
        }
        return p_id;
    }

   
    private double getValidPositiveDoubleInput(String prompt) {
        double value = -1;
        Scanner sc = new Scanner(System.in);
        while (value <= 0) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                if (value <= 0) {
                    System.out.println("Please enter a positive value.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.next();  
            }
        }
        return value;
    }


    private double getValidDoubleInput(String prompt, double due) {
        double value = -1;
        Scanner sc = new Scanner(System.in);
        while (value < due) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                if (value < due) {
                    System.out.println("Amount received must be greater than or equal to the total due.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.next();  
            }
        }
        return value;
    }
}
    

