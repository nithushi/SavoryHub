package lk.javainstitute.savoryhub.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.ui.SignUpActivity;

public class RegisterRiderFragment extends Fragment {

    private EditText fnameTxt, lnameTxt, mobileTxt, nicTxt, lane1Txt, lane2Txt;
    private Button registerBtn;
    private RadioButton radioButton;
    private RadioGroup radioGroup1;
    private FirebaseFirestore firebaseFirestore;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_rider, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        fnameTxt = view.findViewById(R.id.rider_rfname);
        lnameTxt = view.findViewById(R.id.rider_rlname);
        mobileTxt = view.findViewById(R.id.rider_rmobile);
        nicTxt = view.findViewById(R.id.rider_rnic);
        lane1Txt =view.findViewById(R.id.rider_rlane1);
        lane2Txt = view.findViewById(R.id.rider_rlane2);

        registerBtn = view.findViewById(R.id.rider_register_btn);

        radioGroup1 = view.findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                Log.i("App18Log", String.valueOf(radioButton.getText()));
                gender = String.valueOf(radioButton.getText());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveRiderData();

            }
        });


        return view;
    }

    private void SaveRiderData() {

        String fname = String.valueOf(fnameTxt.getText());
        String lname = String.valueOf(lnameTxt.getText());
        String mobileStr = String.valueOf(mobileTxt.getText());
        String nicStr = String.valueOf(nicTxt.getText());
        String lane1  = String.valueOf(lane1Txt.getText());
        String lane2 = String.valueOf(lane2Txt.getText());

        if (fname.isEmpty()){
            Toast.makeText(getContext(), "First Name is required", Toast.LENGTH_SHORT).show();
            
        } else if (lname.isEmpty()) {
            Toast.makeText(getContext(), "Last Name is required", Toast.LENGTH_SHORT).show();

        } else if (mobileStr.isEmpty()) {
            Toast.makeText(getContext(), "Mobile is required", Toast.LENGTH_SHORT).show();

        } else if (!mobileStr.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$")) {
            Toast.makeText(getContext(), "Enter Validate Mobile", Toast.LENGTH_LONG).show();

        }  else if (nicStr.isEmpty()) {
            Toast.makeText(getContext(), "NIC is required", Toast.LENGTH_SHORT).show();

        } else if (!nicStr.matches("^(([5,6,7,8,9]{1})([0-9]{1})([0,1,2,3,5,6,7,8]{1})([0-9]{6})([v|V|x|X]))|(([1,2]{1})([0,9]{1})([0-9]{2})([0,1,2,3,5,6,7,8]{1})([0-9]{7}))")) {
            Toast.makeText(getContext(), "Enter Validate NIC", Toast.LENGTH_LONG).show();

        } else if (radioGroup1.getCheckedRadioButtonId()== -1) {
            Toast.makeText(getContext(), "Gender is required", Toast.LENGTH_SHORT).show();

        } else if (lane1.isEmpty()) {
            Toast.makeText(getContext(), "Lane1 is required", Toast.LENGTH_SHORT).show();

        } else if (lane2.isEmpty()) {
            Toast.makeText(getContext(), "Lane2 is required", Toast.LENGTH_SHORT).show();

        }else{

            //date
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String joinedDate = sdf.format(date);

            long mobile = Long.parseLong(mobileStr);
            long nic = Long.parseLong(nicStr);

            //object map
            HashMap<String, Object> riderMap = new HashMap<>();
            riderMap.put("Fname", fname);
            riderMap.put("Lname", lname);
            riderMap.put("Mobile", mobile);
            riderMap.put("NIC", nic);
            riderMap.put("Gender", gender);
            riderMap.put("Joined",joinedDate);
            riderMap.put("Status_Id", 0);
            riderMap.put("Lane1", lane1);
            riderMap.put("Lane2", lane2);
            riderMap.put("Pic", "person2");

            //save rider
            firebaseFirestore
                    .collection("Riders")
                    .add(riderMap)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(getContext(), "Rider Registered Successfully!", Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Fail to register", Toast.LENGTH_SHORT).show();

                        }
                    }
            );
        }
    }

    private void reset(){
        fnameTxt.setText("");
        lnameTxt.setText("");
        mobileTxt.setText("");
        nicTxt.setText("");
        lane1Txt.setText("");
        lane2Txt.setText("");
        radioGroup1.clearCheck();
    }
}

