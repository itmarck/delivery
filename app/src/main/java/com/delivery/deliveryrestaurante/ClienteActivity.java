package com.delivery.deliveryrestaurante;

import android.content.Intent;
import android.os.Bundle;

import com.delivery.deliveryrestaurante.clases.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity {
    private ListView lista;
    private FirebaseFirestore db ;
    private List productos = new ArrayList();
    private ArrayAdapter adaptador;
    private Spinner categorias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lista = findViewById(R.id.lista_general);
        categorias = findViewById(R.id.spinner);
        adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,productos) ;
        lista.setAdapter(adaptador);
        db = FirebaseFirestore.getInstance();

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarDatos(categorias.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.carrito);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClienteActivity.this, CarritoActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cliente) {
            startActivity(new Intent(this, RestauranteActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }
    public void cargarDatos(String categoria){

        productos.clear();
        db.collection("productos").whereEqualTo("categoria", categoria)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot doc : list) {

                                Producto pro = doc.toObject(Producto.class);
                                productos.add(pro.getNombre());

                            }
                            adaptador.notifyDataSetChanged();

                        }


                    }
                });

    }
}
