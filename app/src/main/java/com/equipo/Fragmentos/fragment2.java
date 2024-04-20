package com.equipo.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.equipo.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.equipo.R;
import java.util.HashMap;

public class fragment2 extends Fragment {
    TextView fullname;
    TextInputEditText Nombre, Username, Email, Celular;
    TextInputLayout Nombre1, Username1;
    Button btn, Update, ticket;
    Boolean prueba1 = true;
    String phoneNo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        //Hooks
        fullname = view.findViewById(R.id.Profile_fullname1);
        Nombre = view.findViewById(R.id.Profile_Nombre_completo1);
        Email = view.findViewById(R.id.Profile_Email_edit1);
        Celular = view.findViewById(R.id.Profile_Numero_Edit1);
        Update = view.findViewById(R.id.Update);

        //Update info
        Nombre1 = view.findViewById(R.id.Profile_Nombre);


        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        HashMap<String, String> Details = sessionManager.getUsersDetailFromSession();
        phoneNo = Details.get(SessionManager.KEY_PHONENO);

        //fullname.setText(Details.get(SessionManager.KEY_FULLNAME));
        //Nombre.setText(Details.get(SessionManager.KEY_FULLNAME));
        //Email.setText(Details.get(SessionManager.KEY_EMAIL));
        //Celular.setText(Details.get(SessionManager.KEY_PHONENO));

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validar
                String val = Nombre1.getEditText().getText().toString().trim();
                if (val.isEmpty()) {
                    Nombre1.setError("Favor de ingresar un nombre");
                    prueba1 = false;
                } else {
                    Nombre1.setError(null);
                    Nombre1.setErrorEnabled(false);
                    prueba1 = true;
                }
                if (prueba1) {

                    // Get data from fields
                    String _newFullname = Nombre1.getEditText().getText().toString().trim();
                    String _newUsername = Username1.getEditText().getText().toString().trim();
                    // Update FireBase
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(phoneNo).child("fullName").setValue(_newFullname);
                    reference.child(phoneNo).child("username").setValue(_newUsername);

                    SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
                    //sessionManager.createLoginSession(_newFullname, _newUsername, Details.get(SessionManager.KEY_EMAIL), Details.get(SessionManager.KEY_PHONENO), Details.get(SessionManager.KEY_PASSWORD), Details.get(SessionManager.KEY_DATE), Details.get(SessionManager.KEY_GENDER), Details.get(SessionManager.KEY_PHONENOCOMPLETE), Details.get(SessionManager.KEY_TOKEN));
                    fullname.setText(_newFullname);
                    Nombre.setText(_newFullname);
                    Username.setText(_newUsername);
                    Toast.makeText(getActivity(), "Actualización completada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Favor de ingresar bien los parametros", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

}