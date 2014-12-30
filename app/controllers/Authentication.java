package controllers;

import static play.data.Form.form;

import java.util.List;

import models.Book;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;
//若干おまじない

public class Authentication extends Controller {

	/*
	 * loginページ表示
	 */
    public static Result login(){
    	return ok(login.render("Welcome!",form(User.class)));
    }

    /*
     * login成功時にcacheにlogin情報をセットする
     */
    public static Result authenticate(){
    	Form<User> form = form(User.class).bindFromRequest();
    	if(form.hasErrors()){
    		return badRequest(views.html.login.render("Value is required", form));
    	} else {
    		//formからusernameとpasswordを取得
    		User user = form.get();
    		String username = user.username;
    		String password = user.password;

    		//login処理
    		if(User.authenticate(username, password)){
    			//セッション作成
    			Secured.createSession(username);
    			//セッションにユーザーをブチ込む
    			Secured.setUserInfo(User.find(username));
    			//フォームオブジェクト生成
    			Form<Book> f = new Form<Book>(Book.class);
        	    //本一覧取得
        	    List<Book> books = Book.findAll();        

    			return ok(index.render(null, Secured.getUserInfo(), f, books));
    		} else {
        		return badRequest(login.render("Invalid username or password", form));
    		}
    	}
    }
}
