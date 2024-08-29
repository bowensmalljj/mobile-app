package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.metrics.Event;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class NewEvent extends AppCompatActivity {

    EditText EventID;
    EditText EventName;
    EditText CategoryID;
    EditText TicketsAvailable;
    Switch isEventActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        EventID = findViewById(R.id.editEventId);
        EventName = findViewById(R.id.editEventName);
        CategoryID = findViewById(R.id.editEventCategoryId);
        TicketsAvailable = findViewById(R.id.editTicketsAvailable);
        isEventActive = findViewById(R.id.switchEventActive);
    }




    public void onButtonSaveEvent(View view){
        String CategoryIDString = CategoryID.getText().toString();
        String EventNameString = EventName.getText().toString();
        String ticketsAvailableString = TicketsAvailable.getText().toString();
        Boolean isActive = isEventActive.isChecked();
        Integer TicketsAvailableInt;
        // Check if the "Tickets Available" field is not empty
        if (!TextUtils.isEmpty(ticketsAvailableString)) {
            // If not empty, parse the value to an integer
            TicketsAvailableInt = Integer.parseInt(ticketsAvailableString);


        }else{TicketsAvailableInt=1;}
        String toastMsg;
        String BroadCastMsg = checkIntegerBoolean(CategoryIDString, EventNameString, TicketsAvailableInt.toString(), isActive.toString());
        if (!BroadCastMsg.equals("Unknown or invalid command")){
            generateAndSetEventId();
            toastMsg="Event saved: "+EventID.getText().toString()+" to "+CategoryIDString;
        }else{ toastMsg=BroadCastMsg;}
        Intent intent = new Intent();
        intent.setAction("HELLOTHERE");
        intent.putExtra("key", toastMsg);
        sendBroadcast(intent);


    }



    public static String checkIntegerBoolean(String EventNameString,String CategoryIDString, String TicketsAvailable, String boolActive){
        String BroadCastMsg="Unknown or invalid command";
        if (TextUtils.isEmpty(CategoryIDString)||TextUtils.isEmpty(EventNameString)) {
            BroadCastMsg = "Unknown or invalid command";
        } else {
            // Convert boolActive to uppercase for consistent comparison

            // Check if EventCountString contains only digits and boolActive is either "TRUE" or "FALSE"
            if ((boolActive.equalsIgnoreCase("true") || boolActive.equalsIgnoreCase("false"))) {
                // Construct the broadcast message
                if (Integer.parseInt(TicketsAvailable)<=0){
                    BroadCastMsg = "Unknown or invalid command";
                }
                else {
                    BroadCastMsg = "event:" + EventNameString + ";" + CategoryIDString + ";" + TicketsAvailable + ";" + boolActive.toUpperCase();
                }
            }else {
                // Invalid input, set a message indicating that
                BroadCastMsg = "Unknown or invalid command";
            }
        }

        return BroadCastMsg;
    }
    public void generateAndSetEventId() {
        String EventIdString = "E";

        // Append two random uppercase letters
        EventIdString += (char) (new Random().nextInt(26) + 'A');
        EventIdString += (char) (new Random().nextInt(26) + 'A');

        // Add a hyphen
        EventIdString += "-";

        // Generate 4 random digits
        for (int i = 0; i < 5; i++) {
            EventIdString += (new Random().nextInt(10));
        }

        // Set the generated CategoryIdString to the CategoryId EditText
        EventID.setText(EventIdString);
    }

    private void saveDataToSharedPreference(String EventID, String EventName, String CategoryId,Integer TicketsAvailable, Boolean isEventActive) {
//        private void saveDataToSharedPreference(String CategoryId) {
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("EventForm", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("EVENTID", EventID);
        editor.putString("EVENTNAME", EventName);
        editor.putString("CATEGORYID", CategoryId);
        editor.putInt("EVENTCOUNT", TicketsAvailable);
        editor.putBoolean("ISACTIVE", isEventActive);

        // use editor.apply() to save data to the file asynchronously (in background without freezing the UI)
        // doing in background is very common practice for any File Input/Output operations
        editor.apply();
    }
}