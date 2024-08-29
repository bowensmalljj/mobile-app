package com.example.myapplication.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {

    private CategoryDao mCategoryDao;

    private LiveData<List<Category>> mAllCategory;

    CategoryRepository (Application application) {
        EMADatabase db = EMADatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategory = mCategoryDao.getAllCategory();
    }

    LiveData<List<Category>> getAllCategory () {
        return mAllCategory;
    }
    void update(Category category) {
        EMADatabase.databaseWriteExecutor.execute(() -> mCategoryDao.updateCategory(category));
    }
    void insert(Category category) {
        EMADatabase.databaseWriteExecutor.execute(() -> mCategoryDao.addCategory(category));
    }

    void deleteCategory (String name) {
        EMADatabase.databaseWriteExecutor.execute(() -> mCategoryDao.deleteCategory(name));
    }

    void deleteAll(){
        EMADatabase.databaseWriteExecutor.execute(() -> mCategoryDao.deleteAllCategories());
    }
    public LiveData<Boolean> ifCategoryIDExist(String categoryID){
        return mCategoryDao.ifCategoryIDExist(categoryID);
    }
}
