package controllers;

import static play.data.Form.form;

import java.util.List;

import models.Book;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.reg;
//若干おまじない

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
    		
			Secured.setUserInfo(User.find(username));
    		
    		//フォームオブジェクト生成
    	    Form<Book> f = new Form<Book>(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
			return ok(index.render("登録ありがとうございます", Secured.getUserInfo(), f, books));
    	}
    }
}
