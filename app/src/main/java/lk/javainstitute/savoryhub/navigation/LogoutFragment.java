package lk.javainstitute.savoryhub.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.ui.MainActivity;
import lk.javainstitute.savoryhub.ui.SignInActivity;

public class LogoutFragment extends Fragment {

    private Button logOut;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot =  inflater.inflate(R.layout.fragment_logout, container, false);


        Context context= getContext();
        if(context!=null){
            sharedPreferences = context.getSharedPreferences("lk.javainstitute.savoryhub.data", Context.MODE_PRIVATE);
        }


        logOut = viewRoot.findViewById(R.id.admin_logout_btn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences!=null){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin", false);
                    editor.apply();
                    Toast.makeText(context, "Logout Successfully",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        return viewRoot;
    }
}