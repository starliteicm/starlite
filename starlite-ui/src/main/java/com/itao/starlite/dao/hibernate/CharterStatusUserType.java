package com.itao.starlite.dao.hibernate;

import com.itao.persistence.EnumUserType;
import com.itao.starlite.model.types.CharterStatus;

public class CharterStatusUserType extends EnumUserType<CharterStatus>{

	protected CharterStatusUserType() {
		super(CharterStatus.class);
	}

}
