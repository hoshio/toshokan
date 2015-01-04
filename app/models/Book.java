package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
@Table(name="books")
public class Book extends Model {

	@Id
	public Long id;	

	@Required
	public String isbn_code;
	
	public String book_name;
	
	public String publisher;
	
	public String author;
	
	public String image;
	
	@ManyToOne(cascade = CascadeType.ALL)
	public User owner;
	
	@ManyToOne(cascade = CascadeType.ALL)
	public User borrower;
	
	@ManyToOne(cascade = CascadeType.ALL)
	public Room room;

	// 0:通常, 1:削除済み
	public String deleteStatus = "0";
	
	protected static Finder<Long, Book> finder =
			new Finder<Long, Book>(Long.class, Book.class);
	
	public static Book find(Long id) {
		return finder.byId(id);
	}
	
	//TODO: そのうち消す
	public static List<Book> findAll() {
		return finder.where().eq("deleteStatus", "0").orderBy("id desc").findList();
	}
	
	public static List<Book> findAll(Long room_id) {
		return finder.where().eq("room_id", room_id).eq("deleteStatus", "0").orderBy("id desc").findList();
	}
	
	public static List<Book> findForDump() {
		return finder.where().orderBy("room_id asc").findList();
	}

	//デバッグ用
	@Override
    public String toString(){
    	return("[id: " + id + ", book_name" + book_name + ", owner: " + owner);
    }
}