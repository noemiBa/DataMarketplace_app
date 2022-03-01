package myapp;

import org.springframework.stereotype.Controller;

@Controller
public class activeUser {
    private static activeUser activeUserInstance = null;
    private Users activeUser = null;
    private activeUser() { }
    public static activeUser getInstance() {
        if(activeUserInstance == null) {
            activeUserInstance = new activeUser();
        }
        return activeUserInstance;
    }

    public Users getActiveUser() { return activeUser; }

    public void loginUser(Users user){
        activeUser = user;
        System.out.println(this.toString());
    }

    public void logoutUser() {
        activeUser = null;
        System.out.println(this.toString());
    }

    public String toString() {
        if(activeUser == null) {
            return "No user is logged in.";
        }
        return "!! The Active user is: " + activeUser.getUsername() + ". Is Admin: " + activeUser.isAdmin() + "!!";
    }
}
