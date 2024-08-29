package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    EditText StudentName;
    EditText Password;
    EditText PasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.app_bar_layout);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
        setContentView(R.layout.activity_main);
    }
    public void SignUpButtonClick(View view){
        // get reference to the UI elements
        // findViewById method looks for elements by the Id we set on elements
        // and search for them on current Activity's UI
        StudentName = findViewById(R.id.editName);
        Password = findViewById(R.id.editPassword);
        PasswordConfirmation = findViewById(R.id.editConfirmPassword);

        // using the referenced UI elements we extract values into plain text format
        String nameString = StudentName.getText().toString();
        String passwordString = Password.getText().toString();
        String confirmPasswordString = PasswordConfirmation.getText().toString();


        // display a Toast message using makeText method, with three parameters explained below
        // 1. context – The context to use. Usually your android.app.Application or android.app.Activity object.
        // 2. text – The text to show. Can be formatted text.
        // 3. duration – How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
        if (nameString.length() == 0 || passwordString.length() == 0 || confirmPasswordString.length() == 0) {
            Toast.makeText(this, "Invalid password or username", Toast.LENGTH_SHORT).show();
        }
        else if (passwordString.equals(confirmPasswordString)) {
            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);

            // call save the new JAVA method to save data to persistent storage
            saveDataToSharedPreference(nameString, passwordString);

            // put some data in the same transaction before we perform launch new activity
            // data is need to be put in key-value pairs
            // both sender and destination code should know the keys to set/extract values

            // finally launch the activity using startActivity method
            startActivity(intent);
        }

        else if (passwordString.equals(confirmPasswordString)==false) {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();

        }




    }
    public void LoginButtonClick(View view){
        Intent intent = new Intent(this, Login.class);

        // finally launch the activity using startActivity method
        startActivity(intent);
    }
    private void saveDataToSharedPreference(String nameValue, String passwordValue){
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("NAME", nameValue);
        editor.putString("PASSWORD", passwordValue);

        // use editor.apply() to save data to the file asynchronously (in background without freezing the UI)
        editor.apply();

    }

}

