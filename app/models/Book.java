package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Book extends Model {

	@Id
	public Long id;

	@Required
	public String book_name;

	@Required
	public String owner_name;

	public String borrower;

	// 0:貸出可, 1:貸出不可
	public String bookStatus = "0";

	// 0:通常, 1:削除済み
	public String deleteStatus = "0";
	public static Finder<Long, Book> find = new Finder<Long, Book>(Long.class,
			Book.class);

	@Override
    public String toString(){
    	return("[id"+id+",book_name"+book_name+",owner_name"+owner_name+",borrower"+borrower+""
    			+ ",bookStatus"+bookStatus+",deleteStatus"+deleteStatus+"]");
    }
}