package com.ukho.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

import com.sun.mail.imap.IMAPFolder;
import com.ukho.bo.ChartBO;

public class GetEmailContents {
	private static ArrayList<ChartBO> chartData = null;
	//private static  ChartBO chartObject = null;
	
	public static void main(String[] args) {
		GetEmailContents getEmailcont = new GetEmailContents();
		chartData = getEmailcont.getChartDataArray();
		System.out.println("msgData size is " +chartData.size());
		for (int i = 0; i < chartData.size(); i++) {
			System.out.println("recieve date: " +chartData.get(i).getItem_recieveDate() +", Affected:" +chartData.get(i).getItem_affected() );
		}
	}
	
	public static double ConvertToDecimals(String degree) {
		
		//String degree = "E 51 Deg 1.1362962175194 mins ";
		double decimalVal = 0;
		if (degree.contains("mins")){
			degree = degree.replace("mins", " ");
	    }if (degree.contains("Deg")){
	    	degree = degree.replace("Deg", ":");
	    }
	    //System.out.println(degree);
	    decimalVal = getLatitude(degree);
	    System.out.println("converted value" +decimalVal);
	    
	    return decimalVal;

	}
	
	static double getLatitude(String degree){
		int isNegative = 1;
		degree = degree.replace("N", " ");
		degree = degree.replace("S", " ");
		degree = degree.replace("E", " ");
		degree = degree.replace("W", " ");
		degree = degree.trim();
		String[] parts = degree.split(":");
		double p1 = Double.parseDouble(parts[0]);
		double p2 = Double.parseDouble(parts[1]);
		
		double dec60 = p2 +p1*60;
		double latitude = dec60/60;
		
		return latitude;
	}
	public ArrayList<ChartBO> getChartDataArray(){
		
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");

			Session session;

			session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("mail.appserviceprovider.com","info@appserviceprovider.com", "ali123");

			IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
			folder.open(Folder.READ_ONLY);

			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message message[] = folder.search(unseenFlagTerm);

			chartData = getMessageParams(folder, message);
			
			
			folder.close(false);
			store.close();
			

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return chartData;
	}

	public double convertDegreeToDecimals(String degree){
		
//		int d = (int)deg;
//		double md = (Math.abs(deg - d)) * 60;
		
		
		
		return 2;
	}
	public static ArrayList<ChartBO> getMessageParams(IMAPFolder folder, Message message[]) {
		ChartBO chartObject;
		chartData = new ArrayList<ChartBO>();
		
		try {
			int j = message.length - 1;

			for (int i = j; i >= 0; i--) {
				
				String subject = message[i].getSubject();
				subject = subject.toLowerCase();
				if (subject.contains("h-note") && subject.contains("chart")) {
					chartObject = new ChartBO();			// initialize and make it ready for new entry for the object
					
					//message[i].setFlag(Flags.Flag.SEEN, false);						// MARK MESSAGE SEEN
					System.out.println("Message " + (i + 1));
					
					long long_time =message[i].getReceivedDate().getTime();

					//Date localTime = new Date(t);
					
					System.out.println(long_time);
					
					System.out.println("Recieve date : " +message[i].getReceivedDate());
					chartObject.setItem_recieveDate(long_time);		// set email date
					
					Address[] internetAdd = message[i].getFrom();
					String emailAddress = ((InternetAddress)internetAdd[0]).getAddress();		// get actual email
					
					//System.out.println("From : " + emailAddress);
					//chartObject.setUserid(internetAdd[0].toString());		// wrong as it have Name as well					
					chartObject.setUserid(emailAddress);						// SETS USER ID
					//System.out.println("Subject : " + message[i].getSubject());
					

					if (message[i].getContent() instanceof String) {
						String body = (String) message[i].getContent().toString();
						System.out.println("Plain Body(no MIME type): " + body);

					} else {

						// System.out.println("Body (Multipart) : " +
						// message[i].getContent());
						Multipart multipart = (Multipart) message[i]
								.getContent();
						// System.out.println(multipart.getCount()
						// +"\n---------------------------\n");

						// for (int k = 0; k < multipart.getCount(); k++) {
						for (int k = 0; k < 1; k++) {

							BodyPart bodyPart = multipart.getBodyPart(k);
							BufferedReader br = null;
							String sCurrentLine;

							br = new BufferedReader(new StringReader(bodyPart
									.getContent().toString()));
							while ((sCurrentLine = br.readLine()) != null) {
								System.out.println(sCurrentLine);
								String fields[] = sCurrentLine.split(":");
								String _LABEL_LINE = fields[0].toLowerCase().trim();

								if (_LABEL_LINE.contains("userlatitude")) {
									System.out.println("userlatitude:"+ fields[1].trim());
									chartObject.setItem_lat(fields[1].trim());
								}
								if (_LABEL_LINE.contains("userlongitude")) {
									System.out.println("userlongitude:"+ fields[1].trim());
									chartObject.setItem_long(fields[1].trim());
								}
								
								// START:OBJECT LATLONG for N/S or E/W
								if (_LABEL_LINE.contains("object latitude")) {
									System.out.println("object latitude:"+ fields[1].trim());
									double obj_lat = ConvertToDecimals(fields[1].trim());
									chartObject.setItem_object_lat(obj_lat+"");
								}
								if (_LABEL_LINE.contains("object longitude")) {
									System.out.println("object longitude:"+ fields[1].trim());
									double obj_long = ConvertToDecimals(fields[1].trim());
									chartObject.setItem_object_long(obj_long+"");
								}
								// END:OBJECT LATLONG
								
								if (_LABEL_LINE.contains("chart")) {
									System.out.println("affected:" + fields[1].trim());
									chartObject.setItem_affected(fields[1].trim());
								}if (_LABEL_LINE.contains("edition")) {
									System.out.println("edition:" + fields[1].trim());
									chartObject.setItem_edition(fields[1].trim());
								}
								if (_LABEL_LINE.contains("edition")) {
									System.out.println("edition:" + fields[1].trim());
									chartObject.setItem_obsr(fields[1].trim());
								}
								if (_LABEL_LINE.contains("last updated")) {
									String[] weekYr = fields[1].trim().split(",");
		
									String week = weekYr[0].substring(5, weekYr[0].length());	// get week from position 5
									System.out.println("last updated:" + fields[1].trim() +"=>" +week+" => year " +weekYr[1]);
									chartObject.setItem_week(week.trim());
									chartObject.setItem_year(weekYr[1].trim());
								}
								if (_LABEL_LINE.contains("observation")) {
									System.out.println("observation:" + fields[1].trim());
									chartObject.setItem_obsr(fields[1].trim());
								}

							}
							
						}
						
					}
					
					// add records per email
					chartData.add(chartObject);
					
				}
				

			}

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return chartData;
	}

}
