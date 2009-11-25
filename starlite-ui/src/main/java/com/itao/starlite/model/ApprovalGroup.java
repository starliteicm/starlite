package com.itao.starlite.model;

import java.security.InvalidKeyException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ApprovalGroup {
	@Id
	@GeneratedValue
	private Integer id;
	
	//@Type(type="enum_varchar")
	private ApprovalStatus approvalStatus = ApprovalStatus.OPEN_FOR_EDITING;
	private String approvalKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date keyExpires;
	
	//Default timeout is 2 minutes.
	//private static final long defaultTimeout = 1000*60*2;
	
	public ApprovalStatus getApprovalStatus() {
		checkForTimeout();
		return approvalStatus;
	}
	
	/**
	 * If in the OPEN_FOR_EDITING state, moves into UNDER_REVIEW state.
	 * @throws IllegalStateException if not OPEN_FOR_EDITING
	 * @return an approval key required to complete the approval process.
	 */
	public synchronized String review(long timeout) {
		if (approvalStatus.equals(ApprovalStatus.OPEN_FOR_EDITING)) {
			approvalStatus = ApprovalStatus.UNDER_REVIEW;
			approvalKey = UUID.randomUUID().toString();
			Date now = new Date();
			keyExpires = new Date();
			keyExpires.setTime(now.getTime() + timeout);
			return approvalKey;
		} else
			throw new IllegalStateException("Item must be open in order to be reviewed.");
	}
	
	/**
	 * If not in the OPEN_FOR_EDITING state, moves into it.
	 * @throws InvalidKeyException 
	 * @throws IllegalStateException if already OPEN_FOR_EDITING
	 */
	public synchronized void open(String key) throws InvalidKeyException {
		if (approvalKey != null && !approvalKey.equals(key)) {
			throw new InvalidKeyException();
		}
		if (approvalStatus.equals(ApprovalStatus.OPEN_FOR_EDITING))
			throw new IllegalStateException("Item is already open for editing.");
		reset();
		approvalStatus = ApprovalStatus.OPEN_FOR_EDITING;
	}
	
	public synchronized void open() throws InvalidKeyException {
		open(null);
	}
	
	/**
	 * If in the UNDER_REVIEW state, moves into APPROVED.
	 * @throws TimeoutException 
	 * @throws InvalidKeyException 
	 * @throws IllegalStateException if not UNDER_REVIEW
	 */
	public synchronized void approve(String key) throws TimeoutException, InvalidKeyException {
		if (approvalStatus.equals(ApprovalStatus.UNDER_REVIEW)) {
			//Check key and timeout
			if (key == null)
				key = "";
			if (!key.equals(approvalKey))
				throw new InvalidKeyException();
			if (checkForTimeout())
				throw new TimeoutException();
			
			reset();
			approvalStatus = ApprovalStatus.APPROVED;
		} else {
			throw new IllegalStateException("Item cannot be approved unless under review.");
		}
	}

	/**
	 * Checks whether the approval process has timed out. The timeout period begins
	 * when the ApprovalGroup moves into UNDER_REVIEW state. If the timeout period has elapsed
	 * before the ApprovalGroup has been approved, then the ApprovalGroup moves back to the OPEN_FOR_EDITING state.
	 * @return true if a timeout has occurred, false otherwise.
	 */
	public synchronized boolean checkForTimeout() {
		if (keyExpires == null)
			return false;
		Date now = new Date();
		if (now.compareTo(keyExpires) > 0) {
			reset();
			approvalStatus = ApprovalStatus.OPEN_FOR_EDITING;
			return true;
		}
		return false;	
	}
	
	private void reset() {
		keyExpires  = null;
		approvalKey = null;
	}

	public Integer getId() {
		return id;
	}
}
