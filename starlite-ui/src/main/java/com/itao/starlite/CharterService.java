package com.itao.starlite;

import java.io.IOException;

import org.jibx.runtime.JiBXException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import com.google.inject.Inject;
import com.itao.concierge.jibx.JibxRepresentation;
import com.itao.concierge.pub.restlet.AttributeWrapper;
import com.itao.concierge.pub.restlet.RestletHelper;
import com.itao.starlite.manager.StarliteCoreManager;
import com.itao.starlite.model.Charter;

public class CharterService extends RestletHelper {
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	protected void doGet(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		
		if (id == null) {
			response.setEntity(new JibxRepresentation(manager.getAllCharters()));
		} else {
			response.setEntity(new JibxRepresentation(manager.getAircraft(id)));
		}
	}
	
	@Override
	protected void doPost(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		if (id != null) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return;
		}
		
		
		try {
			Charter newCharter = JibxRepresentation.toObject(request.getEntity(), Charter.class);
			newCharter.setId(null);
			newCharter = manager.saveCharter(newCharter);
			response.setEntity(new JibxRepresentation(newCharter));
		} catch (JiBXException e) {
			e.printStackTrace();
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		} catch (IOException e) {
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	@Override
	protected void doPut(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		
		if (id == null) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return;
		}
		
		try {
			Charter charter = JibxRepresentation.toObject(request.getEntity(), Charter.class);
			if (!charter.getId().equals(id)) {
				response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return;
			}
			manager.saveCharter(charter);
		} catch (JiBXException e) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		} catch (IOException e) {
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	@Override
	protected void doDelete(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		
		if (id == null) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return;
		}
		
		manager.deleteCharter(id);
	}
}
