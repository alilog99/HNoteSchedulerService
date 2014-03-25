package com.ukho.main;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import com.ukho.actions.GetEmailContents;
import com.ukho.actions.SendURLRequest;
import com.ukho.bo.ChartBO;

public class Scheduler extends Thread  {
	
	public static void main(String[] args) {
		new Scheduler("HNote Scheduler").start();
	

	}

	public Scheduler(String str) {
		super(str);
	}
	
	public void run() {
		int i=0;
		while(true){
			System.out.println(i + " " + getName());
			checkForMessage();
			try {
				sleep(12000);
				i++;
			} 
			catch (InterruptedException e) {
			}
		}
		
	}
	
	
	public synchronized void checkForMessage(){
		SendURLRequest http = new SendURLRequest();
		
		GetEmailContents getEmailcont = new GetEmailContents();
		ArrayList<ChartBO> charData = getEmailcont.getChartDataArray();
		System.out.println("FOUND NEW MESSAGES : " +charData.size());
		if(charData.size()>0){
			for (int i = 0; i < charData.size(); i++) {
				System.out.println("INSERTING RECORD NO: " +i+1);
				System.out.println("recieve date: " +charData.get(i).getItem_recieveDate());
				
				
				URIBuilder builder = new URIBuilder();
				builder.setScheme("http").setHost("appserviceprovider.com/clients/ukho/hnote").setPath("/insert-chart.php")
				    .setParameter("userid", charData.get(i).getUserid())
				    .setParameter("item_affected", charData.get(i).getItem_affected())
				    .setParameter("edition", charData.get(i).getItem_edition())
				    .setParameter("lat", charData.get(i).getItem_object_lat())
				    .setParameter("long", charData.get(i).getItem_long())
				    .setParameter("week", charData.get(i).getItem_week())
				    .setParameter("year", charData.get(i).getItem_year())
				    .setParameter("observ", charData.get(i).getItem_obsr().toString())
				    .setParameter("time", charData.get(i).getItem_recieveDate()+"");
				URI uri;
				try {
					uri = builder.build();
					HttpGet httpget = new HttpGet(uri);
					//System.out.println(httpget.getURI());
					http.inserData(uri);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("*** END OF DATA INSERTED ***: ");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
