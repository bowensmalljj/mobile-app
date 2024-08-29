package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.telephony.SmsMessage;

import com.example.myapplication.provider.CategoryViewModel;
import com.example.myapplication.provider.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class NewEventCategory extends AppCompatActivity {
    EditText CategoryId;
    EditText CategoryName;
    EditText EventCount;
    Switch isActive;
    EditText Location;
    ArrayList<Category> listCategory=new ArrayList<>();
    MyRecyclerAdapter recyclerAdapter;
    Gson gson=new Gson();
    SharedPreferences sharedPreferences;
    private CategoryViewModel categoryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);
        sharedPreferences = getSharedPreferences("NewEventCategory", MODE_PRIVATE);


        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.SEND_SMS,
                        android.Manifest.permission.RECEIVE_SMS,
                        android.Manifest.permission.READ_SMS}, 0);


        CategoryId = findViewById(R.id.editCategoryId);
        CategoryName = findViewById(R.id.editCategoryName);
        EventCount = findViewById(R.id.editEventCount);
        isActive = findViewById(R.id.switchActive);
        Location = findViewById(R.id.editCategoryLocation);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }



    public void onButtonBroadcastClick(View view) {

        String CategoryNameString = CategoryName.getText().toString();
        String EventCountString = EventCount.getText().toString();
        String LocationString = Location.getText().toString();
        boolean boolActive = isActive.isChecked();

        if (TextUtils.isEmpty(CategoryNameString) || TextUtils.isEmpty(EventCountString)) {
            // Show error Toast for empty fields
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;

        }

        // Validate category name
        if (!isValidCategoryName(CategoryNameString)) {
            // Show error Toast for invalid category name
            Toast.makeText(this, "Invalid category name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with other operations
        String toastMsg;
        String broadCastMsg = checkIntegerBoolean(CategoryNameString, EventCountString, String.valueOf(boolActive).toUpperCase());
        if (!broadCastMsg.equals("Unknown or invalid command")) {
            generateAndSetCategoryId();
            toastMsg = "Category saved successfully: " + CategoryId.getText().toString();
            Category category = new Category(CategoryId.getText().toString(), CategoryNameString, Integer.parseInt(EventCountString), boolActive,LocationString);
//            listCategory.add(category);
//            saveArrayListAsText(listCategory);
            categoryViewModel.insert(category);
            finish();
        } else {
            toastMsg = broadCastMsg;
        }
    }


    // Function to validate the category name
    private boolean isValidCategoryName(String categoryName) {
        // Check if the category name is not empty and does not contain special characters
        return !TextUtils.isEmpty(categoryName) && !categoryName.matches(".*[-._,%!@#$^&*()+\\[\\]{}|<>/?~].*") && !categoryName.matches("\\d+");    }

    public void generateAndSetCategoryId() {
        String CategoryIdString = "C" + (char) (new Random().nextInt(26) + 'A')
                + (char) (new Random().nextInt(26) + 'A') + "-";
        for (int i = 0; i < 4; i++) {
            CategoryIdString += new Random().nextInt(10);
        }
        CategoryId.setText(CategoryIdString);
    }

    public String checkIntegerBoolean(String CategoryName, String EventCount, String boolActive) {
        String BroadCastMsg = "Unknown or invalid command";
        if (!TextUtils.isEmpty(CategoryName)) {
            if (TextUtils.isDigitsOnly(EventCount) && (boolActive.equalsIgnoreCase("TRUE") || boolActive.equalsIgnoreCase("FALSE") || boolActive.isEmpty())) {
                if (boolActive.isEmpty()) {
                    boolActive = "false";
                }
                BroadCastMsg = "category:" + CategoryName + ";" + EventCount + ";" + boolActive.toUpperCase();
            }
        }
        return BroadCastMsg;
    }

    private void saveArrayListAsText(ArrayList<Category> listCategory){
        ArrayList<Category> existingData = loadDataFromSharedPreferences();
        existingData.addAll(listCategory); // Combine existing and new categories
        String arrayListStr = gson.toJson(existingData); // Convert combined list to JSON
        SharedPreferences.Editor editor = getSharedPreferences("NewEventCategory", MODE_PRIVATE).edit(); // Use the same SharedPreferences instance
        editor.putString("CATEGORY_KEY", arrayListStr); // Store the combined list
        editor.apply();
    }
    private ArrayList<Category> loadDataFromSharedPreferences() {
        String arrListString = sharedPreferences.getString("CATEGORY_KEY", null);
        if (arrListString != null) {
            // Convert JSON string back to ArrayList<Category>
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Category>>(){}.getType();
            return gson.fromJson(arrListString, type);
        } else {
            // If no data is found, return an empty ArrayList
            return new ArrayList<>();
        }
    }
}
