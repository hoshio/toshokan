package models;
 
import java.util.Date;
 
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
 
import play.db.ebean.Model;
 
import com.avaje.ebean.annotation.CreatedTimestamp;
 
@Entity
public class Library extends Model {

	@Id
	public int id;
	public int room_id;
	public String isbn_code	
	public String book_name	
	public String publisher	
	public String owner	
	public String state	
	public String delete_flg	
	public String borrower
	
    }
}