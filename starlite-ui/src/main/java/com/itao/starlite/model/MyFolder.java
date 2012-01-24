package com.itao.starlite.model;

import com.itao.starlite.docs.model.Document;

public class MyFolder extends com.itao.starlite.docs.model.Folder
{
	public MyFolder()
	{
		super();
	}
	
	public Document getFolder(String tag)
	{
		Document doc = new Document();
		doc = this.getDocumentByTag(tag);
		return doc;
	}

}
