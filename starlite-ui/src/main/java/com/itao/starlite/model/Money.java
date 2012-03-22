package com.itao.starlite.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * <p><del>Immutable</del> class representing a monetary amount for a specified Currency. Provides formatting and simple arithmetic.</p>
 * <p>Is Embeddable when using JPA/Hibernate, using two fields: currencyCode and amount.</p>
 * 
 * <p>This class has now been made mutable to allow Struts to set the values from a form. For most other purposes it should be treated as Immutable.</p>
 * @author Jason Choy
 * @author Jonathan Elliott
 */
@Embeddable
public class Money {
	@Transient
	private Currency currency=null;
	@Column(length=3)
	private String currencyCode;
	@Column(nullable=true, columnDefinition="bigint(10) default 0")
	private Long amount;
	
	
	public Money(String money){
		setAmountAsDouble( new Double(money.substring(1).replaceAll(",","")));
	}
	
	public Money(Currency currency, Long amount) {
		this.currency = currency;
		this.currencyCode = currency.getCurrencyCode();
		this.amount = amount;
		if(amount == null){
			this.amount = new Long(0L);
		}
	}
	
	public Money(String currencyCode, double amount) {
		//System.out.println("set as double "+amount);
		try {
			this.currency = Currency.getInstance(currencyCode);
			this.currencyCode = currencyCode;
			setAmountAsDouble(amount);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("CurrencyCode cannot be null", e);
		}
	}
	
	
	/**
	 * Creates a monetary amount for the specified Currency.
	 * @param currencyCode the 3-digit ISO 4217 Currency Code (see {@link http://en.wikipedia.org/wiki/ISO_4217} for more information.)
	 * @param amount
	 * @throws IllegalArgumentException if currencyCode or amount are null, or if currencyCode is not an ISO 4217 Currency Code
	 */
	public Money(String currencyCode, Long amount) {
		try {
			this.currency = Currency.getInstance(currencyCode);
			this.currencyCode = currencyCode;
			this.amount = amount;
			if(amount == null){
				this.amount = new Long(0L);
			}
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("CurrencyCode cannot be null", e);
		}
	}
	
	/**
	 * <del>Private</del> default constructor for Hibernate/Struts.
	 */
	public Money() {}
	
	public Double getAmountAsDouble() 
	{
		Double temp = new Double(0);
		int cents = 100;
		if (getCurrency() != null) {
			int fracDigits = getCurrency().getDefaultFractionDigits();
			cents = (int) Math.pow(10, fracDigits);
		}
		try{
			temp = ((double)amount)/cents;
		}
		catch(Exception e){temp = new Double(0);}
		//return ((double)amount)/cents;
		return temp;
	}
	
	public long getAmount() 
	{
		if (amount == null ){amount = 0L;}
	return amount;
	}
	
	public Currency getCurrency() {
		/*This will be null if the object has been created by hibernate
		 * as currency is not persisted.
		 */
		if (currency == null && currencyCode != null && currencyCode.trim().length() == 3) {
			currency = Currency.getInstance(currencyCode);
		}
		if(currency == null){
			currency = Currency.getInstance("USD");
		}
		return currency;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public Money add(Money other) {
		checkSameCurrency(other);
		return new Money(getCurrency(), getAmount()+other.getAmount());
	}
	
	public Money subtract(Money other) 
	{
		if (other == null) {other = new Money();}
		checkSameCurrency(other);
		return new Money(getCurrency(), getAmount()-other.getAmount());
	}
	
	public Money multiply(Money other) {
		checkSameCurrency(other);
		return new Money(getCurrency(), getAmount()*other.getAmount());
	}
	
	public Money multiply(Integer multiplier) {
		return new Money(getCurrency(), getAmount()*multiplier);
	}
	
	public Money multiply(Double multiplier) {
		Double thisAmount = new Double(getAmountAsDouble());
		return new Money(getCurrency().getCurrencyCode(), thisAmount*multiplier);
	}
	
	public Money divide(Money other) {
		return divide(other, MathContext.DECIMAL32);
	}
	
	public Money divide(Money other, MathContext mathContext) {
		checkSameCurrency(other);
		BigDecimal amount = new BigDecimal(getAmountAsDouble());
		BigDecimal oAmount = new BigDecimal(other.getAmountAsDouble());
		
		BigDecimal newAmount = amount.divide(oAmount);
		newAmount.round(mathContext);
		
		return new Money(getCurrency(), newAmount.longValue());
	}
	
	private void checkSameCurrency(Money other) {
		if (!sameCurrency(other))
			throw new IllegalArgumentException("Monies must be of the same currency");
	}
	
	public boolean sameCurrency(Money other) {
		if (other == null)
			return false;
		//System.out.println(getCurrencyCode()+"=="+other.getCurrencyCode());
		//String otherCC = other.getCurrencyCode();
		//String thisCC  = getCurrencyCode();
		//if(otherCC == null){otherCC = "USD";other.setCurrencyCode("USD");}
		//if(thisCC == null){thisCC = "USD";this.setCurrencyCode("USD");}
		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)amount.intValue();
		result = prime * result
				+ ((getCurrencyCode() == null) ? 0 : getCurrencyCode().hashCode());
		return result;
	}

	/**
	 * Returns true iff both objects have the same currencyCode and amount. currencyCode
	 * must be used as {@link Currency} does not implement equals and hashCode. Does not check equality between different Currencies!
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Money other = (Money) obj;
		if (amount != other.getAmount())
			return false;
		if (getCurrencyCode() == null) {
			if (other.getCurrencyCode() != null)
				return false;
		} else if (!getCurrencyCode().equals(other.getCurrencyCode()))
			return false;
		return true;
	}
	
	public String getNoSymbol() {
		return CurrencyFormatter.getInstance(getCurrency(),false).format(amount);
	}
	
	@Override
	public String toString() 
	{
		String temp="";
	
		try{temp=CurrencyFormatter.getInstance(getCurrency()).format(amount);}
		catch(Exception e){temp="";}
		//return CurrencyFormatter.getInstance(getCurrency()).format(amount);
		return temp;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setAmountAsDouble(Double amount) {
		if (amount == null) {
			setAmount(null);
			return;
		}
			
		int cents = 100;
		if (getCurrency() != null) {
			int fracDigits = getCurrency().getDefaultFractionDigits();
			cents = (int) Math.pow(10, fracDigits);
		}
		setAmount((long) Math.round(((Double)amount)*cents));
	}
	
	
	
	public void setAmount(Long amount) 
	{
		if (amount == null)
		{
			amount = 0L;
		}
		
		this.amount = amount;
	}
}
