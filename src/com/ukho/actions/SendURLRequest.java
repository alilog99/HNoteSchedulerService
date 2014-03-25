package com.ukho.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ukho.bo.ChartBO;


public class SendURLRequest {
	public static ChartBO chartObject = null;
	
	public static void main(String[] args) {
		SendURLRequest http = new SendURLRequest();
		
		GetEmailContents getEmailcont = new GetEmailContents();
		ArrayList<ChartBO> charData = getEmailcont.getChartDataArray();
		System.out.println("msgData size is " +charData.size());
		for (int i = 0; i < charData.size(); i++) {
			System.out.println("recieve date: " +charData.get(i).getItem_recieveDate());
			
			// FOLLOW REST OF IMPLEMENTATION FROM SCHEDULER FOR TESTING
			
		}
		
		

	}

	// HTTP GET request
	public synchronized void inserData(URI uri) {
		
		//Mainurl = Mainurl +aURL;
		

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);
		System.out.println(request.getURI());
		HttpResponse response;

		try {
			response = client.execute(request);

			// Get the response
			BufferedReader rd;
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
/*
	private void sendPost() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"https://www.google.com/accounts/ClientLogin");

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("Email", "youremail"));
			nameValuePairs
					.add(new BasicNameValuePair("Passwd", "yourpassword"));
			nameValuePairs.add(new BasicNameValuePair("accountType", "GOOGLE"));
			nameValuePairs.add(new BasicNameValuePair("source",
					"Google-cURL-Example"));
			nameValuePairs.add(new BasicNameValuePair("service", "ac2dm"));

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				if (line.startsWith("Auth=")) {
					String key = line.substring(5);
					// do something with the key
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	
}
