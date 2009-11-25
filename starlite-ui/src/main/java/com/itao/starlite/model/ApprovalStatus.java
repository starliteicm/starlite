package com.itao.starlite.model;

public enum ApprovalStatus {
	/**
	 * Indicates that the item is open for editing, and has not yet been approved.
	 */
	OPEN_FOR_EDITING,
	
	/**
	 * Indicates that the item is being reviewed for approval. No changes can be made to the item, unless it is re-opened for editing.
	 */
	UNDER_REVIEW,
	
	/**
	 * Indicates that the item has been approved. No changes are possible, unless the item is re-opened for editing.
	 */
	APPROVED
}
