package controllers;

import static play.data.Form.form;
import models.Admin;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator{
	
	/*
	 * 現在ログインしているユーザーのユーザー名を取得
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context ctx){
		String session = (String)Cache.get("login.key");
		if (session == null){
			return null;    // ログインセッションなし
		}

		// アクセスのたびにログイン情報登録をリフレッシュする
		updateSession(session);
        
		return session;
	}
	
	public static void updateSession(String session)
    {
        // アプリケーションキャッシュの有効期限を更新
        Cache.set("login.key", session, 10);
    }
	
    /**
    * セッション作成機能
    */
    public static void createSession(String username) {
        //フォームオブジェクト生成
		Object strCache = Cache.get("login.key");
		if( strCache == null ) {
			updateSession(username);
		} else {
		    strCache = strCache.toString();
		}
	}

	
	/*
	 * getUsernameがnullを返すと、
	 * 認証機能はリクエストを中断し、
	 * その代わりにログイン画面にリダイレクトする
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 */
	@Override
	public Result onUnauthorized(Context ctx){
    	Form<Admin> loginForm = form(Admin.class).bindFromRequest();
		return badRequest(views.html.login.render("session timeout", loginForm));
//		return redirect(controllers.routes.Authentication.login());
	}

}