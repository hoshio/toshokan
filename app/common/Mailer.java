package common;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.Logger;

public class Mailer extends Thread {
	
	private String ownerEmail;
	
	private String borrower;
	
	private String bookName;
	
	public Mailer(String ownerEmail, String borrower, String bookName) {
		this.ownerEmail = ownerEmail;
		this.borrower = borrower;
		this.bookName = bookName;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
    	SimpleEmail mailer = new SimpleEmail();
    	try {
    	    mailer.setCharset("UTF-8");
    	    mailer.setHostName("smtp.gmail.com");
    	    mailer.setSmtpPort(465);
    	    mailer.setSSL(true);
    	    mailer.setAuthentication("toshokanapp@gmail.com", "toshoapp");
    	    mailer.setFrom("toshokanapp@gmail.com");
    	    mailer.setMsg(borrower + "があなたの" + bookName + "を貸してほしいそうです。");
    	    mailer.setSubject("図書館アプリ Rental希望");
    	    mailer.addTo(ownerEmail);
    	    mailer.send();
    	} catch(EmailException e) {
    	    Logger.error(e.toString(), e);
    	}
	}	
}
