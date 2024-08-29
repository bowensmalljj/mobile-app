package com.example.myapplication.provider;

import android.widget.EditText;
import android.widget.Switch;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "categoryID")
    private String CategoryId;
    @ColumnInfo(name = "categoryName")
    private String Name;
    @ColumnInfo(name = "eventCount")
    private int Event;
    @ColumnInfo(name = "categoryActive")
    private boolean Active;
    @ColumnInfo(name = "categoryLocation")
    private String Location;
    public Category(){}


    public Category(String id, String name, int event, boolean active,String location) {
        this.CategoryId = id;
        this.Name = name;
        this.Event = event;
        this.Active = active;
        this.Location=location;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getEvent() {
        return Event;
    }

    public void setEvent(int event) {
        Event = event;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }
}

