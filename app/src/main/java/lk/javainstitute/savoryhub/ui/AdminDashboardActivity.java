package lk.javainstitute.savoryhub.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import lk.javainstitute.savoryhub.R;
import lk.javainstitute.savoryhub.navigation.AllOrdersFragment;
import lk.javainstitute.savoryhub.navigation.AnalyzingFragment;
import lk.javainstitute.savoryhub.navigation.DashboardFragment;
import lk.javainstitute.savoryhub.navigation.LogoutFragment;
import lk.javainstitute.savoryhub.navigation.ManageProductFragment;
import lk.javainstitute.savoryhub.navigation.ManageUserFragment;
import lk.javainstitute.savoryhub.navigation.ProfileFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadFragments(new DashboardFragment());

        ImageView toolbarImg = findViewById(R.id.toolbar_img);
        DrawerLayout drawerLayout1 = findViewById(R.id.drawerLayout1);
        NavigationView navigationView1 = findViewById(R.id.navigationView1);
        Toolbar toolbar1 = findViewById(R.id.toolBar1);

        toolbarImg.setOnClickListener(view -> {
            if (drawerLayout1.isDrawerOpen(navigationView1)) {
                drawerLayout1.closeDrawer(navigationView1);
            } else {
                drawerLayout1.openDrawer(navigationView1);
            }
        });

        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.nav_menu_dashboard){
                    loadFragments(new DashboardFragment());
                } else if (item.getItemId()==R.id.nav_menu_profile) {
                    loadFragments(new ProfileFragment());
                } else if (item.getItemId()==R.id.nav_menu_products) {
                    loadFragments(new ManageProductFragment());
                } else if (item.getItemId() == R.id.nav_menu_logout) {
                    loadFragments(new LogoutFragment());
                } else if (item.getItemId()==R.id.nav_menu_users) {
                    loadFragments(new ManageUserFragment());
                } else if (item.getItemId()==R.id.nav_menu_analyzing) {
                    loadFragments(new AnalyzingFragment());
                } else if (item.getItemId()==R.id.nav_menu_orders) {
                    loadFragments(new AllOrdersFragment());
                }

                toolbar1.setTitle(item.getTitle());
                drawerLayout1.closeDrawers();

                return true;
            }
        });

    }

    private void loadFragments(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                .replace(R.id.fragmentContainerView1, fragment, null)
                .setReorderingAllowed(true)
                .commit();

    }
}