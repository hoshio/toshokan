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

public class Application extends Controller {

    /*
    * トップページ表示
    */
	@Security.Authenticated(Secured.class)
    public static Result index() {
        //フォームオブジェクト生成
    	Form<Book> f = new Form(Book.class);
	    //本一覧取得
	    List<Book> books = CommonUtility.findBookList();        
        return ok(index.render((String)Cache.get("login.key"), f, books));
	}

	/*
	 * トップページ表示
	 */
    public static Result initview() {
        //フォームオブジェクト生成
        Form<Book> f = new Form(Book.class);
	    //本一覧取得
	    List<Book> books = CommonUtility.findBookList();        
        return ok(index.render((String)Cache.get("login.key"), f, books));
    }

    /*
     * 本登録機能
     */
	@Security.Authenticated(Secured.class)
    public static Result register() {
        //requestからフォームインスタンスを取得
    	Form<Book> f = new Form(Book.class).bindFromRequest();
    	if (!f.hasErrors()) {
            //フォームにエラーがない場合、Messageインスタンスを取得
    		Book data = f.get();
    		data.owner_name = (String)Cache.get("login.key");
    		data.borrower = null;
            //Messageインスタンスを保存
    		data.save();
            //Welcomeページにリダイレクト
    		return redirect("/");
    	} else {
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        

    		return badRequest(index.render("ERROR", f, books));
    	}
    }

    /*
     * 論理削除機能
     */
	@Security.Authenticated(Secured.class)
    public static Result logicalDelete(Long id) {
        Book book = Book.find.byId(id);
        if (book != null) {
            //deleteStatusを1に更新する。
            book.deleteStatus = "1";
            book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
    }
    
    /*
     * 物理削除機能
     */
    public static Result delete(Long id) {
        Book obj = Book.find.byId(id);
        if (obj != null) {
            obj.delete();
            return redirect("/");
        } else {
            Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        

            return ok(index.render("削除対象が見つかりませんでした", f, books));
        }
    }
    
    /*
     * 本を借りるときの処理
     */
	@Security.Authenticated(Secured.class)
    public static Result borrowBook(Long id) {
        Book book = Book.find.byId(id);
        if (book != null) {
			//sessionからユーザー名を取得
        	String username = (String)Cache.get("login.key");
        	//Book情報の更新
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
			book.borrower = username;
			book.update();

			//メール送信
			Admin owner = Admin.find.where().eq("username", book.owner_name).findUnique();
			CommonUtility.SendMail(owner.email, username, book.book_name);
			
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
    }
    
    /*
     * 本を返すときの処理
     */
	@Security.Authenticated(Secured.class)
    public static Result returnBook(Long id) {
        Book book = Book.find.byId(id);
        if (book != null) {
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
        	book.borrower = "";
        	book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = CommonUtility.findBookList();        
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
    }

    /*
     * ログアウト機能
     */
    public static Result logout(){
    	//セッションクリア
    	Cache.remove("login.key");

    	return redirect(routes.Authentication.login());
    }
}