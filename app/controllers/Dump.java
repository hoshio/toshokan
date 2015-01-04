package controllers;

import static play.data.Form.form;

import java.util.Arrays;

import models.Book;
import models.Room;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.dump;

public class Dump extends Controller {
	public static Result init() {
		return ok(dump.render(new Form<Book>(Book.class), Book.findForDump(), User.findForDump(), Room.findAll()));
	}
	
	public static Result register() {
		Form<Book> f = form(Book.class).bindFromRequest();
		
		if (f.hasErrors()) return redirect("/dump");
		
		String[] cds = f.get().isbn_code.split(",");
		Arrays.asList(cds).forEach(cd -> save(cd.trim()));
		return redirect("/dump");
	}
	
	private static void save(String cd) {
		Book book = new Book();
		book.isbn_code = cd;
		try {
			ItemLookup.setBookInf(book);
		} catch (RuntimeException e) {
			Logger.debug("ItemLookUp失敗. ISBN-CD: " + cd, e);
			return;
		}
		User user = Secured.getUserInfo();
		if (user == null) {
			user = User.findForDump().get(0);
		}
		book.room = Room.findAll().get(0);
		book.owner = user;
		book.save();
	}
}
