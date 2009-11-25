package com.itao.starlite.auth;

public class PermissionTester {
	public static boolean hasPermission(String permissionsString, User u) {
		if (permissionsString == null || permissionsString.equals(""))
			return true;
		if (u == null)
			return false;
		
		String[] permissions = permissionsString.split("\\|\\|");
		for (String p: permissions) {
			if (u.hasPermission(p.trim()))
				return true;
		}
		return false;
	}
}
