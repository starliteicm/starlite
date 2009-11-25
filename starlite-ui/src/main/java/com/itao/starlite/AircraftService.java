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
import com.itao.starlite.model.Aircraft;

public class AircraftService extends RestletHelper {
	@Inject
	private StarliteCoreManager manager;
	
	@Override
	protected void doGet(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		
		if (id == null) {
			response.setEntity(new JibxRepresentation(manager.getAllAircraft()));
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
			Aircraft newAircraft = JibxRepresentation.toObject(request.getEntity(), Aircraft.class);
			newAircraft.setId(null);
			newAircraft = manager.saveAircraft(newAircraft);
			response.setEntity(new JibxRepresentation(newAircraft));
		} catch (JiBXException e) {
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
			Aircraft aircraft = JibxRepresentation.toObject(request.getEntity(), Aircraft.class);
			if (!aircraft.getId().equals(id)) {
				response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return;
			}
			manager.saveAircraft(aircraft);
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
		
		manager.deleteAircraft(id);
	}
}
