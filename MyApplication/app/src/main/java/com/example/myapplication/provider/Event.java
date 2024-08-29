package com.example.myapplication.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "eventID")
    private String EventId;
    @ColumnInfo(name = "eventName")
    private String EventName;
    @ColumnInfo(name = "categoryID")
    private String CategoryId;
    @ColumnInfo(name = "ticketsAvailable")
    private int ticketsAvailable;
    @ColumnInfo(name = "eventActive")
    private boolean isEventActive;
    public Event(){}
    public Event(String eventId, String eventName, String categoryId, int ticketsAvailable, boolean isEventActive) {
        EventId = eventId;
        EventName = eventName;
        CategoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isEventActive = isEventActive;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public boolean isEventActive() {
        return isEventActive;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }
}
