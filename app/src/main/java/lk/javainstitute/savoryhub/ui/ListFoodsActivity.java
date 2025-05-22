package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.adapters.FoodListAdapter;
import lk.javainstitute.savoryhub.model.Foods;

public class ListFoodsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private int categoryId;
    private String categoryName;

    private String searchText;
    private boolean isSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_foods);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        initList();

    }

    private void initList() {
        RecyclerView initFoodsRecycleView = findViewById(R.id.foodsListRecycleView);
        ProgressBar initfoodsProgressBar = findViewById(R.id.listFoodProgressBar);
        ArrayList<Foods> initFoodsArrayList = new ArrayList<>();
        FoodListAdapter foodListAdapter = new FoodListAdapter(new ArrayList<>());

        firebaseFirestore = FirebaseFirestore.getInstance();

        initfoodsProgressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Foods")
                .whereEqualTo("CategoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Foods foods = documentSnapshot.toObject(Foods.class);
                                Log.d("ListActivity", "Food item loaded: " + foods.getTitle());
                                initFoodsArrayList.add(foods);
                            }

                            if(initFoodsArrayList.size() > 0){
                                Log.d("ListActivity", "Food item list size: " + initFoodsArrayList.size());
                                initFoodsRecycleView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                                FoodListAdapter foodListAdapter = new FoodListAdapter(initFoodsArrayList);
                                initFoodsRecycleView.setAdapter(foodListAdapter);
                            }else{
                                //Toast.makeText(ListFoodsActivity.this, "No foods found for the search text.. " + searchText, Toast.LENGTH_SHORT).show();
                            }
                            foodListAdapter.setItems(new ArrayList<>(initFoodsArrayList));

                        }else{
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                        }
                        initfoodsProgressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore Error", "Error getting documents: ", e);
                    }
                }
        );

    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("CategoryId",0);
        categoryName = intent.getStringExtra("CategoryName");

        Log.d("ListFoodsActivity", "Received Category ID: " + categoryId + ", Category Name: " + categoryName);

        //searchText = intent.getStringExtra("text");
        //isSearch = getIntent().getBooleanExtra("isSearch", false);

        TextView categoryTitleTxt = findViewById(R.id.list_foods_titleTxt);
        if (categoryName != null) {
            categoryTitleTxt.setText(categoryName);
        }

        searchText = intent.getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);
    }

}
