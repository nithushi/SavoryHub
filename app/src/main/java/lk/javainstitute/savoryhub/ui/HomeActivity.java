package lk.javainstitute.savoryhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.adapters.BestFoodsAdapter;
import lk.javainstitute.savoryhub.adapters.CategoryAdapter;
import lk.javainstitute.savoryhub.databinding.ActivityMainBinding;
import lk.javainstitute.savoryhub.model.Category;
import lk.javainstitute.savoryhub.model.Foods;

public class HomeActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Spinner location_spinner;
    private Spinner time_spinner;
    private Spinner price_spinner;
    private FirebaseFirestore firebaseFirestore;
    private boolean isSearch = false;
    private ImageView home_search_btn ;
    private EditText home_search_text ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView logged_userText1 = findViewById(R.id.logged_userText2);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("fname");
        String lastName = intent.getStringExtra("lname");

        logged_userText1.setText("Welcome Back,  " +firstName +" "+ lastName);

        //Move to profile
        ImageView home_profile_btn = findViewById(R.id.home_profile_btn);
        home_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this , UserProfileActivity.class);
                startActivity(intent1);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        loadLocationSpinner();
        loadTimeSpinner();
        loadPriceSpinner();

        loadCategoryItems();
        loadBestFoodItems();
        setVaribale();
        search();

    }

    private void loadLocationSpinner(){
        location_spinner = findViewById(R.id.location_spinner1);
        firebaseFirestore
                .collection("Location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> locationArrayList = new ArrayList<>();
                            locationArrayList.add("Select");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                locationArrayList.add(documentSnapshot.getString("loc"));
                            }
                            ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.custom_spinner_item,locationArrayList);
                            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            location_spinner.setAdapter(locationAdapter);
                        }else{
                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void loadTimeSpinner(){
        time_spinner = findViewById(R.id.time_spinner1);
        firebaseFirestore
                .collection("Time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> timeArrayList = new ArrayList<>();
                            timeArrayList.add("Select");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                timeArrayList.add(documentSnapshot.getString("Value"));
                            }
                            ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.custom_spinner_item,timeArrayList);
                            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            time_spinner.setAdapter(timeAdapter);
                        }else{
                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void loadPriceSpinner(){
        price_spinner = findViewById(R.id.price_spinner1);
        firebaseFirestore
                .collection("Price")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> priceArrayList = new ArrayList<>();
                            priceArrayList.add("Select");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                priceArrayList.add(documentSnapshot.getString("Value"));
                            }
                            ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.custom_spinner_item,priceArrayList);
                            priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            price_spinner.setAdapter(priceAdapter);
                        }else{
                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    //load category items
    private void loadCategoryItems() {
        ProgressBar categoryProgressBar = findViewById(R.id.categoryProgressBar);
        firebaseFirestore.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Category> categoryList = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Category categoryItem = documentSnapshot.toObject(Category.class);
                                categoryList.add(categoryItem);
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
                            RecyclerView categoryRecycleView = findViewById(R.id.categoryRecycleView); // Assuming you have the RecyclerView in your layout with this ID
                            categoryRecycleView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 4)); // 2 columns
                            categoryRecycleView.setAdapter(categoryAdapter);
                            categoryProgressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(HomeActivity.this, "Error loading category items", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    //load best foods
    private void loadBestFoodItems() {

        RecyclerView bestFoodsRecycleView = findViewById(R.id.bestFoodsRecycleView);
        ProgressBar bestFoodProgressBar = findViewById(R.id.bestFoodProgressBar);
        BestFoodsAdapter bestFoodsAdapter = new BestFoodsAdapter(new ArrayList<>());
        ArrayList<Foods> foodItemList = new ArrayList<>();

        bestFoodProgressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Foods")
                .whereEqualTo("BestFood", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("HomeActivity", "Firebase query successful");
                            foodItemList.clear(); // Clear the list before adding new items
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Foods foodItem = documentSnapshot.toObject(Foods.class);
                                Log.d("HomeActivity", "Food item loaded: " + foodItem.getTitle());
                                foodItemList.add(foodItem);
                            }
                            if (foodItemList.size() > 0) {
                                Log.d("HomeActivity", "Food item list size: " + foodItemList.size());
                                bestFoodsAdapter.setItems(new ArrayList<>(foodItemList)); // Update the adapter items
                                bestFoodsRecycleView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                bestFoodsRecycleView.setAdapter(bestFoodsAdapter);
                            }
                            bestFoodProgressBar.setVisibility(View.GONE);
                        } else {
                            Log.e("HomeActivity", "Error loading food items: ", task.getException());
                            Toast.makeText(HomeActivity.this, "Error loading food items", Toast.LENGTH_SHORT).show();
                            bestFoodProgressBar.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }


    //serach btn
    private void search(){

//        ImageView home_search_btn = findViewById(R.id.home_search_btn);
//        EditText home_search_text = findViewById(R.id.home_search_text);

        home_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(home_search_text.getText());
                if(!text.isEmpty()){
                    Intent intent = new Intent(HomeActivity.this, ListFoodsActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("isSearch", true);
                    startActivity(intent);
                }
            }
        });
    }

    private void setVaribale() {

        ImageView home_cart_btn = findViewById(R.id.home_cart_btn);
        home_search_btn = findViewById(R.id.home_search_btn);
        home_search_text = findViewById(R.id.home_search_text);

        String text = String.valueOf(home_search_text.getText());
        if(!text.isEmpty()){
            Intent intent = new Intent(HomeActivity.this, ListFoodsActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("isSearch", true);
            startActivity(intent);
        }

        //cart btn
        home_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
    }
}


