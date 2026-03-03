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
    public static String Username(String name){

        Scanner input = new Scanner(System.in);

        System.out.println("Enter a valid username.");
        name = input.nextLine();

        return name;
    }

    public static String Password(String pass){

        Scanner input = new Scanner(System.in);

        System.out.println("Enter a valid password.");
        pass = input.nextLine();

        return pass;
    }

    public static void main(String[] args) {

        users.add(new User("admin","12345678"));

        String name="";
        String pass="";

        name = Username(name);
        pass = Password(pass);

    }
}