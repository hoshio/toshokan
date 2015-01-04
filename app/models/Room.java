package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
 
@Entity
@Table(name = "rooms")
public class Room extends Model {
 
	private static final long serialVersionUID = 1L;

	@Id
    public Long id;
    
    @Required
    public String room_name;
    
    @ManyToOne(cascade = CascadeType.ALL)
    public User create_user;

    //controllersにfinderを触らせるのはやめよう！ということでprotected
    protected static Finder<Long, Room> finder =
       new Finder<Long, Room>(Long.class, Room.class);
    
    public static Room findById(Long id) {
    	return finder.byId(id);
    }
    
    public static List<Room> findAll() {
    	return finder.all();
    }
    
    public String toString() {
    	return String.join(id + "," + room_name + "," + create_user.id);
    }

 }