//package models;
//
//import play.data.validation.Constraints;
//import play.db.ebean.Model;
//import java.security.NoSuchAlgorithmException;
//
//public class Login {
//
//	public Long id;
//	
//	@Constraints.Required
//	public String username;
//	
//	@Constraints.Required
//	public String password;
//
//	public String validate() throws java.security.NoSuchAlgorithmException {
//		if (authenticate(username, password) == null) {
//			return "Invalid user or password";
//		}
//		return null;
//	}
//
//	public static User authenticate(String username, String password)
//			throws java.security.NoSuchAlgorithmException {
//		Model.Finder<Long, User> find = new Model.Finder<Long, User>(
//				Long.class, User.class);
//		// String hashedPassword = "";
//		// if(password != null){
//		// hashedpassword = sha512(password);
//		return find.where().eq("username", username).eq("password", password)
//				.findUnique();
//		// }
//	}
//
//	public static String sha512(String message)
//			throws java.security.NoSuchAlgorithmException {
//		java.security.MessageDigest md = java.security.MessageDigest
//				.getInstance("SHA-512");
//		StringBuilder sb = new StringBuilder();
//		md.update(message.getBytes());
//		byte[] mb = md.digest();
//		for (byte m : mb) {
//			String hex = String.format("%02x", m);
//			sb.append(hex);
//		}
//		return sb.toString();
//	}
//
//}
