package com.jjm.android.quizanoi.activity;
import roboguice.activity.RoboPreferenceActivity;
import android.os.Bundle;

import com.jjm.android.quizanoi.R;

public class Settings extends RoboPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings); 
	}
 


}
