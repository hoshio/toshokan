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

    public String delete_flg;

    public String borrower;

    public static Finder<Long, Book> find =
       new Finder<Long, Book>(Long.class, Book.class);

}