package mx.edu.utng.mmacias.gallerymayra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GalleryActivity extends AppCompatActivity   {
    GridView simpleGrid;
    //GridView gvPins;
    //Button btnGallery;
   int pics[] = {R.drawable.josefa02, R.drawable.josefa03, R.drawable.josefa05, R.drawable.josefa02,
            R.drawable.josefa10, R.drawable.josefa11, R.drawable.josefa012, R.drawable.josefa13,R.drawable.josefa02, R.drawable.josefa03, R.drawable.josefa05, R.drawable.josefa02,
            R.drawable.josefa10, R.drawable.josefa11, R.drawable.josefa012, R.drawable.josefa13};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //gvPins = findViewById(R.id.gv_pins);
        //btnGallery =findViewById(R.id.btn_gallery);

        //btnGallery.setOnClickListener(this);

        //btnGallery = (Button) findViewById(R.id.btnGallery) ;
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), pics);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // set an Intent to Another Activity
                Intent intent = new Intent(GalleryActivity.this, SecondActivity.class);
                intent.putExtra("image", pics[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });
    }

}
