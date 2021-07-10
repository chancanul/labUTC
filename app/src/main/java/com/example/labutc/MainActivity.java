package com.example.labutc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labutc.configuracion.config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.labutc.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    TextView txtV1NavHeader, txtV2NavHeader;
    ImageView imgViewNavHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Traer los valores del bundle
        Intent intent = this.getIntent();
        Bundle valores = intent.getExtras();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        //La siguiente referencia al navController es utililizada dentro el control androidx.fragment.app.FragmentContainerView
        //ubicado en el content_main normalmente se establece con fragment sin embargo para un mejor rendimiento se utiliza  el
        //mencionado anteriormente.
        //---------------------------------------------------
        //Está línea de código funciona con el fragment normal
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //-----------------------------------------------------
        //Con la nueva implementación.
        //--------------------------
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        //----------------------------
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Definiendo la parte gráfica y lógica.
        txtV1NavHeader = navigationView.getHeaderView(0).findViewById(R.id.txtV1CNavHeader);
        txtV2NavHeader = navigationView.getHeaderView(0).findViewById(R.id.txtV2CNavHeader);
        imgViewNavHeader = navigationView.getHeaderView(0).findViewById(R.id.imageViewNavHeader);

        //Asignar los valores.
        txtV1NavHeader.setText(valores.getString("nombre"));
        txtV2NavHeader.setText(valores.getString("apellido_p"));
        Picasso.with(getApplicationContext()).load(config.getUrlImages() + valores.getString("imagen")).fit().into(imgViewNavHeader);

    } //fin on create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}