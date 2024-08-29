package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.provider.CategoryViewModel;
import com.example.myapplication.provider.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {
    ArrayList<Category> listCategory = new ArrayList<>();
    MyRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CategoryViewModel mCategoryViewModel;
    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        mCategoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), newData -> {
            // Convert List to ArrayList before setting it to the adapter
            ArrayList<Category> categoryArrayList = new ArrayList<>(newData);
            recyclerAdapter.setData(categoryArrayList);

            recyclerAdapter.notifyDataSetChanged();
        });
//        recyclerAdapter.setData(listCategory);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("NewEventCategory", MODE_PRIVATE);
//        String arrayListStringRestored = sharedPreferences.getString("CATEGORY_KEY", "[]");
//
//        // Retrieve the stored JSON string from SharedPreferences
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
//        ArrayList<Category> data = gson.fromJson(arrayListStringRestored, type);
//        recyclerAdapter.setData(data);
//
//        // Set the adapter to the RecyclerView
//        recyclerView.setAdapter(recyclerAdapter);

        return fragmentView;

    }

}