package com.example.myapplication;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.provider.Category;
import com.example.myapplication.provider.Event;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import com.example.myapplication.provider.CategoryViewModel;
import com.example.myapplication.provider.Category;
import com.example.myapplication.provider.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import com.google.android.material.navigation.NavigationView;

public class dashboard extends AppCompatActivity {
    EditText EventID;
    EditText EventName;
    EditText CategoryID;
    EditText TicketsAvailable;
    int modified_eventcount;
    Switch isEventActive;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Gson gson=new Gson();
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    ArrayList<Event> listEvent=new ArrayList<>();
    EventRecyclerAdapter eventRecyclerAdapter;
    MyRecyclerAdapter recyclerAdapter;
    private CategoryViewModel categoryViewModel;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);
        sharedPreferences = getSharedPreferences("NewEvent", MODE_PRIVATE);
        recyclerAdapter = new MyRecyclerAdapter();
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
//        categoryViewModel.deleteAll();
        categoryViewModel.getAllCategory().observe(this, newData -> {
            recyclerAdapter.setData((ArrayList<Category>) newData);
            recyclerAdapter.notifyDataSetChanged();
        });
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        createHeader();
        getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();

        FragmentListCategory fragmentListCategory = FragmentListCategory.newInstance("param1", "param2");
        getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, fragmentListCategory).commit();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventID = findViewById(R.id.editEventId);
                EventName = findViewById(R.id.editEventName);
                CategoryID = findViewById(R.id.editEventCategoryId);
                TicketsAvailable = findViewById(R.id.editTicketsAvailable);
                isEventActive = findViewById(R.id.switchEventActive);

                // Perform validations
                if (TextUtils.isEmpty(EventName.getText().toString()) ||
                        TextUtils.isEmpty(CategoryID.getText().toString()) ||
                        TextUtils.isEmpty(TicketsAvailable.getText().toString())) {
                    // Show error Toast for empty fields
                    Toast.makeText(dashboard.this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                if (!isValidEventName(EventName.getText().toString())) {
                    // Show error Toast for invalid event name
                    Toast.makeText(dashboard.this, "Invalid event name", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                if (CategoryID.getText().toString().isEmpty()) {
                    Toast.makeText(dashboard.this, "Category ID is required", Toast.LENGTH_SHORT).show();
                    return;

                }

                // Create and save the event if all validations pass
                Snackbar.make(view, "Item added", Snackbar.LENGTH_LONG).setAction("Undo", undoOnClickListener).show();

                LiveData<Boolean> categoryExists = categoryViewModel.ifCategoryIDExist(CategoryID.getText().toString());
                categoryExists.observe(dashboard.this, exists -> {
                    if (exists != null && exists) {
                        // Category exists, proceed with saving the event
                        generateAndSetEventId();
                        Event event = new Event(EventID.getText().toString(), EventName.getText().toString(), CategoryID.getText().toString(), Integer.parseInt(TicketsAvailable.getText().toString()), isEventActive.isChecked());
                        eventViewModel.insert(event);
                        categoryViewModel.getAllCategory().observe(dashboard.this, categories -> {
                            Category matchedCategory = categories.stream()
                                    .filter(c -> c.getCategoryId().equals( CategoryID.getText().toString()))
                                    .findFirst()
                                    .orElse(null);

                            if (matchedCategory != null) {
                                updateCategoryEventCount(matchedCategory, 1);
                                Snackbar.make(fab, "Event saved successfully", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", view1 -> undoSaveEvent(event, matchedCategory))
                                        .show();
                                Toast.makeText(dashboard.this, "Event saved: " + EventID.getText().toString() + " to " + CategoryID.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                            // remove observers here to prevent multiple updates
                            categoryViewModel.getAllCategory().removeObservers(dashboard.this);
                        });
                    } else {
                        Toast.makeText(dashboard.this, "Category ID does not exist", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the observer to prevent this from being called again
                    categoryExists.removeObservers(dashboard.this);
                });

//                listEvent.add(event);
//                saveArrayListAsText(listEvent);
//                eventViewModel.insert(event);

            }
        });

    }
    private void updateCategoryEventCount(Category category, int increment) {
        int newEventCount = category.getEvent() + increment;
        category.setEvent(newEventCount);
        categoryViewModel.update(category); // Assuming update method is thread-safe or handles concurrency appropriately
    }
    private void undoSaveEvent(Event event, Category matchedCategory) {
        // Remove the event using ViewModel
        eventViewModel.deleteEvent(String.valueOf(event.getEventName()));

        // Decrement the event count in the category
        matchedCategory.setEvent(matchedCategory.getEvent() - 1);
        categoryViewModel.update(matchedCategory);
//        listEvent.clear();
        getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();

        Toast.makeText(this, "Event save undone", Toast.LENGTH_SHORT).show();
    }
    private void generateAndSetEventId() {
        // Generate random characters
        char randomChar1 = (char) (new Random().nextInt(26) + 'A'); // Random uppercase letter
        char randomChar2 = (char) (new Random().nextInt(26) + 'A'); // Random uppercase letter

        // Generate random digits
        String randomDigits = String.format("%05d", new Random().nextInt(100000)); // Random 5-digit number

        // Build the event ID
        String eventId = "E" + randomChar1 + randomChar2 + "-" + randomDigits;

        // Set the generated event ID to the TextView
        EventID.setText(eventId);
    }
    // Function to validate the event name
    private boolean isValidEventName(String eventName) {
        // Check if the event name is not empty and does not contain only digits
        return !TextUtils.isEmpty(eventName) && !eventName.matches("^\\d+$");
    }

    // Function to check if the category exists
    private boolean categoryExists(String categoryId,String operator) {
        // Retrieve the stored categories from SharedPreferences
        sharedPreferences = getSharedPreferences("NewEventCategory", MODE_PRIVATE);
        String arrayListStringRestored = sharedPreferences.getString("CATEGORY_KEY", "[]");

        // Convert the JSON string back to an ArrayList<Category>
        Type type = new TypeToken<ArrayList<Category>>(){}.getType();
        ArrayList<Category> existingCategories = gson.fromJson(arrayListStringRestored, type);

        // Check if the provided categoryId exists in the list of existing categories
        for (Category category : existingCategories) {
            if (category.getCategoryId().equals(categoryId)) {
                if (operator.equals("add")) {
                    category.setEvent(category.getEvent()+1);
                    saveCategoriesToSharedPreferences(existingCategories);
                    getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();
                    return true;
                } else {
                    category.setEvent(category.getEvent()-1);
                    listEvent.remove(listEvent.size() - 1);
                    saveArrayListAsText(listEvent);
                    saveCategoriesToSharedPreferences(existingCategories);
                    getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();
                    return true;
                }
            }


        }

        return false; // Category does not exist

    }




    View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            categoryExists(CategoryID.getText().toString(),"remove");

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_event_form) {
            // Do something
            EventID = findViewById(R.id.editEventId);
            EventName = findViewById(R.id.editEventName);
            CategoryID = findViewById(R.id.editEventCategoryId);
            TicketsAvailable = findViewById(R.id.editTicketsAvailable);
            isEventActive = findViewById(R.id.switchEventActive);

            EventID.setText("");
            EventName.setText("");
            CategoryID.setText("");
            TicketsAvailable.setText("");
            isEventActive.setChecked(false);

        } else if (id == R.id.delete_all_categories) {
            // Do something
//            clearSharedPreferences("NewEventCategory");
            deleteAllCategories();
        }else if (id == R.id.delete_all_events) {
            // Do something
//            clearSharedPreferences("NewEvent");
            deleteAllEvents();
        }else if (id == R.id.refresh) {
            // Do something
            getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();
        }

        // tell the OS
        return true;
    }
    private void deleteAllEvents(){
        eventViewModel.deleteAll();
    }
    private void deleteAllCategories() {
        // Delete all categories using ViewModel
        categoryViewModel.deleteAll();
        Category header = new Category("id","name",0,false,"location");
////        ArrayList<Category> currentCategories = getCategoriesFromSharedPreferences();
//            // Insert a new header category after deletion
        categoryViewModel.insert(header);

        // Replace the current fragment to refresh the list
        getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.list_cateogry_activity) {
                listCategoryButtonClick(null);
            } else if (id == R.id.view_all_events) {
                listEventButtonClick(null);
            } else if (id == R.id.add_category){
                newEventCategoryButtonClick(null);
            }
             else if (id == R.id.log_out){
                logoutButtonClick(null);
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    public void newEventCategoryButtonClick(View view){
        Intent intent = new Intent(this, NewEventCategory.class);

        startActivity(intent);
    }
    public void addEventButtonClick(View view){
        Intent intent = new Intent(this, NewEvent.class);

        startActivity(intent);
    }
    public void listCategoryButtonClick(View view){
        Intent intent = new Intent(this, ListCategoryActivity.class);
        startActivity(intent);
    }
    public void listEventButtonClick(View view){
        Intent intent = new Intent(this, ListEventActivity.class);
        startActivity(intent);
    }

    public void logoutButtonClick(View view){
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void clearSharedPreferences(String name) {
//        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
//        sharedPreferences.edit().clear().apply();
        categoryViewModel.deleteAll();
//        listEvent.clear();
//        createHeader();
//        getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            // Insert a new header category after deletion
//            Category header = new Category("Header ID", "Header Category", 0, false, "Header Location");
//            categoryViewModel.insert(header);
////
////            // Replace the current fragment to refresh the list
//            getSupportFragmentManager().beginTransaction().replace(R.id.hostcontainer, new FragmentListCategory()).commit();
//
//        }, 500);
    }
    private void saveArrayListAsText(ArrayList<Event> listEvent){
        ArrayList<Event> existingData = loadDataFromSharedPreferences();
        existingData.addAll(listEvent); // Combine existing and new categories
        String arrayListStr = gson.toJson(existingData); // Convert combined list to JSON
        SharedPreferences.Editor editor = getSharedPreferences("NewEvent", MODE_PRIVATE).edit(); // Use the same SharedPreferences instance
        editor.putString("EVENT_KEY", arrayListStr); // Store the combined list
        editor.apply();
    }
    private void saveCategoriesToSharedPreferences(ArrayList<Category> categories) {
        // Convert the ArrayList<Category> to JSON string
        sharedPreferences = getSharedPreferences("NewEventCategory", MODE_PRIVATE);
        String jsonCategories = gson.toJson(categories);

        // Save the JSON string back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CATEGORY_KEY", jsonCategories);
        editor.apply();
    }

    private ArrayList<Event> loadDataFromSharedPreferences() {
        String arrListString = sharedPreferences.getString("EVENT_KEY", null);
        if (arrListString != null) {
            // Convert JSON string back to ArrayList<Category>
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Event>>(){}.getType();
            return gson.fromJson(arrListString, type);
        } else {
            // If no data is found, return an empty ArrayList
            return new ArrayList<>();
        }
    }

    public void createHeader() {
        LiveData<List<Category>> categoriesLiveData = categoryViewModel.getAllCategory();

        categoriesLiveData.observe(this, categories -> {
            if (categories == null || categories.isEmpty()) {
                // No categories exist, insert header
                Category header = new Category("id","name",0,false,"location");
                categoryViewModel.insert(header);
                // Remove observer after inserting the header
//                categoriesLiveData.removeObservers(this);
            }

        });
    }
    private ArrayList<Category> getCategoriesFromSharedPreferences() {
        // Retrieve the stored categories from SharedPreferences
        sharedPreferences = getSharedPreferences("NewEventCategory", MODE_PRIVATE);
        String arrayListStringRestored = sharedPreferences.getString("CATEGORY_KEY", "[]");

        // Convert the JSON string back to an ArrayList<Category>
        Type type = new TypeToken<ArrayList<Category>>(){}.getType();
        return gson.fromJson(arrayListStringRestored, type);
    }
}