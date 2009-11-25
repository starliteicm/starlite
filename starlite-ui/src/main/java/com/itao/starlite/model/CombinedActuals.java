package com.itao.starlite.model;

import java.util.List;

public class CombinedActuals extends Actuals {
	private Double totalCapt=0.0;
	private Double totalAframe=0.0;
	private Integer totalLandings=0;
	private Integer totalPax=0;
	private String label;
	private String params;
	
	public CombinedActuals(String label, List<? extends Actuals> actuals) {
		this.label = label;
		for (Actuals a: actuals) {
			if (a.getCapt() != null)
				totalCapt += a.getCapt();
			if (a.getAframe() != null)
				totalAframe += a.getAframe();
			if (a.getLandings() != null)
				totalLandings += a.getLandings();
			if (a.getPax() != null)
				totalPax += a.getPax();
		}
	}

	public String getLabel() {
		return label;
	}

	public String getParams() {
		return params;
	}
	
	public void setParams(String params) {
		this.params = params;
	}

	@Override
	public Double getAframe() {
		return totalAframe;
	}

	@Override
	public Double getCapt() {
		return totalCapt;
	}

	@Override
	public Integer getLandings() {
		return totalLandings;
	}

	@Override
	public Integer getPax() {
		return totalPax;
	}
}
