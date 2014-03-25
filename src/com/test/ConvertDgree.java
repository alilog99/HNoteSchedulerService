package com.test;

import java.text.DecimalFormat;

public class ConvertDgree {
	/**
	 * @param args
	 * int d = (int)deg;
		double md = (Math.abs(deg - d)) * 60;
		N 51 Deg 1.4847 mins 
	 */
	public static void main(String[] args) {
		double[] mydeg = getDegreesMinutes(51.018938270);
		System.out.println("conv=" +mydeg[0] +" : " +mydeg[1]);
		
		String degree = "E 51 Deg 1.1362962175194 mins ";
		double decimalVal = 0;
		if (degree.contains("mins")){
			degree = degree.replace("mins", " ");
	    }if (degree.contains("Deg")){
	    	degree = degree.replace("Deg", ":");
	    }
	    System.out.println(degree);
	    decimalVal = getLatitude(degree);
	    System.out.println(decimalVal);

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
	

	
// Converts it into Degrees and minutes in decimal as per requirement. To 3 decmal places
	public static double[] getDegreesMinutes(double deg){
		DecimalFormat df = new DecimalFormat("#.##############");			// 3 decimal places
		
		int d = (int)deg;
		double md = (Math.abs(deg - d)) * 60;
		md = Double.valueOf(df.format(md));				// converts 3 decimal places text to double

		double arr[] = {d, md};
		return arr;
	}

}
