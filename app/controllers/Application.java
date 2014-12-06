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
        //本一覧を取得
        List<Book> books = Book.find.all();
        return ok(index.render("Your new application is ready.", f, books));
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
    		data.delete_flg = "0";
    		data.borrower = null;
            //Messageインスタンスを保存
    		data.save();
            //Welcomeページにリダイレクト
    		return redirect("/");
    	} else {
            List<Book> books = Book.find.all();
    		return badRequest(index.render("ERROR", f, books));
    	}
    }

}
