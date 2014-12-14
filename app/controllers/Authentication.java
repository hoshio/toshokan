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
    	return ok(login.render("Welcome!",form(Admin.class)));
    }
    

    /*
     * login成功時にcacheにlogin情報をセットする
     */
    public static Result authenticate(){
    	Form<Admin> loginForm = form(Admin.class).bindFromRequest();
    	if(loginForm.hasErrors()){
    		return badRequest(views.html.login.render("Value is required", loginForm));
    	}else{
    		//formからusernameとpasswordを取得
    		String username = loginForm.get().getUsername();
    		String password = loginForm.get().getPassword();

    		//login処理
    		if( Admin.authenticate(username, password) == true ){
    			//セッション作成
    			Secured.createSession(username);
    			//フォームオブジェクト生成
    			Form<Book> f = new Form(Book.class);
        	    //本一覧取得
        	    List<Book> books = CommonUtility.findBookList();        

    			return ok(index.render((String)Cache.get("login.key"),f,books));
    		} else {
        		return badRequest(views.html.login.render("Invalid username or password", loginForm));
    		}
    	}
    }
}
