package com.equipo.Fragmentos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.equipo.FirstScreen;
import com.equipo.R;
import com.equipo.SessionManager;
import com.equipo.dto.ClinicHistory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class pagina_principal extends Fragment {
    TextInputLayout fullname, age, civilStatus, nationality,dob,diseases,notes;
    Button Save,Share;
    private FirebaseStorage firebaseStorage;
    //
    SwipeRefreshLayout refreshLayout;
    //////////////   RV Prueba Promociones


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_pagina_principal, container, false);
        fullname = view.findViewById(R.id.fullname);
        age = view.findViewById(R.id.age);
        civilStatus = view.findViewById(R.id.civil_status);
        nationality = view.findViewById(R.id.nationality);
        dob = view.findViewById(R.id.dob);
        diseases = view.findViewById(R.id.diseases);
        notes = view.findViewById(R.id.notes);
        Save = view.findViewById(R.id.Save);
        Share = view.findViewById(R.id.Share);
        refreshLayout = view.findViewById(R.id.Refresh);

        Save.setOnClickListener(v -> {
            saveMedicalRecord(view);
        });

        Share.setOnClickListener(v -> {
            //Aqui deberias de crear el pdf y luego mandar el File a la funcion de
            //UploadToFirebase()
        });

        return view;
    }

    public void saveMedicalRecord(View view) {
        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();
        String phoneNo = userDetails.getOrDefault(SessionManager.KEY_PHONENO, "");

        if(!phoneNo.isEmpty()) {
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("Users");
            ClinicHistory clinicHistory = new ClinicHistory();
            clinicHistory.setFullname(getTextInputLayoutValue(fullname));
            clinicHistory.setAge(Integer.parseInt(getTextInputLayoutValue(age)));
            clinicHistory.setCivilStatus(getTextInputLayoutValue(civilStatus));
            clinicHistory.setNationality(getTextInputLayoutValue(nationality));
            clinicHistory.setDob(getTextInputLayoutValue(dob));
            clinicHistory.setDob(getTextInputLayoutValue(diseases));
            clinicHistory.setNotes(getTextInputLayoutValue(notes));
            reference.child(phoneNo)
                    .child("clinic_history")
                    //.push()
                    .setValue(clinicHistory);
        }
    }

    private String getTextInputLayoutValue(TextInputLayout component) {

        return component.getEditText().getText().toString().trim();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Hooks
        refreshLayout.setOnRefreshListener(() -> {
            Intent intent = new Intent(getActivity(), FirstScreen.class);
            refreshLayout.setRefreshing(false);
            startActivity(intent);
        });

    }
    public void UploadToFirebase(File file){
        //Por el momento esto solo lo va a compartir con el usuario 1 y contraseña 1
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference myUserImageStorageRef = firebaseStorage.getReference("Users").child("1")
                .child("Historial_Clinico.pdf");
        UploadTask uploadTask = myUserImageStorageRef.putFile(Uri.fromFile(file));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getActivity(), "PDF Compartido exitoso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}