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
    	return ok(reg.render(form(Admin.class)));
    }
    
    /*
     * 新規ユーザー登録
     */
    public static Result register() {
    	Form<Admin> regForm = form(Admin.class).bindFromRequest();
    	if(regForm.hasErrors()){
    		return badRequest(views.html.reg.render(regForm));
    	}else{
    		String username = regForm.get().getUsername();
    		String email = regForm.get().getEmail();
    		String password = regForm.get().getPassword();
    		
    		Admin new_user = Admin.find.where().eq("username", username).eq("email", email).findUnique();
    		if (new_user == null) {
    			//新規者insert
    			Admin.create(username, email, password);
        		//セッション作成
    			Secured.createSession(username);
    		} else {
        		return badRequest(views.html.reg.render(regForm));
    		}
    		
    		//フォームオブジェクト生成
    	    Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        
			return ok(index.render((String)Cache.get("login.key"),f,books));
    	}
    }
}
