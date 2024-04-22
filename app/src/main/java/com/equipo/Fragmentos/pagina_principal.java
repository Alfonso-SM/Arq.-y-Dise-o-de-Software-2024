package com.equipo.Fragmentos;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.equipo.FirstScreen;
import com.equipo.R;
import com.equipo.SessionManager;
import com.equipo.dto.ClinicHistory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import android.os.Environment;
import android.widget.Toast;


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
            UploadToFirebase();
        });

        loadUserInfo();
        return view;
    }

    private void loadUserInfo() {

        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();
        String phoneNo = userDetails.getOrDefault(SessionManager.KEY_PHONENO, "");

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference user = rootNode.getReference("Users").child(phoneNo);
        user.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ClinicHistory clinicHistory = dataSnapshot.child("clinic_history").getValue(ClinicHistory.class);
                setTextInputLayoutValue(fullname,clinicHistory.getFullname());
                setTextInputLayoutValue(age, String.valueOf(clinicHistory.getAge()));
                setTextInputLayoutValue(civilStatus,clinicHistory.getCivilStatus());
                setTextInputLayoutValue(nationality,clinicHistory.getNationality());
                setTextInputLayoutValue(dob,clinicHistory.getDob());
                setTextInputLayoutValue(diseases,clinicHistory.getDiseases());
                setTextInputLayoutValue(notes,clinicHistory.getNotes());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void setTextInputLayoutValue(TextInputLayout component, String value) {

        component.getEditText().setText(value);
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
    public void UploadToFirebase(){
        //Por el momento esto solo lo va a compartir con el usuario 1 y contraseña 1
        firebaseStorage = FirebaseStorage.getInstance();
        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();
        String phoneNo = userDetails.getOrDefault(SessionManager.KEY_PHONENO, "");
        StorageReference myUserImageStorageRef = firebaseStorage.getReference("Users").child(phoneNo)
                .child("Historial_Clinico.pdf");

        File pdf = generatePDF();
        UploadTask uploadTask = myUserImageStorageRef.putFile(Uri.fromFile(pdf));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getActivity(), "PDF Compartido exitoso", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdf);
        shareIntent.setType("application/pdf");
        startActivity(Intent.createChooser(shareIntent, null));
    }

    private File generatePDF() {

       // String extstoragedir = Environment.getExternalStorageDirectory().toString();
        File fol = getContext().getCacheDir();
        File folder=new File(fol,"pdf");
        if(!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
            final File file = new File(folder, "sample.pdf");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText("Nombre: " + getTextInputLayoutValue(fullname), 10, 10, paint);
            canvas.drawText("Edad: " + getTextInputLayoutValue(age), 10, 20, paint);
            canvas.drawText("Estado Civil: " + getTextInputLayoutValue(civilStatus), 10, 30, paint);
            canvas.drawText("Nacionalidad: " + getTextInputLayoutValue(nationality), 10, 40, paint);
            canvas.drawText("Fecha de Nacimiento: " + getTextInputLayoutValue(dob), 10, 50, paint);
            canvas.drawText("Enfermedades :" + getTextInputLayoutValue(diseases), 10, 60, paint);
            canvas.drawText("Notas: " + getTextInputLayoutValue(notes), 10, 70, paint);

            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            return file;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}