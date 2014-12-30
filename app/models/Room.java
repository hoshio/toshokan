package models;

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
    
//    @OneToMany(cascade = CascadeType.ALL)
//    public List<Book> books;

    //controllersにfinderを触らせるのはやめよう！ということでprotected
    protected static Finder<Long, Room> finder =
       new Finder<Long, Room>(Long.class, Room.class);

 }