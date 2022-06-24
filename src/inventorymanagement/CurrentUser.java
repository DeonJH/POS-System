package inventorymanagement;

//Class stores the current user in system
public class CurrentUser {
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    
    CurrentUser(String firstname, String lastname, String username,  String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.role = role;
    }
    
    public String getRole() {
        return this.role;
    }
    
    public String getFirstName() {
        return this.firstname;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getLastName() {
        return this.lastname;
    }
}
