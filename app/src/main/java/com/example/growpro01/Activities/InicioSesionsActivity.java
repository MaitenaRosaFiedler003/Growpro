package com.example.growpro01.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.growpro01.R;
import com.example.growpro01.databinding.ActivityInicioSesionsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InicioSesionsActivity extends AppCompatActivity {
    private ActivityInicioSesionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesions);
        FirebaseAuth auth ;

        binding = ActivityInicioSesionsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();

        binding.registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (InicioSesionsActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        binding.acceder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.email.getText().toString();
                String password = binding.psswrd.getText().toString();

                ProgressDialog pd = showProgressDialog(InicioSesionsActivity.this);
                //si el email o la contraseñ a estan vacios manda mensaje de error
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(InicioSesionsActivity.this, getText(R.string.camposObligatoriosMessg), Toast.LENGTH_SHORT).show();
                    Log.d("121212", "en toast");
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(InicioSesionsActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(auth.getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //TODO(6a): Dejar de mostrar el progress dialog y crear un objeto intent de la clase Intent para lanzar MainActivity

                                                pd.dismiss();

                                                Intent intent = new Intent(InicioSesionsActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                //TODO(6b): Dejar de mostrar el progress dialog
                                                pd.dismiss();
                                            }
                                        });
                                    } else {
                                        //TODO(7): Dejar de mostrar el progress dialog y mostrar un toast con el texto authentication failed
                                        pd.dismiss();
                                        Toast toast1 =
                                                Toast.makeText(getApplicationContext(),
                                                        "Error de autenticación", Toast.LENGTH_SHORT);
                                    }

                                }
                            });
                }
            }
        });
    }

    ProgressDialog showProgressDialog(Context context) {
        final ProgressDialog pd = new ProgressDialog(InicioSesionsActivity.this);
        pd.setMessage(context.getString(R.string.inciando));
        pd.show();
        return pd;
    }


}