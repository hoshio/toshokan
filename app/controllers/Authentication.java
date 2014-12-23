package controllers;

import java.util.*;
//modelsパッケージ使うよね
import models.*;
import views.html.*;
//若干おまじない
import play.*;
import play.data.*;
import static play.data.Form.*;
import play.mvc.*;
import play.cache.Cache;

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

    			return ok(index.render((String)Cache.get("login.key"), f, books));
    		} else {
        		return badRequest(views.html.login.render("Invalid username or password", form));
    		}
    	}
    }
}
