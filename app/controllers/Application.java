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

public class Application extends Controller {

    public static Result index() {
        //フォームオブジェクト生成
    	Form<Book> f = new Form(Book.class);
        //本一覧を取得 (条件: 削除フラグ = 0, idで降順（新規が上にくる)
        List<Book> books = Book.find.where().eq("deleteStatus", "0").orderBy("id desc").findList();
        Logger.debug("通ったよ");
        return ok(index.render("ここに部屋の名前がくる?", f, books));
    }

    public static Result initview() {
        //フォームオブジェクト生成
        Form<Book> f = new Form(Book.class);
        //本一覧を取得
        List<Book> books = Book.find.all();
        return ok(index.render("Your new application is ready.", f, books));
    }

    public static Result register() {
        //requestからフォームインスタンスを取得
    	Form<Book> f = new Form(Book.class).bindFromRequest();
    	if (!f.hasErrors()) {
            //フォームにエラーがない場合、Messageインスタンスを取得
    		Book data = f.get();
    		data.borrower = null;
            //Messageインスタンスを保存
    		data.save();
            //Welcomeページにリダイレクト
    		return redirect("/");
    	} else {
            List<Book> books = Book.find.where().eq("deleteStatus", "0").orderBy("id desc").findList();
    		return badRequest(index.render("ERROR", f, books));
    	}
    }
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
            //本一覧を取得
            List<Book> books = Book.find.all();
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
       
    }
    public static Result delete(Long id) {
        Book obj = Book.find.byId(id);
        if (obj != null) {
            obj.delete();
            return redirect("/");
        } else {
            List<Book> books = Book.find.all();
            Form<Book> f = new Form(Book.class);
            return ok(index.render("削除対象が見つかりませんでした", f, books));
        }
    }
    public static Result borrowBook(Long id) {
        Book book = Book.find.byId(id);
        if (book != null) {
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
            book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
            //本一覧を取得
            List<Book> books = Book.find.all();
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
      }
    public static Result returnBook(Long id) {
        Book book = Book.find.byId(id);
        if (book != null) {
        	book.bookStatus= book.bookStatus.equals("0")?"1":"0";
            book.update();
            return redirect("/");
        } else {
            //フォームオブジェクト生成
        	Form<Book> f = new Form(Book.class);
            //本一覧を取得
            List<Book> books = Book.find.all();
            return ok(index.render("ERROR:そのID番号は見つかりません",f,books));
        }
      }

}
