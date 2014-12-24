package models;

import java.io.Serializable;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "users")
public class User extends Model implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	
	@Constraints.Required
	public String username;
	
	@Constraints.Email
	public String email;
	
	@Constraints.Required
	public String password;
	
	protected static Finder<Long, User> finder = 
			new Finder<Long,User>(Long.class, User.class);
	
	public static void create(String username, String email, String password) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.save();
    }
	
	public static Boolean authenticate(String name, String password) {
		return (finder.where().eq("username", name).eq("password", password).findUnique() != null);
	}
	
	public static User find(String name) {
		return finder.where().eq("username", name).findUnique();
	}
	
	public static User find(String name, String email) {
		return finder.where().eq("username", name).eq("email", email).findUnique();
	}
	
	@Override
	public String toString() {
		return String.join(",", id.toString(), username, email, password);
	}
}
