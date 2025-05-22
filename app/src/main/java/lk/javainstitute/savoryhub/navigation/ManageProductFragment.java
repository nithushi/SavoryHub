package lk.javainstitute.savoryhub.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lk.javainstitute.savoryhub.R;

public class ManageProductFragment extends Fragment {

    private EditText searchText;
    private ImageView searchBtn;
    private ImageView productImage;
    private TextView title;
    private TextView description;
    private TextView price;
    private TextView categoryId;
    private TextView availability;
    private TextView delivery;
    private TextView id;

    private LinearLayout linearLayout;

    private TextView stock;

    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_manage_product, container, false);


        searchText = rootView.findViewById(R.id.manageuser_search_text);
        searchBtn = rootView.findViewById(R.id.manageuser_search_btn);
        productImage = rootView.findViewById(R.id.admin_product_image);
        title = rootView.findViewById(R.id.product_details_title);
        description = rootView.findViewById(R.id.admin_product_detail_description);
        price = rootView.findViewById(R.id.admin_product_detail_price);
        categoryId = rootView.findViewById(R.id.admin_product_category);
        availability = rootView.findViewById(R.id.admin_product_detail_availability);
        delivery =rootView.findViewById(R.id.admin_product_delivery);
        id=rootView.findViewById(R.id.admin_product_detail_id);
        linearLayout = rootView.findViewById(R.id.product_linearLayout_1);
        stock = rootView.findViewById(R.id.admin_product_stock_label);
        firebaseFirestore = FirebaseFirestore.getInstance();

        linearLayout.setVisibility(View.GONE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = String.valueOf(searchText.getText()).trim();
                if(!productName.isEmpty()){
                    fetchProductDetails(productName);
                }else{
                    Log.i("ManageProductFragment","Enter Product Name");
                }
            }
        });

        return rootView;
    }

    private void fetchProductDetails(String productName) {
        firebaseFirestore
                .collection("Foods")
                .where(Filter.equalTo("Title", productName))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(! task.getResult().isEmpty()){
                                DocumentSnapshot document= task.getResult().getDocuments().get(0);
                                Log.d("ManageProductFragment", "Product found: " + document.getString("Title"));

                                title.setText(document.getString("Title"));
                                description.setText(document.getString("Description"));
                                price.setText(String.valueOf("$"+ document.getDouble("Price")));
                                availability.setText("In Stock");
                                delivery.setText("$10");
                                id.setText(String.valueOf("P00"+document.getLong("Id")));

                                long qty = document.getLong("Qty");
                                if(qty<= 0){
                                    availability.setText("Out of Stock");
                                    availability.setBackgroundResource(R.drawable.outstock_background);
                                }else{
                                    availability.setText("In Stock");
                                    availability.setBackgroundResource(R.drawable.instock_background);
                                }

                                String imagePath = document.getString("ImagePath");
                                int drawableResourceId = getContext().getResources().getIdentifier(imagePath, "drawable", getContext().getPackageName());

                                Glide.with(getContext())
                                        .load(drawableResourceId)
                                        .transform(new CenterCrop(), new RoundedCorners(20))
                                        .into(productImage);

                                long categoryValue = document.getLong("CategoryId");
                                fetchCategoryName(categoryValue);


                                linearLayout.setVisibility(View.VISIBLE);
                                searchText.setText("");

                            }else{

                            }
                        }else{
                            Log.e("ManageProductFragment", "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ManageProductFragment","Fail to fetch details");
                    }
                }
        );
    }

    private void fetchCategoryName(long categoryValue) {
        firebaseFirestore
                .collection("Category")
                .document(String.valueOf(categoryValue))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String categoryName = document.getString("Name");
                                categoryId.setText(categoryName); // Set the category name in the TextView
                            } else {
                                Log.i("ManageProductFragment", "No such category found");
                            }
                        } else {
                            Log.e("ManageProductFragment", "Error getting category: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ManageProductFragment", "Fail to fetch category name");
                    }
                }
        );
    }

}
