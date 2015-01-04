package controllers;

import static play.data.Form.form;

import models.Room;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.room;

public class Roomlist extends Controller {
	
	/**
	 * 部屋一覧画面初期表示
	 * @return
	 */
	@Security.Authenticated(Secured.class)
	public static Result init() {
		return ok(room.render(Room.findAll(), new Form<Room>(Room.class)));
	}
	
	/**
	 * 部屋新規登録
	 * @return
	 */
	@Security.Authenticated(Secured.class)
	public static Result register() {
		Form<Room> f = form(Room.class).bindFromRequest();
		if (f.hasErrors()) {
			return init();
		}
		Room room = f.get();
		room.create_user = Secured.getUserInfo();
		room.save();
		return init();
	}
	
}
