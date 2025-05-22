package lk.javainstitute.savoryhub.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.adapters.OrderHistoryAdapter;
import lk.javainstitute.savoryhub.model.Orders;

public class AllOrdersFragment extends Fragment {

    private RecyclerView orderRecycleView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<Orders> ordersArrayList;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);

        orderRecycleView = view.findViewById(R.id.all_orders_recycleview);
        orderRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersArrayList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(ordersArrayList);
        orderRecycleView.setAdapter(orderHistoryAdapter);

        progressBar = view.findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        loadOrderHistory();

        return view;
    }

    private void loadOrderHistory() {

        progressBar.setVisibility(View.GONE);
        firebaseFirestore.collection("Orders")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                Orders orders = document.toObject(Orders.class);
                                if (orders != null) {
                                    ordersArrayList.add(orders);
                                    Log.d("DashboardFragment", "Order added: " + orders.getOrderId());
                                } else {
                                    Log.w("DashboardFragment", "Null Order document");
                                }
                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("DashboardFragment", "Error getting documents: ", task.getException());
                        }
                    }
                }
        );

    }
}