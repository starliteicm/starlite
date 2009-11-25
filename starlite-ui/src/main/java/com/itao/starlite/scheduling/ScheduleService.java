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
import com.itao.starlite.scheduling.manager.SchedulingManager;
import com.itao.starlite.scheduling.model.Schedule;

public class ScheduleService extends RestletHelper {
	@Inject
	private SchedulingManager manager;
	
	@Override
	protected void doPut(Request request, Response response) {
		try {
			Schedule s = JibxRepresentation.toObject(request.getEntity(), Schedule.class);
			manager.saveCompleteSchedule(s);
		} catch (JiBXException e) {
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		} catch (IOException e) {
			response.setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}
	
	@Override
	protected void doGet(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer charterId = attrs.getInt("charterId");
		Integer crewMemberId = attrs.getInt("crewMemberId");
		
		Schedule s;
		if (charterId == null && crewMemberId == null) {
			s = manager.getCompleteSchedule();
		} else if (crewMemberId == null){
			s = manager.getScheduleForCharter(charterId);
		} else {
			s = manager.getScheduleForCrewMember(crewMemberId);
		}
		response.setEntity(new JibxRepresentation(s));
	}
	
	@Override
	protected void doDelete(Request request, Response response) {
		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
		Integer allocationId = attrs.getInt("allocationId");
		
		if (allocationId != null) {
			manager.deleteAllocation(allocationId);
			response.setStatus(Status.SUCCESS_OK);
		}
		else
			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
	}

	@Override
	protected void doPost(Request request, Response response) {
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}
}
