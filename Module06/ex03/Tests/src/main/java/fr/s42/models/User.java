package fr.s42.models;

public class User {
    private Long id;
    private String login;
    private String password;
    private Boolean isAuthenticated;

    public User(Long id, String login, String password, Boolean isAuthenticated){
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
    }

    public Long getId() {
        return id;
    }
    public Boolean isAuthenticated() {
        return isAuthenticated;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id) 
            && login.equals(user.login) 
            && password.equals(user.password) 
            && isAuthenticated.equals(user.isAuthenticated);
    }
    
}
