/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package controllers;

import common.KeyManager;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import models.Book;

/**
 *ProductAdvertisingAPI を用いて商品情報を取得するクラス 
 */
public class ItemLookup {

	//アクセスキー
	private static final String AWS_ACCESS_KEY_ID = KeyManager.instance().getAccessKey();

	//シークレットキー
	private static final String AWS_SECRET_KEY = KeyManager.instance().getSecretKey();

	//エンドポイント指定
	private static final String ENDPOINT = "ecs.amazonaws.jp";

	//ISBNコード
	private static String ITEM_ID = null;
	
	/**
	 * 入力されたISBNコードより書籍情報を取得し、Bookオブジェクトに格納する
	 * ISBNコードの入力チェックを実施しておく事
	 * @param book
	 */
	public static void setBookInf (Book Data){
		
		//入力情報よりISBNコードを設定
		ITEM_ID = Data.isbn_code;
		
		//変数の宣言
		String requestUrl = null;
		String title = null;
		String author = null;
		String publisher = null;
		String imageURL = null;
		
		//requests helper のセットアップ
		SignedRequestsHelper helper;
		try {
			helper = SignedRequestsHelper.getInstance
					(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		//リクエストパラメータをMapに格納
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2013-08-01");
		params.put("Operation", "ItemLookup");
		params.put("ItemId", ITEM_ID);
		params.put("ResponseGroup", "Medium");
		params.put("AssociateTag", KeyManager.instance().getAssociateTag());//アソシエイトタグ	
		params.put("IdType", "ISBN");
		params.put("SearchIndex", "Books");

		//リクエストURLの作成
		requestUrl = helper.sign(params);

		//レスポンスより値を取得
		title = fetchTitle(requestUrl);
		author = fetchAuthor(requestUrl);
		publisher = fetchPublisher(requestUrl);
		imageURL = fetchImage(requestUrl);
		
		//取得した値をData変数に格納
		Data.book_name = title;
		Data.author = author;
		Data.publisher = publisher;
		Data.image = imageURL;

		return;
	}
	
	/**
	 * レスポンスXMLより書名を取得
	 * @param requestUrl
	 * @return
	 */
	private static String fetchTitle(String requestUrl) {
		String title = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			Node titleNode = doc.getElementsByTagName("Title").item(0);
			title = titleNode.getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return title;
	}
	
    /**
     * レスポンスXMLより著者名を取得
     * @param requestUrl
     * @return
     */
	private static String fetchAuthor(String requestUrl) {
		String author = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			Node authorNode = doc.getElementsByTagName("Author").item(0);
			author = authorNode.getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return author;
	}
	
	/**
	 * レスポンスXMLより出版社を取得
	 * @param requestUrl
	 * @return
	 */
	private static String fetchPublisher(String requestUrl) {
		String publisher = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			Node titleNode = doc.getElementsByTagName("Publisher").item(0);
			publisher = titleNode.getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return publisher;
	}
	
	/**
	 * レスポンスXMLより画像URLを取得
	 */
	private static String fetchImage(String requestUrl) {
		String image = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			Node imageNode = doc.getElementsByTagName("TinyImage").item(0);
			image = imageNode.getFirstChild().getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return image;
	}
}