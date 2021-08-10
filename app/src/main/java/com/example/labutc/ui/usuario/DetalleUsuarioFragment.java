package com.example.labutc.ui.usuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labutc.MainActivity;
import com.example.labutc.R;
import com.example.labutc.configuracion.config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class DetalleUsuarioFragment extends Fragment {
    private String accion = "";
    private DetalleUsuarioViewModel mViewModel;
    private TextView txtVIdUsuario;
    private EditText eTxtNombre, eTxtAp, eTxtAm, eTxtUsuario, eTxtPassword;
    private ImageView imgBtnCamara, imgBtnGaleria;
    private ImageView imgUsuario;

    public static DetalleUsuarioFragment newInstance() {
        return new DetalleUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detalle_usuario_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainActivity navigation = (MainActivity) getActivity();
        txtVIdUsuario = view.findViewById(R.id.duCEtxtid);
        eTxtNombre = view.findViewById(R.id.duCEtxtNombre);
        eTxtAp = view.findViewById(R.id.duCEtxtApellidop);
        eTxtAm = view.findViewById(R.id.duCEtxtApelldiom);
        eTxtUsuario = view.findViewById(R.id.duCEtxtUsuario);
        eTxtPassword = view.findViewById(R.id.duCEtxtPassword);

        imgBtnCamara = view.findViewById(R.id.duCImgBtnCamara);
        imgBtnGaleria = view.findViewById(R.id.duCImgBtnGaleria);
        imgUsuario = view.findViewById(R.id.duCimgVUsuario);

        if (getArguments() != null) {
            accion = getArguments().getString("accion");
            switch (accion) {
                case "N":
                    txtVIdUsuario.setText("");
                    eTxtNombre.setText("");
                    eTxtAp.setText("");
                    eTxtAm.setText("");
                    eTxtUsuario.setText("");
                    eTxtPassword.setText("");
                    break;
                case "M":
                    txtVIdUsuario.setText(getArguments().getString("id_usuario"));
                    eTxtNombre.setText(getArguments().getString("nombre"));
                    eTxtAp.setText(getArguments().getString("apellido_p"));
                    eTxtAm.setText(getArguments().getString("apellido_m"));
                    eTxtUsuario.setText(getArguments().getString("usuario"));
                    eTxtPassword.setText(getArguments().getString("password"));
                    Picasso.with(getContext()).load(config.getUrlImages() + "/" +
                            getArguments().getString("imagen")).fit().into(imgUsuario);
                    break;
            }
        }

        //Antes de manipular al mainActivituy debemos comprobar que no sea nulo.
        if (navigation != null) {
            FloatingActionButton fabactionUser = navigation.findViewById(R.id.fab);
            fabactionUser.setImageResource(R.drawable.guardar);
        }

    }
}












