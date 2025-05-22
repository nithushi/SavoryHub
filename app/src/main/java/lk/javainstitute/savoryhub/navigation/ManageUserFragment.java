package lk.javainstitute.savoryhub.navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.ui.OrderTrackActivity;

public class ManageUserFragment extends Fragment {

    private TextView fullName, mobile, nic, gender, address, joinedDate, status;
    private ImageView pic, searchBtn , call;
    private EditText searchTxt;
    private Button registerRiderBtn;
    private FirebaseFirestore firebaseFirestore;
    private ConstraintLayout layout1, layout2;
    private LinearLayout layout3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_user, container, false);

        searchTxt = view.findViewById(R.id.manageuser_search_text);
        searchBtn =view.findViewById(R.id.manageuser_search_btn);
        fullName = view.findViewById(R.id.rider_name);
        mobile = view.findViewById(R.id.rider_mobile);
        nic = view.findViewById(R.id.rider_nic);
        gender = view.findViewById(R.id.rider_gender);
        address = view.findViewById(R.id.rider_address);
        joinedDate = view.findViewById(R.id.rider_joined_date);
        status = view.findViewById(R.id.rider_status);
        pic = view.findViewById(R.id.rider_pic);

        call= view.findViewById(R.id.user_call_img);

        firebaseFirestore = FirebaseFirestore.getInstance();
        layout1=view.findViewById(R.id.constraint_layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);

        registerRiderBtn = view.findViewById(R.id.register_rider_btn);


        layout1.setVisibility(View.GONE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String riderNICTxt = searchTxt.getText().toString();
                if (!riderNICTxt.isEmpty()) {
                    try {
                        long riderNIC = Long.parseLong(riderNICTxt);
                        Log.d("ManageUserFragment", "Retrieved NIC: " + riderNIC);
                        fetchRiderDetails(riderNIC);
                    } catch (NumberFormatException e) {
                        Log.e("ManageUserFragment", "Invalid NIC format");
                    }
                } else {
                    Log.e("ManageUserFragment", "NIC field is empty");
                }
            }
        });

        //open register fragment
        registerRiderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);

                RegisterRiderFragment registerRiderFragment = new RegisterRiderFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, registerRiderFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // call
//        call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(Intent.ACTION_CALL);
//                Uri uri = Uri.parse();
//                intent1.setData(uri);
//
//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    Log.i("ManageUserFragment", "Requesting CALL_PHONE permission");
//                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
//                } else {
//                    Log.i("ManageUserFragment", "Starting call activity");
//                    startActivity(intent1);
//                }
//            }
//        });



        return view;
    }

    private void fetchRiderDetails(long riderNIC) {
        firebaseFirestore.collection("Riders")
                .whereEqualTo("NIC", riderNIC)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                Log.d("ManageUserFragment", "Document data: " + document.getData());

                                try {
                                    fullName.setText(document.getString("Fname") + " " + document.getString("Lname"));
                                    mobile.setText(String.valueOf(document.getLong("Mobile")));
                                    nic.setText(String.valueOf(document.getLong("NIC")));
                                    gender.setText(document.getString("Gender"));
                                    address.setText(document.getString("Lane1")+" "+document.getString("Lane2"));
                                    joinedDate.setText(document.getString("Joined"));

                                    long statusId = document.getLong("Status_Id");
                                    if(statusId == 0){
                                        status.setText("Active");
                                        status.setBackgroundResource(R.drawable.instock_background);
                                    }else{
                                        status.setText("Inactive");
                                        status.setBackgroundResource(R.drawable.outstock_background);
                                    }

                                    String imagePath = document.getString("Pic");
                                    int drawableResourceId = getContext().getResources().getIdentifier(imagePath, "drawable", getContext().getPackageName());

                                    Glide.with(getContext())
                                            .load(drawableResourceId)
                                            .transform(new CenterCrop(), new RoundedCorners(200))
                                            .into(pic);

                                    layout1.setVisibility(View.VISIBLE);
                                    searchTxt.setText("");
                                } catch (Exception e) {
                                    Log.e("ManageUserFragment", "Error fetching details", e);
                                }
                            } else {
                                Toast.makeText(getContext() , "User Not Found", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e("ManageUserFragment", "Error getting documents: ", task.getException());
                        }
                    }
                }
        );
    }

}
