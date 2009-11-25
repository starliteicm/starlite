package com.itao.starlite.ui;

public class Tab {
	private String label;
	private String url;
	private boolean isCurrent;
	public Tab(String label, String url, boolean isCurrent) {
		super();
		this.label = label;
		this.url = url;
		this.isCurrent = isCurrent;
	}
	public String getLabel() {
		return label;
	}
	public String getUrl() {
		return url;
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public static Tab[] toArray(Tab... tabs) {
		return tabs;
	}
}
