package com.example.labutc.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labutc.MainActivity;
import com.example.labutc.R;
import com.example.labutc.adaptadores.adapterusuario;
import com.example.labutc.configuracion.config;
import com.example.labutc.databinding.ActivityMainBinding;
import com.example.labutc.databinding.FragmentHomeBinding;
import com.example.labutc.modelos.usuarios;
import com.example.labutc.retrofit.interfaceRetrofit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView myRecycler;
    private View myView;
    private adapterusuario myAdapter;


    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Obtener el MainActivity para poder manipular el floatingActionButton
        final MainActivity navigation = (MainActivity) getActivity();
        myView = view;
        //Antes de manipular al mainActivituy debemos comprobar que no sea nulo.
        if (navigation != null) {
         FloatingActionButton fab = navigation.findViewById(R.id.fab);
          fab.setImageResource(R.drawable.guardar_usuario);
       }
        getUsuarios();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void getUsuarios() {
        //Variable para iniciar la petición Retrofit
        interfaceRetrofit peticion = config.getRetrofit().create(interfaceRetrofit.class);
        //Preparar la petición call (llamar)
        Call<List<usuarios>> call = peticion.getusuarios("actUser");
        //Iniciar la petición con enqueue. el método incluye dos apartados para saber si la petición se llevó con éxito o fracaso
        //onResponse-onFailure
        call.enqueue(new Callback<List<usuarios>>() {
            @Override
            public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
                //En caso de éxito
                //la variable response es la encargada de almacenar la respuesta del servidor.
                mostrarRecycler(response.body());
            }
            @Override
            public void onFailure(Call<List<usuarios>> call, Throwable t) {
                //en caso de fracaso

               Snackbar msjPersonalizado = Snackbar.make(myView, "Servidor inaccesible", Snackbar.LENGTH_SHORT);
                msjPersonalizado.show();
            }
        });
    }  //fin get usuarios
    public void mostrarRecycler(List<usuarios> listado) {
        myRecycler = (RecyclerView) myView.findViewById(R.id.recvCUsuarios); //Definiendo el recycler
        myAdapter = new adapterusuario(getContext(), listado); //Construyendo el adaptador
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext())); //Agregando un contraintLayout al recicler
        myRecycler.setAdapter(myAdapter);//Volcar los datos al recycler
        //Generar la función clic del adaptador, implementado en el adaptador

    }

}