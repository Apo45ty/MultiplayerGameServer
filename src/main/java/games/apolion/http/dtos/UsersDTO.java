package games.apolion.http.dtos;

import javax.persistence.Id;

public class UsersDTO {

    private String username;
    private String passwordhash;
    @Id
    private String email;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsersDTO(String username, String passwordhash, String email) {
        super();
        this.username = username;
        this.passwordhash = passwordhash;
        this.email = email;
    }

    public UsersDTO(String username, String email) {
        super();
        this.username = username;
        this.email = email;
    }

    public UsersDTO(String username) {
        super();
        this.username = username;
    }
    public UsersDTO() {
        super();
    }
}
