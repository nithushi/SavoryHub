package lk.javainstitute.savoryhub.navigation;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

import lk.javainstitute.savoryhub.R;

public class AnalyzingFragment extends Fragment {
    private PieChart pieChart1, pieChart2;
    private FirebaseFirestore firebaseFirestore;

    private TextView positiveReviewsTextView, negativeReviewsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analyzing, container, false);


        pieChart1 = view.findViewById(R.id.pieChart1);
        pieChart2 = view.findViewById(R.id.pieChart2);

        positiveReviewsTextView = view.findViewById(R.id.positiveReviewsTextView);
        negativeReviewsTextView = view.findViewById(R.id.negativeReviewsTextView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        loadReviewCharts();
        loadCategoryCharts();


        return view;
    }

    private void loadReviewCharts() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(90f, "Positive"));
        entries.add(new PieEntry(10f, "Negative"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#5D8736")); // Positive reviews color
        colors.add(Color.parseColor("#EC7272")); // Negative reviews color

        PieDataSet dataSet = new PieDataSet(entries, "Review Data");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f); // Set text size to 12sp
        PieData data = new PieData(dataSet);
        pieChart2.setData(data);

        Description description = new Description();
        description.setText("Review Statistics");
        description.setTextSize(12f);
        pieChart2.setDescription(description);

        pieChart2.invalidate();

        positiveReviewsTextView.setText("Positive: 90%");
        negativeReviewsTextView.setText("Negative: 10%");

    }

    private void loadCategoryCharts() {
        firebaseFirestore.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            ArrayList<PieEntry> entries = new ArrayList<>();
                            float totalValue = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                float value = document.getDouble("Id").floatValue();
                                totalValue += value;
                            }

                            ArrayList<Integer> colors = new ArrayList<>();
                            Random random = new Random();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                float value = document.getDouble("Id").floatValue();
                                float percentage = (value / totalValue) * 100;
                                entries.add(new PieEntry(percentage, name));

                                // Generate random color
                                int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                                colors.add(color);
                            }

                            PieDataSet dataSet = new PieDataSet(entries, "Category Data");
                            dataSet.setColors(colors);
                            dataSet.setValueTextSize(12f);
                            PieData data = new PieData(dataSet);
                            pieChart1.setData(data);

                            Description description = new Description();
                            description.setText("Categories Statistics");
                            description.setTextSize(12f);
                            pieChart1.setDescription(description);

                            pieChart1.invalidate();
                        }
                    }
                }
        );
    }

}