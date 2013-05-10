package com.example.soccerman;

import com.example.soccerman.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity 
{
	Button btnSignIn,btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.main);

	     // create a instance of SQLite Database
	     loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();

	     // Get The Reference Of Buttons
	     btnSignIn=(Button)findViewById(R.id.buttonSignIN);
	     btnSignUp=(Button)findViewById(R.id.buttonSignUP);

	     SharedPreferences pref = getApplicationContext().getSharedPreferences("com.example.soccerman", 0);
	     Editor editor = pref.edit();
	     editor.remove("userId");
	     editor.remove("username");
	     editor.commit();
	     
	    // Set OnClick Listener on SignUp button 
	    btnSignUp.setOnClickListener(new View.OnClickListener() 
	    {
			public void onClick(View v) 
			{
				/// Create Intent for SignUpActivity abd Start The Activity
				Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});
	}
	// Method to handleClick Event of Sign In Button
	public void signIn(View V)
	{
		final Dialog dialog = new Dialog(HomeActivity.this);
		dialog.setContentView(R.layout.login);
	    dialog.setTitle("Login");

	    // get the References of views
	    final  EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
	    final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

		Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);

		// Set On ClickListener
		btnSignIn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// get The User name and Password
				String userName=editTextUserName.getText().toString();
				String password=editTextPassword.getText().toString();

				// fetch the Password form database for respective user name
				String storedPassword=loginDataBaseAdapter.getUserPassword(userName);

				// check if the Stored password matches with  Password entered by user
				if(password.equals(storedPassword))
				{
					int pass = loginDataBaseAdapter.getUserId(userName);
					SharedPreferences pref = getApplicationContext().getSharedPreferences("com.example.soccerman", 0);
				    Editor editor = pref.edit();
				    editor.putInt("userId", pass);
				    editor.putString("username", userName);
				    editor.commit();
				     
					Toast.makeText(HomeActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
					dialog.dismiss();
					
					Intent home = new Intent(HomeActivity.this, ListMatchesActivity.class);
					startActivity(home);
				}
				else
				{
					Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		loginDataBaseAdapter.close();
	}
}