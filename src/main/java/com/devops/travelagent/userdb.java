package com.devops.travelagent;

import java.util.HashMap;

public class userdb {
	
	HashMap<String, String> db = new HashMap<String, String>();

	public void setuser(String user,String pass) {
		db.put(user, pass);
	}
	
	public String getuserpassword(String user) {
		if(db.containsKey(user)) {
				return db.get(user);
		}
		return "Invalid";
	}
	
	public boolean checkuser(String user,String pass) {
		
		if(db.containsKey(user)) {
			if(db.get(user).equals(pass)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
