package controllers;

import java.util.*;
//modelsパッケージ使うよね
import common.*;
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
	    List<Book> books = Book.findAll();        
        return ok(index.render((String)Cache.get("login.key"), f, books));
	}

	/*
	 * トップページ表示
	 */
    public static Result initview() {
        //フォームオブジェクト生成
        Form<Book> f = new Form(Book.class);
	    //本一覧取得
	    List<Book> books = Book.findAll();        
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
    		data.borrower_name = null;
    		//オーナー情報をセッションからセット
    		data.owner = Secured.getUserInfo();
            //Messageインスタンスを保存
    		data.save();
            //Welcomeページにリダイレクト
    		return redirect("/");
    	} else {
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
    		return badRequest(index.render("ERROR", f, books));
    	}
    }

    /*
     * 論理削除機能
     */
	@Security.Authenticated(Secured.class)
    public static Result logicalDelete(Long id) {
        Book book = Book.find(id);
        if (book != null) {
            //deleteStatusを1に更新する。
            book.deleteStatus = "1";
            book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
    }
    
    /*
     * 物理削除機能
     */
    public static Result delete(Long id) {
        Book obj = Book.find(id);
        if (obj != null) {
            obj.delete();
            return redirect("/");
        } else {
            Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        

            return ok(index.render("削除対象が見つかりませんでした", f, books));
        }
    }
    
    /*
     * 本を借りるときの処理
     */
	@Security.Authenticated(Secured.class)
    public static Result borrowBook(Long id) {
        Book book = Book.find(id);
        if (book != null) {
			//sessionからユーザー名を取得
        	String username = (String)Cache.get("login.key");
        	User borrower = Secured.getUserInfo();
        	//Book情報の更新
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
			book.borrower_name = username;
			book.update();

			//メール送信
			User owner = User.find(book.owner_name);
			
			Mailer mailer = new Mailer(owner.email, borrower.username , book.book_name);
			mailer.start();
			
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
    }
    
    /*
     * 本を返すときの処理
     */
	@Security.Authenticated(Secured.class)
    public static Result returnBook(Long id) {
        Book book = Book.find(id);
        if (book != null) {
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
        	book.borrower_name = "";
        	book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
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