package controllers;

import java.util.*;
//modelsパッケージ使うよね
import models.*;
import views.html.*;
//若干おまじない
import play.*;
import play.cache.Cache;
import play.data.*;
import static play.data.Form.*;
import play.mvc.*;

public class Registration extends Controller {

    /*
     * ユーザー登録画面へ遷移
     */
    public static Result regPage(){
    	return ok(reg.render(form(User.class)));
    }
    
    /*
     * 新規ユーザー登録
     */
    public static Result register() {
    	Form<User> form = form(User.class).bindFromRequest();
    	if(form.hasErrors()) {
    		return badRequest(views.html.reg.render(form));
    	} else {
    		User user = form.get();
    		String username = user.username;
    		String email = user.email;
    		String password = user.password;
    		
    		//ユーザー名のユニークチェックが必要。すごく雑に実装します
    		if (User.find(username) != null) {
    			//登録済みのユーザー
    			return badRequest(reg.render(form));
    		}
    		
    		User newUser = User.find(username, email);
    		if (newUser == null) {
    			//新規者insert
    			User.create(username, email, password);
        		//セッション作成
    			Secured.createSession(username);
    		} else {
        		return badRequest(views.html.reg.render(form));
    		}
    		
    		//フォームオブジェクト生成
    	    Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
			return ok(index.render((String)Cache.get("login.key"),f,books));
    	}
    }
}
