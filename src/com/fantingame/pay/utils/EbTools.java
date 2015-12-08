package com.fantingame.pay.utils;

public class EbTools {
	
	public static long rmbToEb(String rmb){
		try {
			Double ebDouble = Math.ceil(Double.parseDouble(rmb)*100);
			return ebDouble.longValue();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0l;
		}
	}
   public static long  yuanToFen(String yuan){
	   try {
			Double ebDouble = Math.ceil(Double.parseDouble(yuan)*100);
			return ebDouble.longValue();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0l;
		}
   }
	
	
	public static String ebToRmb(String eb){
		Double rmb = Double.parseDouble(eb)/100;
		return rmb.toString();
	}
	
	
	public static void main(String[] args) {
		String rmb = "0.01";
		System.out.println(rmbToEb(rmb));
	}
	
}
