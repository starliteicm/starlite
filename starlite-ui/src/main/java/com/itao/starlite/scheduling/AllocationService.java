package com.itao.starlite.scheduling;

import java.io.IOException;

import org.jibx.runtime.JiBXException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import com.google.inject.Inject;
import com.itao.concierge.jibx.JibxRepresentation;
import com.itao.concierge.pub.restlet.AttributeWrapper;
import com.itao.concierge.pub.restlet.RestletHelper;
import com.itao.starlite.exceptions.CausesCollisionsException;
import com.itao.starlite.scheduling.manager.SchedulingManager;
import com.itao.starlite.scheduling.model.Allocation;

public class AllocationService extends RestletHelper {
	@Inject
	SchedulingManager manager;
	
	@Override
	protected void doGet(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		if (id == null) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		} else {
			response.setEntity(new JibxRepresentation(manager.getAllocation(id)));
		}
	}
	
	@Override
	protected void doPut(Request request, Response response) {
		try {
			Allocation a = JibxRepresentation.toObject(request.getEntity(), Allocation.class);
			manager.saveAllocation(a);
		} catch (JiBXException e) {
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		} catch (IOException e) {
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		} catch (CausesCollisionsException e) {
			response.setStatus(Status.CLIENT_ERROR_CONFLICT);
		}
	}
	
	@Override
	protected void doDelete(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer id = attrs.getInt("id");
		manager.deleteAllocation(id);
	}
}
