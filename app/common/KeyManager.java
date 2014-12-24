package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import play.*;

/**
 * Amazon APIのシークレットキー管理クラス
 * application.confのmyconf.pathに指定されたパスから設定ファイルを読み込み、キーを保持する
 * @author suguru
 *
 */
public final class KeyManager {
	
	private static final KeyManager _instance = new KeyManager();
	
	private static  String _accessKey;
	
	private static String _secretKey;
	
	private static String _associateTag;
	
	private KeyManager() {
		String path = Play.application().configuration().getString("myconf.path");
		List<String> confs = readLines(path);
		Logger.info("Loaded key.conf " + String.join(",", confs));
		_accessKey = confs.get(0).split(":")[1];
		_secretKey = confs.get(1).split(":")[1];
		_associateTag = confs.get(2).split(":")[1];
	}
	
	public static List<String> readLines(String path) {
		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
		    for (String line; (line = br.readLine()) != null;) {
		    	lines.add(line);
		    }
		} catch(IOException e) {
			Logger.error("ファイルの読み込みに失敗しました", e);
			throw new RuntimeException(e);
		}
		return lines;
	}
	
	public static KeyManager instance() {
		return _instance;
	}

	public String getAccessKey() {
		return _accessKey;
	}

	public String getSecretKey() {
		return _secretKey;
	}

	public String getAssociateTag() {
		return _associateTag;
	}
}
