package com.example.myapplication.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private EventRepository mEventRepository;
    private LiveData<List<Event>> mAllEvent;

    public EventViewModel(@NonNull Application application) {
        super(application);
        mEventRepository = new EventRepository(application);
        mAllEvent = mEventRepository.getAllEvent();
    }

    public LiveData<List<Event>> getAllEvent() { return mAllEvent;}

    public void insert(Event event) {mEventRepository.insert(event);}

    public void deleteEvent(String name) {mEventRepository.deleteEvent(name);}

    public void deleteAll() {mEventRepository.deleteAll();}
}
