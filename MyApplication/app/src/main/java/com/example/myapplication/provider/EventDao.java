package com.example.myapplication.provider;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface EventDao {
    @Query("select * from events")
    LiveData<List<Event>> getAllEvent();

    @Query("select * from events where eventName=:name")
    List<Event> getEvent(String name);

    @Insert
    void addEvent(Event event);

    @Query("delete from events where eventName=:name")
    void deleteEvent(String name);

    @Query("delete from events")
    void deleteAllEvents();
}