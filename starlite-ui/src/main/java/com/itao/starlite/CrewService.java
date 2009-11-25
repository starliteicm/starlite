package com.itao.starlite;

import com.itao.concierge.pub.restlet.RestletHelper;

public class CrewService extends RestletHelper {
//	@Inject
//	private StarliteCoreManager manager;
//	
//	@Override
//	protected void doGet(Request request, Response response) {
//		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
//		Integer id = attrs.getInt("id");
//		
//		if (id == null) {
//			response.setEntity(new JibxRepresentation(manager.getAllCrew()));
//		} else {
//			response.setEntity(new JibxRepresentation(manager.getCrewMember(id)));
//		}
//	}
//	
//	@Override
//	protected void doPost(Request request, Response response) {
//		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
//		Integer id = attrs.getInt("id");
//		if (id != null) {
//			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//			return;
//		}
//		
//		try {
//			CrewMember newCrew = JibxRepresentation.toObject(request.getEntity(), CrewMember.class);
//			newCrew.setId(null);
//			newCrew = manager.saveCrewMember(newCrew);
//			response.setEntity(new JibxRepresentation(newCrew));
//		} catch (JiBXException e) {
//			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//		} catch (IOException e) {
//			response.setStatus(Status.SERVER_ERROR_INTERNAL);
//		}
//	}
//	
//	@Override
//	protected void doPut(Request request, Response response) {
//		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
//		Integer id = attrs.getInt("id");
//		
//		if (id == null) {
//			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//			return;
//		}
//		
//		try {
//			CrewMember cm = JibxRepresentation.toObject(request.getEntity(), CrewMember.class);
//			if (!cm.getId().equals(id)) {
//				response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//				return;
//			}
//			manager.saveCrewMember(cm);
//		} catch (JiBXException e) {
//			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//		} catch (IOException e) {
//			response.setStatus(Status.SERVER_ERROR_INTERNAL);
//		}
//	}
//	
//	@Override
//	protected void doDelete(Request request, Response response) {
//		AttributeWrapper attrs = new AttributeWrapper(request.getAttributes());
//		Integer id = attrs.getInt("id");
//		
//		if (id == null) {
//			response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
//			return;
//		}
//		
//		manager.deleteCrewMember(id);
//	}
}
