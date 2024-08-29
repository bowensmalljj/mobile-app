package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText loginName;
    EditText loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String previousUser = sharedPreferences.getString("NAME", "");
        if (previousUser.length()!=0){
            TextView tvName = findViewById(R.id.tvName);
            tvName.setText(previousUser);


        }

    }

    public void loginButtonClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        String nameRestored = sharedPreferences.getString("NAME", "");
        String passwordRestored = sharedPreferences.getString("PASSWORD", "");

        loginName = findViewById(R.id.tvName);
        loginPassword = findViewById(R.id.editLoginPassword);

        String enteredName = loginName.getText().toString();
        String enteredPassword = loginPassword.getText().toString();

        if (nameRestored.equals("") && passwordRestored.equals("")) {
            Toast.makeText(this, "User hasn't registered yet", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the entered username or password is empty
            if (enteredName.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                // Check if entered username and password match the stored values
                if (nameRestored.equals(enteredName) && passwordRestored.equals(enteredPassword)) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, dashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        }
//
    }


    public void registerButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }
    public void logout() {
        // Perform any necessary logout actions

        // Finish the Login activity
        finish();
    }
}