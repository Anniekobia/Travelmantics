package com.example.travelmantics;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("traveldeals");
        txtPrice=findViewById(R.id.txtPrice);
        txtDescription=findViewById(R.id.txtDescription);
        txtTitle=findViewById(R.id.txtTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
            saveDeal();
            Toast.makeText(this,"Deal saved", Toast.LENGTH_LONG);
            clean();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clean() {
        txtDescription.setText("");
        txtTitle.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }

    private void saveDeal() {
        String title=txtTitle.getText().toString();
        String price=txtPrice.getText().toString();
        String description=txtDescription.getText().toString();
        TravelDeal deal = new TravelDeal(title,description,price,"");
        mDatabaseReference.push().setValue(deal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }
}
