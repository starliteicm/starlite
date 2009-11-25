package com.itao.starlite.manager;

import java.security.InvalidKeyException;
import java.util.concurrent.TimeoutException;

import com.google.inject.Inject;
import com.itao.starlite.dao.ApprovalGroupDao;
import com.itao.starlite.model.ApprovalGroup;
import com.itao.starlite.model.ApprovalStatus;
import com.wideplay.warp.persist.Transactional;

public class ApprovalsManager {
	@Inject
	private ApprovalGroupDao approvalGroupDao;
	
	//Default timeout is 2 mins.
	private static final long defaultTimeout = 1000 * 60 * 2; 
	
	/**
	 * Attempts to place the ApprovalGroup into Review mode.
	 * @param approvalGroupId
	 * @param timeout
	 * @throws IllegalStateException if the ApprovalGroup is not {@link ApprovalStatus.OPEN_FOR_EDITING}.
	 * @return the approvalKey required to approve or cancel a 
	 */
	@Transactional
	public String review(Integer approvalGroupId, long timeout) {
		ApprovalGroup ag = approvalGroupDao.findById(approvalGroupId);
		ag.checkForTimeout();
		return ag.review(timeout);
	}
	
	@Transactional
	public String review(Integer approvalGroupId) {
		return review(approvalGroupId, defaultTimeout);
	}
	
	@Transactional(rollbackOn={InvalidKeyException.class, RuntimeException.class})
	public void open(Integer approvalGroupId, String key) throws InvalidKeyException {
		ApprovalGroup ag = approvalGroupDao.findById(approvalGroupId);
		ag.checkForTimeout();
		ag.open(key);
	}
	
	@Transactional(rollbackOn={InvalidKeyException.class, RuntimeException.class})
	public void open(Integer approvalGroupId) throws InvalidKeyException {
		open(approvalGroupId, null);
	}
	
	@Transactional(rollbackOn={InvalidKeyException.class, RuntimeException.class})
	public void approve(Integer approvalGroupId, String key) throws InvalidKeyException, TimeoutException {
		ApprovalGroup ag = approvalGroupDao.findById(approvalGroupId);
		ag.checkForTimeout();
		ag.approve(key);
	}
}
