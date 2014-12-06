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

    @play.mvc.Security.Authenticated(models.Secured.class)
    public static Result index_2(){
    	return redirect(routes.Authentication.login());
    }

    @play.mvc.Security.Authenticated(models.Secured.class)
    public static Result logout(){
    	session().clear();
    	return redirect(routes.Authentication.login());
    }

	//loginページ表示
    public static Result login(){
    	return ok(login.render(form(Admin.class)));
    }
    
    public static Result authenticate(){
    	Form<Admin> loginForm = form(Admin.class).bindFromRequest();
    	if(loginForm.hasErrors()){
    		return badRequest(views.html.login.render(loginForm));
    	}else{
    		// sessionに値をセット
    		session("username", loginForm.get().getUsername());
			// sessionにセットした値を取得
			String user = session("username");
	        //フォームオブジェクト生成
    	    Form<Book> f = new Form(Book.class);
    	    //本一覧を取得
    	    List<Book> books = Book.find.all();
        
/*    		String returnUrl = ctx().session().get("returnUrl");
    		if(returnUrl == null || returnUrl.equals("") || returnUrl.equals(routes.Authentication.login().absoluteURL(request()))){
    			returnUrl = routes.Authentication.index_2().url();
    		}
    		
    		return redirect(returnUrl);
*/
			return ok(index.render(user,f,books));
    	}
    }
}
