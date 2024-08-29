package com.example.myapplication.provider;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {
    private EventDao mEventDao;

    private LiveData<List<Event>> mAllEvent;

    EventRepository (Application application) {
        EMADatabase db = EMADatabase.getDatabase(application);
        mEventDao = db.eventDao();
        mAllEvent = mEventDao.getAllEvent();
    }

    LiveData<List<Event>> getAllEvent () {
        return mAllEvent;
    }

    void insert(Event event) {
        EMADatabase.databaseWriteExecutor.execute(() -> mEventDao.addEvent(event));
    }

    void deleteEvent (String name) {
        EMADatabase.databaseWriteExecutor.execute(() -> mEventDao.deleteEvent(name));
    }

    void deleteAll() {
        EMADatabase.databaseWriteExecutor.execute(() -> mEventDao.deleteAllEvents());
    }
}

