package controllers;

import java.util.List;

import models.Book;
import models.Room;
import models.User;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import views.html.library;
import common.Mailer;

public class Application extends Controller {

	/*
	 * 部屋ID指定によるメインページ表示
	 */
	@Security.Authenticated(Secured.class)
	public static Result index() {
		// フォームオブジェクト生成
		Form<Book> f = new Form<Book>(Book.class);
		// 本一覧取得
		List<Book> books = Book.findAll();
		return ok(index.render(null, Secured.getUserInfo(), f, books));
	}

	/**
	 * 部屋ID指定による初期表示
	 * @param room_id : 部屋ID
	 * @param msg : メッセージ
	 * @return
	 */
	@Security.Authenticated(Secured.class)
	public static Result init(Long room_id, String msg) {
		// フォームオブジェクト生成
		Form<Book> f = new Form<Book>(Book.class);
		// 本一覧取得 (部屋ID指定)
		List<Book> books = Book.findAll(room_id);
		return ok(library.render(Room.findById(room_id), msg, Secured.getUserInfo(), f, books));
	}

	/*
	 * 本登録機能
	 */
	@Security.Authenticated(Secured.class)
	public static Result register(Long room_id) {
		// requestからフォームインスタンスを取得
		Form<Book> f = new Form<Book>(Book.class).bindFromRequest();

		// エラーがあった場合
		if (f.hasErrors()) {
			return init(room_id, "エラー");
		}

		// フォームにエラーがない場合、Messageインスタンスを取得
		Book book = f.get();
		// ISBNコードより書籍情報を取得する(見つからないとぬるポで落ちる)
		try {
			ItemLookup.setBookInf(book);
		} catch (RuntimeException e) {
			return init(room_id, "本が見つかりませんでした。");
		}
		book.owner = Secured.getUserInfo();
		book.borrower = null;
		book.room = Room.findById(room_id);
		//なぜかidに部屋IDがバインドされてしまう・・・のでここでリセット	。
		book.id = null;
		// Messageインスタンスを保存
		book.save();
		// メインページに戻る
		return redirect(routes.Application.init(book.room.id, "登録しました"));
		//return init(book.room.id, book.book_name + "を登録しました");
	}

	/*
	 * 論理削除機能
	 */
	@Security.Authenticated(Secured.class)
	public static Result logicalDelete(Long id) {
		Book book = Book.find(id);
		// deleteStatusを1に更新する。
		book.deleteStatus = "1";
		book.update();
		return redirect(routes.Application.init(book.room.id, "削除しました"));
		//return init(book.room.id, "削除しました");
	}

	/*
	 * 物理削除機能
	 */
	public static Result delete(Long id) {
		Book book = Book.find(id);
		book.delete();
		return init(book.room.id, "削除しました");
	}

	/*
	 * 本を借りるときの処理
	 */
	@Security.Authenticated(Secured.class)
	public static Result borrowBook(Long id) {
		Book book = Book.find(id);
		User borrower = Secured.getUserInfo();
		// Book情報の更新
		book.borrower = borrower;
		book.update();
		// メール送信
		User owner = book.owner;
		Mailer mailer = new Mailer(owner.email, borrower.username, book.book_name);
		mailer.start();
		return redirect(routes.Application.init(book.room.id, "メールを送信しました"));
		//return init(book.room.id, null);
	}

	/*
	 * 本を返すときの処理
	 */
	@Security.Authenticated(Secured.class)
	public static Result returnBook(Long id) {
		Book book = Book.find(id);
		book.borrower = null;
		book.update();
		return redirect(routes.Application.init(book.room.id, "返却しました"));
		//return init(book.room.id, null);
	}

	/*
	 * ログアウト機能
	 */
	public static Result logout() {
		// セッションクリア
		Cache.remove("login.key");
		Secured.removeUserInfo();
		return redirect(routes.Authentication.login());
	}
}