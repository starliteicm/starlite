package com.itao.starlite.ui;

public class Breadcrumb {
	private String label;
	private String url = null;
	
	public Breadcrumb(String label, String url) {
		this.label = label;
		this.url = url;
	}
	
	public Breadcrumb(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getUrl() {
		return url;
	}
	
	public static Breadcrumb[] toArray(Breadcrumb... breadcrumbs) {
		return breadcrumbs;
	}
}
