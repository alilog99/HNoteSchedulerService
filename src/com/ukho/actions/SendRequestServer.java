package com.ukho.actions;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class SendRequestServer {
	// do this wherever you are wanting to POST
	public static URL url;
	public static HttpURLConnection conn;

	public static void main(String[] args) {
		try {
			// if you are using https, make sure to import
			// java.net.HttpsURLConnection
			url = new URL(
					"http://appserviceprovider.com/clients/ukho/hnote/insert-latlong.php?lat=324234");

			// you need to encode ONLY the values of the parameters
			String param = "userid=" + URLEncoder.encode("2", "UTF-8")
					+ "&type=" + URLEncoder.encode("1", "UTF-8") + "&lat="
					+ URLEncoder.encode("54.435435", "UTF-8");

			
			conn = (HttpURLConnection) url.openConnection();
			// set the output to true, indicating you are outputting(uploading)
			// POST data
			conn.setDoOutput(true);
			// once you set the output to true, you don’t really need to set the
			// request method to post, but I’m doing it anyway
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setRequestProperty("Accept", "text/plain");
			
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream());
			out.write(param);
			out.flush();
			out.close();
			
			//build the string to store the response text from the server
			String response= "";

			//start listening to the stream
			Scanner inStream = new Scanner(conn.getInputStream());

			//process the stream and store it in StringBuilder
			while(inStream.hasNextLine())
			response+=(inStream.nextLine());
			System.out.println(response);

		}
		// catch some error
		catch (MalformedURLException ex) {
			System.out.println(ex);
		}
		// and some more
		catch (IOException ex) {
			System.out.println(ex);

		}

	}

}
