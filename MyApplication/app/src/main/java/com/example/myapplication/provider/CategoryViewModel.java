package com.example.myapplication.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository mCategoryRepository;
    private LiveData<List<Category>> mAllCategory;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mCategoryRepository = new CategoryRepository(application);
        mAllCategory = mCategoryRepository.getAllCategory();
    }

    public LiveData<List<Category>> getAllCategory() { return mAllCategory;}

    public void insert(Category category) {mCategoryRepository.insert(category);}

    public void deleteCategory(String name) {mCategoryRepository.deleteCategory(name);}
    public void update(Category category) {mCategoryRepository.update(category);}
    public void deleteAll() {mCategoryRepository.deleteAll();}
    public LiveData<Boolean> ifCategoryIDExist(String categoryID){
        return mCategoryRepository.ifCategoryIDExist(categoryID);
        }
    }
