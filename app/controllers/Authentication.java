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


public class Authentication extends Controller {

	//loginページ表示
    public static Result login(){
    	return ok(login.render("Welcome!",form(Admin.class)));
    }
    
    public static Result authenticate(){
    	Form<Admin> loginForm = form(Admin.class).bindFromRequest();
    	if(loginForm.hasErrors()){
    		return badRequest(views.html.login.render("Value is required", loginForm));
    	}else{
    		String username = loginForm.get().getUsername();
    		String password = loginForm.get().getPassword();
    		if( Admin.authenticate(username, password) == true ){
    			// sessionに値をセット
    			session("username", username);
    			// sessionにセットした値を取得
    			String user = session("username");
    			//フォームオブジェクト生成
    			Form<Book> f = new Form(Book.class);
    			//本一覧を取得
    			List<Book> books = Book.find.all();

    			return ok(index.render(user,f,books));
    		} else {
        		return badRequest(views.html.login.render("Wrong username or password", loginForm));
    		}
    	}
    }
}
