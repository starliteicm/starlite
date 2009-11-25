package com.itao.starlite.docs.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.util.ByteUtils;

import com.google.inject.Inject;
import com.itao.starlite.auth.User;
import com.itao.starlite.auth.exceptions.InsufficientPrivilagesException;
import com.itao.starlite.auth.manager.AuthManager;
import com.itao.starlite.docs.manager.DocumentManager;
import com.itao.starlite.docs.model.Document;

public class DocumentService extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2623575819122579646L;

	@Inject
	private DocumentManager docManager;
	
	@Inject
	private AuthManager authManager;
	
	private RestletFileUpload fileUpload = new RestletFileUpload();
	
	public DocumentService() {
		fileUpload.setFileSizeMax(10*1024*1024);
		fileUpload.setFileItemFactory(new DiskFileItemFactory());
	}
	
//	@Override
//	protected void doPost(Request req, Response resp) {
//		String docName = "file";
//		Document docInfo = new Document();
//		docInfo.setName(docName);
//		try {
//			List<FileItem> fileItems = fileUpload.parseRepresentation(req.getEntity());
//			boolean done = false;
//			InputStream fIn = null;
//			String folder = "";
//			for (FileItem f: fileItems) {
//				String name = f.getFieldName();
//				if (f.isFormField()) {
//					if (name.equals("name"))
//						docInfo.setName(f.getString());
//					if (name.equals("folder")) {
//						folder = f.getString();
//					}
//				} else if (name.equals("doc")) {
//					docInfo.setContentType(f.getContentType());
//					fIn = f.getInputStream();
//				}
//			}
//			if (fIn != null) {
//				docManager.createDocument(docInfo, folder, fIn);
//			} else
//				resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//		} catch (FileUploadException e) {
//			resp.setStatus(Status.SERVER_ERROR_INTERNAL);
//		} catch (IOException e) {
//			resp.setStatus(Status.SERVER_ERROR_INTERNAL);
//		}
//	}
	
	
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			HttpSession session = req.getSession();
			
			User user = (User) session.getAttribute("userObj");
			
			String path = URLDecoder.decode(req.getPathInfo(), "UTF-8");
			if (path != null) {				
				
				Object[] result = docManager.getDocumentWithData(path, user);
				if (result == null) {
					resp.setStatus(404);
					resp.getWriter().println("404 Not Found");
					return;
				}
				Document doc = (Document) result[0];
				InputStream in = (InputStream) result[1];
				//InputStream in = docManager.getDocumentData(doc.getId());
				if (in == null) {
					resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					resp.getWriter().println("Internal Server Error");
					return;
				} else {
					//MediaType m = new MediaType(doc.getContentType());
					resp.addHeader("content-type", doc.getContentType());
					OutputStream out = resp.getOutputStream();
					ByteUtils.write(in, out);
				}
			} else {
				resp.setStatus(404);
				resp.getWriter().println("404 Not Found");
			}
		} catch (UnsupportedEncodingException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().println("Bad Request");
		} catch (InsufficientPrivilagesException e) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			resp.getWriter().println("Forbidden");
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().println("Internal Server Error");
		}
	}
}
