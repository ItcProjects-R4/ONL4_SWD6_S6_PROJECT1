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

public class Main {

    // Omar Work
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {

        // Add Users
        users.add(new User("admin","12345678"));
        users.add(new User("faris","password123"));
        users.add(new User("joudy","11111111"));

        System.out.println("Users Loaded Successfully");

    }

}