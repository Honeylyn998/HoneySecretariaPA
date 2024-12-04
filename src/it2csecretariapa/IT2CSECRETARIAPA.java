
package it2csecretariapa;

import java.util.Scanner;


public class IT2CSECRETARIAPA {

   
    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
        String response;
        
        do{
            System.out.println("HOLA! WELCOME TO PAWNSHOP APP!");
            System.out.println("1. Customer:");
            System.out.println("2. ItemPawn:");
            System.out.println("3. PawnTransaction:");
            System.out.println("4. Reports:");
            System.out.println("5. Exit:");
            
            System.out.print("Enter Action: ");
            int action = sc.nextInt();
            
            switch (action){
                case 1:
                         Customer cs = new Customer();
                         cs.cTransaction();
                    break;
                    
                case 2:
                            ItemPawn ip = new ItemPawn();
                            ip.Itransaction();
                    break;
                    
                    case 3:
                              PawnTransaction pt = new PawnTransaction();
                              pt.ptransaction();
                    break;
                    
                    case 4:     Reports reports = new Reports();
                                reports.reportMenu();
                    break;
                            
                    
                    
                    
                    case 5: 
                    System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid action. Please Try Again.");
            }
            
            System.out.print("Continue to main menu? (yes/no): ");
            response = sc.next();
            
        } while(response.equalsIgnoreCase("yes"));
        System.out.println("Thank You See You Soon!");
        

        
    }
    
}
       
    
    

