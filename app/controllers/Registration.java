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


public class Registration extends Controller {

    
	//regページ表示
    public static Result regPage(){
    	return ok(reg.render(form(Admin.class)));
    }
    
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
    			Admin.create(username, email, password);
    		} else {
        		return badRequest(views.html.reg.render(regForm));
    		}
    		
    		// sessionに値をセット
    		session("username", username);
			// sessionにセットした値を取得
			String user = session("username");
	        //フォームオブジェクト生成
    	    Form<Book> f = new Form(Book.class);
    	    //本一覧を取得
    	    List<Book> books = Book.find.all();
        
			return ok(index.render(user,f,books));
    	}
    }
}
