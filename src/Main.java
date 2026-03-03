/*
o username
o password  :validation يعملوا .
        2
o username  مش فاضي
o password >= 8  لو البيانات غلط  Exception يعملوا .
3 : يستخدموا .
4
o Conditions
o Methods                      Joudy
o Exception Handling           Faris
o ArrayList لتخزين users       Omar
*/
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner input;

    static {
        input = new Scanner(System.in);
    }

    // Omar Work
    static ArrayList<User> users = new ArrayList<>();

    // Joudy Work
    public static String Username(){
        System.out.println("Enter a valid username.");
        String user = input.nextLine();

        if(user.trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        else
            return user;
    }

    public static void Password(String name){
        System.out.println("Enter a valid password.");
        String password = input.nextLine().trim();
        if(password.trim().isEmpty() || password.length() < 8){
            throw new IllegalArgumentException("Password cannot be empty and be more than 8 characters.");
        }
        boolean accountExists = false;
        for(User u : users){
            if(u.getName().equals(name) && u.getPassword().equals(password)){
                System.out.println("Successfully logged in with an existing account.");
                accountExists = true;
                break;
            }
        }
        if(!accountExists){
            users.add(new User(name, password));
            System.out.println("Successfully logged in with a new account.");
        }

    }

     static void main() {

        users.add(new User("admin","12345678"));

        String currentName;


        while(true){
            System.out.println("\n\nPress enter to start:\n");
            if(input.nextLine().equals("PRINTALLUSERS")){
                for(User u : users){
                    System.out.println(u);
                }
            }
            try {
                 currentName = Username();
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage() + " Try again.");
                continue;
            }
            try{
                Password(currentName);
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage() + " \nTry again.");
                //continue;
            }
        }

    }
}