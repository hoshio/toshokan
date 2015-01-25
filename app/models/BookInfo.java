package models;

import java.util.HashMap;
import java.util.Map;

import play.db.ebean.Model;
import controllers.ItemLookup;

public class BookInfo extends Model {

	public static String author;
	
	public static String bookName;
	
	public static String publisher;
	
	public static String imageUrl;
	
	public static String amazonUrl;
	
	public static void findByCode(String isbn) {
		
		Map<String, String> outMap = new HashMap<>();
		outMap = ItemLookup.setBookInf(isbn);
		bookName = outMap.get("bookName");
		author = outMap.get("author");
		publisher = outMap.get("publisher");
		imageUrl = outMap.get("imageUrl");
		amazonUrl = outMap.get("amazonURL");	
	}
}