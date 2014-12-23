package models;

import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;
 
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
    
    @OneToMany(cascade = CascadeType.ALL)
    public List<Book> books;

    //controllersにfinderを触らせるのはやめよう！ということでprotected
    protected static Finder<Long, Room> finder =
       new Finder<Long, Room>(Long.class, Room.class);

 }