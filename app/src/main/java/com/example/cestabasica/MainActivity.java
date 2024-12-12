package com.example.cestabasica;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    Button btlogar;
    TextView linkregistro;
    EditText edtlogin,edtsenha;
    String login="";
    String senha="";
    String telefone="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtlogin=(EditText)findViewById(R.id.edtusr);
        edtsenha=(EditText)findViewById(R.id.edtSenha);

        btlogar=(Button) findViewById(R.id.btlogar);
        btlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviajsonpostLogineSenha().execute();
            }
        });



        linkregistro =(TextView) findViewById(R.id.edtregtelefone);
        linkregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CadNovoUsuario.class);
                startActivity(i);
            }

        });
    }
    class EnviajsonpostLogineSenha extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                String url = "http://200.132.17.157/ifome/consulta_login.php";
                JSONObject jsonValores = new JSONObject();
                jsonValores.put("login", edtlogin.getText().toString());
                jsonValores.put("senha", edtsenha.getText().toString());
                conexaouniversal mandar = new conexaouniversal();
                String mensagem=mandar.postJSONObject(url,jsonValores);

                try{
                    JSONObject jsonobjc = new JSONObject(mensagem);
                    JSONArray jsonvet = jsonobjc.getJSONArray("usuario");
                    for(int i=0;i<jsonvet.length();i++){
                        JSONObject jsonitem=jsonvet.getJSONObject(i);
                        login=jsonitem.optString("nome").toString();
                        senha=jsonitem.optString("senha").toString();
                    }
                    if ((edtlogin.getText().toString().equals(login))&&(edtsenha.getText().toString().equals(senha)))
                    {
                        Intent i = new Intent(getApplicationContext(), TelaPrincipal.class);
                        startActivity(i);
                    }

                }catch (Exception ex){
                    Toast.makeText(MainActivity.this,"Problemas ao tentar conectar",Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }




}