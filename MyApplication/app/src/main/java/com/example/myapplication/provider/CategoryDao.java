package com.example.myapplication.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("select * from categories")
    LiveData<List<Category>> getAllCategory();

    @Query("select * from categories where categoryName=:name")
    List<Category> getCategory(String name);

    @Insert
    void addCategory(Category category);

    @Query("delete from categories where categoryName=:name")
    void deleteCategory(String name);

    @Query("delete from categories")
    void deleteAllCategories();

    @Query("select COUNT(*) > 0 from categories where categoryId = :categoryID")
    LiveData<Boolean> ifCategoryIDExist(String categoryID);

    @Update
    void updateCategory(Category category);
}
