package com.equipo.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.equipo.R;


public class SetNewPassword extends AppCompatActivity {

    TextInputLayout password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        password = findViewById(R.id.Set_new_Password1);
        confirmPassword = findViewById(R.id.Set_new_Password2);


    }

    public void setNewPasswordBtn(View view) {

        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }

        // Get data from fields
        String _newPassword = password.getEditText().getText().toString().trim();
        String _phoneNo = getIntent().getStringExtra("phoneNo");
        // Update FireBase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_phoneNo).child("password").setValue(_newPassword);
        startActivity(new Intent(getApplicationContext(), ForgerPasswordSuccess.class));
        finish();

    }

    private boolean validateConfirmPassword() {
        String val = password.getEditText().getText().toString().trim();
        String val1 = confirmPassword.getEditText().getText().toString().trim();
        if (!val.equals(val1)) {
            confirmPassword.setError("Las contraseñas no son iguales");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        //"(?=.*[0-9])" +  // Un numero
        //"(?=.*[a-z])" + // Una minuscula
        //"(?=.*[A-Z])" +  // Una mayuscula
        //Cualquier letra
        // Caracter especial
        // Espacios en blanco
        // String CheckPassword = String.format("^(?=.*[a-zA-Z)(?=.*[@#$%%^&+=)(\\A\\w{1,20}\\z).{4,}$");
        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }/*else if (!val.matches(CheckPassword)){
            password.setError("Password too weak");
            return false;
        }*/ else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

}