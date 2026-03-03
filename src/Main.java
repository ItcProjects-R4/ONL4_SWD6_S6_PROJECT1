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

    // Omar Work
    static ArrayList<User> users = new ArrayList<>();

    // Joudy Work
    public static String Username(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a valid username.");
        String user = input.nextLine();

        if(user.trim().equals("")){
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        else {
            for(User u : users){
                if(u.getName().equals(user)){
                    throw new IllegalArgumentException("Username already exists.");
                }
            }
        }
        return user;
    }

    public static void Password(String name){

        Scanner input = new Scanner(System.in);

        System.out.println("Enter a valid password.");
        String password = input.nextLine().trim();
        if(password.trim().equals("") || password.length() < 8){
            throw new IllegalArgumentException("Password cannot be empty and be more than 8 characters.");
        }
        for(User u : users){
            if(u.getName().equals(name) && u.getPassword().equals(password)){
                System.out.println("Successfully logged in with an existing account.");
            } else {
               users.add(new User(name, password));
                System.out.println("Successfully logged in with a new account.");
            }
        }

    }

    public static void main(String[] args) {

        users.add(new User("admin","12345678"));

        Scanner input = new Scanner(System.in);
        String currentName="";
        String pass="";

        System.out.println("");
        while(true){
            System.out.println("Press enter to start:");
            if(input.nextLine().equals("PRINTALLUSERS")){
                for(User u : users){
                    System.out.println(u);
                }
            }
            try {
                 currentName = Username();
            }
            catch (Exception e){
                System.out.println(e.getMessage() + " Try again.");
                continue;
            }
            try{
                Password(currentName);
            }
            catch (Exception e){
                System.out.println(e.getMessage() + " \nTry again.");
                continue;
            }
        }

    }
}