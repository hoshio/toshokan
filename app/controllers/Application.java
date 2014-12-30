package controllers;

import java.util.List;

import models.Book;
import models.User;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

import common.Mailer;

public class Application extends Controller {

    /*
    * トップページ表示
    */
	@Security.Authenticated(Secured.class)
    public static Result index() {
        //フォームオブジェクト生成
    	Form<Book> f = new Form<Book>(Book.class);
	    //本一覧取得
	    List<Book> books = Book.findAll();        
        return ok(index.render(null, Secured.getUserInfo(), f, books));
	}

	/*
	 * トップページ表示
	 */
    public static Result initview() {
        //フォームオブジェクト生成
        Form<Book> f = new Form<Book>(Book.class);
	    //本一覧取得
	    List<Book> books = Book.findAll();        
        return ok(index.render(null, Secured.getUserInfo(), f, books));
    }

    /*
     * 本登録機能
     */
	@Security.Authenticated(Secured.class)
    public static Result register() {
        //requestからフォームインスタンスを取得
    	Form<Book> f = new Form<Book>(Book.class).bindFromRequest();
    	if (!f.hasErrors()) {
            //フォームにエラーがない場合、Messageインスタンスを取得
    		Book data = f.get();
    		
    		//ISBNコードより書籍情報を取得する(見つからないとぬるポで落ちる)
    		try {
    			ItemLookup.setBookInf(data);	
    		} catch (RuntimeException e) {
    			return badRequest(index.render("本が見つかりませんでした", Secured.getUserInfo(),f, Book.findAll()));
    		}
    		
    		data.owner = Secured.getUserInfo();
    		data.borrower = null;
            //Messageインスタンスを保存
    		data.save();
            //Welcomeページにリダイレクト
    		return redirect("/");
    	} else {
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
    		return badRequest(index.render("ERROR", Secured.getUserInfo(), f, books));
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
        	Form<Book> f = new Form<Book>(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
            return ok(index.render("ERROR:そのID番号は見つかりません", Secured.getUserInfo(), f,books));
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
            Form<Book> f = new Form<Book>(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        

            return ok(index.render("削除対象が見つかりませんでした", Secured.getUserInfo(), f, books));
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
        	User borrower = Secured.getUserInfo();
        	//Book情報の更新
        	book.borrower = borrower;
			book.update();
			//メール送信
			User owner = User.find(book.owner.username);
			Mailer mailer = new Mailer(owner.email, borrower.username , book.book_name);
			mailer.start();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form<Book>(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
            return ok(index.render("ERROR:そのID番号は見つかりません", Secured.getUserInfo(), f,books));
        }
    }
    
    /*
     * 本を返すときの処理
     */
	@Security.Authenticated(Secured.class)
    public static Result returnBook(Long id) {
        Book book = Book.find(id);
        if (book != null) {
        	book.borrower = null;
        	book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form<Book>(Book.class);
    	    //本一覧取得
    	    List<Book> books = Book.findAll();        
            return ok(index.render("ERROR", Secured.getUserInfo(), f, books));
        }
    }

    /*
     * ログアウト機能
     */
    public static Result logout(){
    	//セッションクリア
    	Cache.remove("login.key");
    	Secured.removeUserInfo();
    	return redirect(routes.Authentication.login());
    }
}