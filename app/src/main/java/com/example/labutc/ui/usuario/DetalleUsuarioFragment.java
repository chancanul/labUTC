package com.example.labutc.ui.usuario;

import static android.app.Activity.RESULT_OK;

import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetalleUsuarioFragment extends Fragment {
    private String accion = "";
    private DetalleUsuarioViewModel mViewModel;
    private TextView txtVIdUsuario;
    private EditText eTxtNombre, eTxtAp, eTxtAm, eTxtUsuario, eTxtPassword;
    private ImageView imgBtnCamara, imgBtnGaleria;
    private ImageView imgUsuario;
    private int imagenCamara = 10;
    private int imagenGaleria = 11;
    private Bitmap thumbnail;
    private Uri imageUri;

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
       imgBtnCamara.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ContentValues valores = new ContentValues();
               valores.put(MediaStore.Images.Media.TITLE, "My Imagen");
               valores.put(MediaStore.Images.Media.DESCRIPTION, "Photo Taken On " + System.currentTimeMillis());
               imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores);
               Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
               startActivityForResult(intentCamara, imagenCamara);
           }
       });
        imgBtnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galeria, imagenGaleria);
            }
        });

    } //fin onViewCreated

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == imagenCamara) {
                int giro = 0;
                try {
                    ExifInterface exif = new ExifInterface(traerRuta(imageUri));
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            giro = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            giro = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            giro = 90;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                thumbnail = girarImagen(imageUri, giro);
                imgUsuario.setImageBitmap(thumbnail);
            } else if (requestCode == imagenGaleria) {
                imageUri = data.getData();
                imgUsuario.setImageURI(imageUri);
            }

        }
    }
    private Bitmap girarImagen(Uri recurso, int giro) {
        String ruta = "";
        Bitmap imagenResultante = null;
        File archivo;
        ruta = traerRuta(recurso);
        archivo = new File(ruta);
        try {
            imagenResultante = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), recurso);
            FileOutputStream salida = new FileOutputStream(archivo);
            imagenResultante.compress(Bitmap.CompressFormat.JPEG, 50, salida);
            salida.flush();
            salida.close();
            Matrix matrix = new Matrix();
            matrix.postRotate(giro);
            imagenResultante = Bitmap.createBitmap(imagenResultante, 0, 0, imagenResultante.getWidth(),
                               imagenResultante.getHeight(),matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagenResultante;
    }
    private String traerRuta(Uri rutaImagen) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(rutaImagen, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String resultado = cursor.getString(columnIndex);
        cursor.close();
        return resultado;
    }
}











