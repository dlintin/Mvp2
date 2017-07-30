package com.dmovies.diand.diandmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SharedPref {
	
	// Shared Preferences reference
	SharedPreferences pref;
	
	// Editor reference for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREFER_NAME = "AndroidExamplePref";
	
	// All Shared Preferences Keys
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_ID = "name";

	// Constructor
	public SharedPref(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	//Create login session
	public void createUserLoginSession(String name){
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_ID, name);
		

		// commit changes
		editor.commit();
	}	
	

	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		
		//Use hashmap to store user credentials
		HashMap<String, String> user = new HashMap<String, String>();
		
		// user name
		user.put(KEY_ID, pref.getString(KEY_ID, null));

		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		
		// Clearing all user data from Shared Preferences
		editor.clear();
		editor.commit();

	}
	
	
	// Check for login
	public boolean isUserLoggedIn(){
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
}
