package controllers;

import java.util.List;

import play.*;
import play.mvc.*;
import models.Book;

import org.apache.commons.mail.*;

public class CommonUtility extends Controller {

    /**
    * メール送信機能
    */
    public static void SendMail(String owner_email, String borrower, String book_name){
    	SimpleEmail mailer = new SimpleEmail();
    	try {
    	    mailer.setCharset("UTF-8");
    	    mailer.setHostName("smtp.gmail.com");
    	    mailer.setSmtpPort(465);
    	    mailer.setSSL(true);
    	    mailer.setAuthentication("toshokanapp@gmail.com", "toshoapp");
    	    mailer.setFrom("toshokanapp@gmail.com");
    	    mailer.setMsg(borrower + "があなたの" + book_name + "を貸してほしいそうです。");
    	    mailer.setSubject("テストメール");
    	    mailer.addTo(owner_email);
    	    mailer.send();
    	} catch(EmailException e) {
    	    Logger.error(e.toString(), e);
    	}
    }
    
    /*
     * 本一覧を取得 (条件: 削除フラグ = 0, idで降順（新規が上にくる)
     */
    public static List<Book> findBookList(){
    	List<Book> books = Book.find.where().eq("deleteStatus", "0").orderBy("id desc").findList();
    	return books;
    }

}