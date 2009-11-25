package com.itao.starlite.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Very simple CurrencyFormatter class. Just returns the CurrencySymbol + the amount with the decimal place in the right location, formatted in the
 * manner dictated by the default locale.
 * @author Jason Choy
 *
 */
public class CurrencyFormatter {
	private Currency currency;
	private NumberFormat numberFormat;
	private boolean showSymbol = true;
	
	public static CurrencyFormatter getInstance(Currency currency) {
		return getInstance(currency, true); 
	}
	
	public static CurrencyFormatter getInstance(Currency currency, boolean showSymbol) {
		CurrencyFormatter formatter = new CurrencyFormatter();
		formatter.showSymbol = showSymbol;
		formatter.currency = currency;
		formatter.numberFormat = new DecimalFormat("#,##0.00");
		if(!showSymbol){formatter.numberFormat.setGroupingUsed(false);}
		return formatter;
	}
	
	private CurrencyFormatter() {}
	
	public String format(long amount) {
		StringBuilder buf = new StringBuilder();
		if (currency != null) {
			if(showSymbol){
			if (currency.getCurrencyCode().equals("USD")) {
				buf.append("$");
			} else if (currency.getCurrencyCode().equals("GBP")) {
				buf.append("£");
			} else if (currency.getCurrencyCode().equals("EUR")) {
				buf.append("\u20ac");
			} else {
				buf.append(currency.getSymbol() + " ");
			}
			}
		}
		
		int fractionDigits = currency.getDefaultFractionDigits();
		int cents = (int)Math.pow(10, fractionDigits);
		
		double whole = (double)(amount / cents);
		double fract = (amount % cents)/((double)cents);
		
		
		
		buf.append(numberFormat.format(whole+fract));
		return buf.toString();
	}
}
