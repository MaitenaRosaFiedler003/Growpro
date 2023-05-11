package com.example.growpro01.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.growpro01.R;
import com.example.growpro01.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        TextView nombre = findViewById(R.id.nombre);
        TextView nombreComp = findViewById(R.id.nombreCompleto);
        EditText psswrd = findViewById(R.id.clave);
        EditText email = findViewById(R.id.correo);
        TextView loginText = findViewById(R.id.login);
        Button botonRegister = findViewById(R.id.registrar);



        //TODO(8): inicializar auth a la instancia de FirebaseAuth
        auth = FirebaseAuth.getInstance();
        //TODO(9): colocar un listener a txtLogin de modo que lance LoginActivity si hace click
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegistroActivity.this, InicioSesionsActivity.class);
                startActivity(intent);
            }
        });

        botonRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String str_username = binding.txtLogin.getText().toString();
                String str_username = nombre.getText().toString();
                String str_fullname = nombreComp.getText().toString();
                String str_email = email.getText().toString();
                String str_password = psswrd.getText().toString();

                Log.d("12121", "pass : " + str_password);

                //TODO(10): Hacer los correspondientes if-else para que se puedan mostrar los siguientes toasts correctamente
                if(str_username.isEmpty() || str_fullname.isEmpty() || str_email.isEmpty() || str_password.isEmpty()) {

                    Toast.makeText(RegistroActivity.this, Html.fromHtml("<font color='#e3f2fd' ><b>" + getText(R.string.camposObligatoriosMessg) + "</b></font>"), Toast.LENGTH_LONG).show();


                }
                if(str_password.length() < 6){
                    Toast.makeText( RegistroActivity.this, getString(R.string.psswrdCorta),Toast.LENGTH_SHORT );
                } else {
                    pd = new ProgressDialog( RegistroActivity.this );
                    pd.setMessage( getText(R.string.registrando) );
                    pd.show();
                    register(str_username,str_fullname,str_email,str_password);
                }
            }
        } );
    }

    private void register(final String username, final String fullname, String email, String password){
        Log.d("12121212","llegue");
        Log.d("12121212","email" + email);
        Log.d("12121212","pass" + password);
        auth.createUserWithEmailAndPassword( email,password )
                .addOnCompleteListener(  new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("12121212","llegue");
                        if(task.isSuccessful()){
                            Log.d("1212121", "is success1");
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( userid );

                            HashMap<String,Object> hashMap = new HashMap<>( );
                            hashMap.put( "id",userid );
                            hashMap.put( "username",username.toLowerCase() );
                            hashMap.put( "fullname",fullname );
                            hashMap.put( "bio","" );
                            hashMap.put( "email",email );
                            Log.d("1212121", "is success");

                            reference.setValue( hashMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("1212121", "is success");
                                        pd.dismiss();
                                        Intent intent = new Intent(RegistroActivity.this,MainActivity.class);
                                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity( intent );
                                    }
                                }
                            } );
                        }else{
                            pd.dismiss();
                            Toast.makeText( RegistroActivity.this, getText(R.string.UsuarioNoRegistrado), Toast.LENGTH_SHORT).show();
                        }
                    }
                } );
    }

}
