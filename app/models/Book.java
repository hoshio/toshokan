package models;

import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;
 
@Entity
public class Book extends Model {
 
    @Id
    public Long id;
    
    @Required
    public String book_name;

    @Required
    public String owner_name;

    public String borrower;

	//0:貸出可, 1:貸出不可
	public String bookStatus="0";

	//0:通常, 1:削除済み
	public String deleteStatus="0";
    public static Finder<Long, Book> find =
       new Finder<Long, Book>(Long.class, Book.class);

}