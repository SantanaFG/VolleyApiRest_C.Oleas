package com.apecssi.wsvolley;

import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apecssi.wsvolley.model.Cliente;
import com.apecssi.wsvolley.model.Publicacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = findViewById(R.id.tableLayout);
        //Recibir datos desde api.
        getDatos();
    }

    private void getDatos(){
        String url="http://192.168.137.173:8080/api/clientes";//endpoint.
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Recibir el Json y pasar a Publicacion.
                pasarJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //MANEJAMOS EL ERROR.
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonArrayRequest);//hacemos la peticion al API
    }

    private void pasarJson( JSONArray array){

        for(int i=0;i<array.length();i++){
            JSONObject json=null;
            Publicacion publicacion=new Publicacion();
            Cliente cliente = new Cliente();

            try {
                json=array.getJSONObject(i);
                cliente.setId(json.getLong("id"));//como viene del API
                cliente.setNombre(json.getString("nombre"));
                cliente.setApellido(json.getString("apellido"));
                cliente.setEmail(json.getString("email"));
                agregarFila(cliente);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void agregarFila(Cliente cliente) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutParams);

        agregarCelda(cliente.getNombre(), row);
        agregarCelda(cliente.getApellido(), row);
        agregarCelda(cliente.getEmail(), row);

        tableLayout.addView(row);
    }

    private void agregarCelda(String texto, TableRow row) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;

        textView.setLayoutParams(layoutParams);
        row.addView(textView);
    }


}