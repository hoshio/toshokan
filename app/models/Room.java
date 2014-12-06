package models;

import java.util.*;

import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;
 
@Entity
public class Room extends Model {
 
    @Id
    public Long id;
    
    @Required
    public String room_name;

    public static Finder<Long, Room> find =
       new Finder<Long, Room>(Long.class, Room.class);

}