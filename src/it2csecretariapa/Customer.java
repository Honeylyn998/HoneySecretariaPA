
package it2csecretariapa;

import java.util.Scanner;

public class Customer {
     public void cTransaction() {
        Scanner sc = new Scanner(System.in);
        String response;
        do {
            System.out.println("------------------------------");
             System.out.println("CUSTOMER PANEL");
            System.out.println("1. ADD CUSTOMER");
            System.out.println("2. VIEW CUSTOMER");
            System.out.println("3. UPDATE CUSTOMER");
            System.out.println("4. DELETE CUSTOMER");
            System.out.println("5. EXIT");
            System.out.println("ENTER SELECTION");

            int act = sc.nextInt();
            Customer cs = new Customer();

            if (act < 1 || act > 5) {
                System.out.println("Invalid action. Please try again.");
            } else {
                switch (act) {
                    case 1:
                        cs.addCustomer();
                        cs.viewCustomer();
                        break;

                    case 2:
                        cs.viewCustomer();
                        break;

                    case 3: cs.viewCustomer();
                        cs.updateCustomer();
                        break;

                    case 4: cs.viewCustomer();
                            cs.DeleteCustomer();
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        break;
                }
            }
            System.out.println("Do You Want to Continue? (yes/no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    public void addCustomer() {
        Scanner sc = new Scanner(System.in);

        String c_fname = getValidNameInput("Customer FirstName: ");
        String c_lname = getValidNameInput("Customer LastName: ");
        String c_address = getValidInput("Customer Address: ");
        
        String c_phonenum = getValidPhoneNumberInput("Customer Phonenum: ");

        String sql = "INSERT INTO customer (c_fname, c_lname, c_address, c_phonenum) VALUES (?, ?, ?, ?)";
        Config conf = new Config();
        conf.addRecord(sql, c_fname, c_lname, c_address, c_phonenum);
    }

    public void viewCustomer() {
        String cqry = "SELECT * FROM customer";
        String[] hrds = {"ID", "FirstName", "LastName", "Address", "Phonenum"};
        String[] clmns = {"c_id", "c_fname", "c_lname", "c_address", "c_phonenum"};
        Config conf = new Config();
        conf.viewRecord(cqry, hrds, clmns);
    }

    private void updateCustomer() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Customer ID: ");
        int c_id = sc.nextInt();
        if (!isValidCustomerID(c_id)) {
            System.out.println("Invalid Customer ID.");
            return;
        }

        sc.nextLine();  

        String c_fname = getValidNameInput("Enter New Customer FirstName: ");
        String c_lname = getValidNameInput("Enter New Customer LastName: ");
        String c_address = getValidInput("Enter New Customer Address: ");
        
        String c_phonenum = getValidPhoneNumberInput("Enter New Customer Phonenum: ");

        String qry = "UPDATE customer SET c_fname = ?, c_lname = ?, c_address = ?, c_phonenum = ? WHERE c_id = ?";
        Config conf = new Config();
        conf.updateRecord(qry, c_fname, c_lname, c_address, c_phonenum, c_id);
    }

    public void DeleteCustomer() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Customer ID to Delete: ");
        int id = sc.nextInt();

        if (!isValidCustomerID(id)) {
            System.out.println("Invalid Customer ID.");
            return;
        }

        String sqlDelete = "DELETE FROM customer WHERE c_id = ?";
        Config conf = new Config();
        conf.deleteRecord(sqlDelete, id);
    }

      private String getValidInput(String prompt) {
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Please enter a valid value.");
            }
        } while (input.isEmpty());
        return input;
    }

   
    private String getValidPhoneNumberInput(String prompt) {
        Scanner sc = new Scanner(System.in);
        String phone;
        do {
            System.out.print(prompt);
            phone = sc.nextLine();
            if (!isValidPhoneNumber(phone)) {
                System.out.println("Invalid phone number. Please enter a valid 10-digit number.");
            }
        } while (!isValidPhoneNumber(phone));
        return phone;
    }

  
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

   
    private boolean isValidCustomerID(int id) {
        String sqlCheck = "SELECT COUNT(*) FROM customer WHERE c_id = ?";
        Config conf = new Config();
        return conf.exists(sqlCheck, id);
    }

   
    private String getValidNameInput(String prompt) {
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Please enter a valid name.");
            } else if (!input.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid name. Name cannot contain numbers or special characters.");
            }
        } while (input.isEmpty() || !input.matches("[a-zA-Z ]+"));
        return input;
    }
}
    

