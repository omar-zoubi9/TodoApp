package inputManaging;

import UserManagement.PassManager;
import UserManagement.UserDetails;

import java.util.Scanner;

public class InputManager {
    private Scanner scanner = new Scanner(System.in);
    private PassManager passManager = new PassManager();

    public UserDetails getUserDetails(boolean isNewUser){
        boolean passIsWeak = true;
        String pass;
        String newUserAddString;
        if(isNewUser){
            newUserAddString = "new ";
        }else{
            newUserAddString = "";
        }

        UserDetails user = new UserDetails();
        System.out.println("please enter your " + newUserAddString + "username: ");
        user.setUserName(scanner.nextLine());
        while(passIsWeak){
            System.out.println("please enter your password, it should have 3 letters, 3 numbers, a special char, a small letter and a capital letter:");
            pass = scanner.nextLine();
            if(passManager.isPassStrong(pass)) {
                passIsWeak = false;
                user.setPassword(pass);
            }
        }
        return user;
    }
    public UserDetails queryUserToInputNewDetails(){
        return getUserDetails(true);
    }

    public UserDetails queryUserToInputHisDetails(){
        return getUserDetails(false);
    }
}
