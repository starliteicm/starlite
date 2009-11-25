package com.itao.starlite.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * <p><del>Immutable</del> class representing a monetary amount for a specified Currency. Provides formatting and simple arithmetic.</p>
 * <p>Is Embeddable when using JPA/Hibernate, using two fields: currencyCode and amount.</p>
 * 
 * @author Jonathan Elliott
 */
@Entity
public class ExchangeRate {
	
	public ExchangeRate(){}
	
	public ExchangeRate(String from, String to){
		setCurrencyCodeFrom(from);
		setCurrencyCodeTo(to);
	}

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(nullable=false)
	private String currencyCodeFrom;
	
	@Column(nullable=false)
	private String currencyCodeTo;

	@Column(nullable=false)
	private double amount=0.0;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;
	
	private String updatedBy;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public String getCurrencyCodeFrom() {
		return currencyCodeFrom;
	}

	public void setCurrencyCodeFrom(String currencyCodeFrom) {
		this.currencyCodeFrom = currencyCodeFrom;
	}

	public String getCurrencyCodeTo() {
		return currencyCodeTo;
	}

	public void setCurrencyCodeTo(String currencyCodeTo) {
		this.currencyCodeTo = currencyCodeTo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Money convert(double ex){
		Money from = new Money(currencyCodeFrom,new Double(ex));
		from = from.multiply(this.amount);
		//System.out.println(ex+"*"+amount+"="+from.getAmountAsDouble());
		return new Money(currencyCodeTo,from.getAmountAsDouble());
	}
	
}
