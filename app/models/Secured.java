package models;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator{
	
	@Override
	public String getUsername(Context ctx){
		return ctx.session().get("username");
	}
	@Override
	public Result onUnauthorized(Context ctx){
		String returnUrl = ctx.request().uri();
		if(returnUrl == null){
			returnUrl = "/";
		}
		ctx.session().put("returnUrl",returnUrl);
		return redirect(controllers.routes.Application.login());
	}
}