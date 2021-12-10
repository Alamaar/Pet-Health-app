package mad.oamk.pettracker.customdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mad.oamk.pettracker.LoginActivity;
import mad.oamk.pettracker.R;
import mad.oamk.pettracker.customdata.adapters.CustomDataAddAdapter;

public class CustomDataAddActivity extends AppCompatActivity {

    private RecyclerView addDataRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_data_add);


        Bundle extra = getIntent().getBundleExtra("fields");
        String dataHeader = getIntent().getStringExtra("dataHeader");
        String petId = getIntent().getStringExtra("PetId");
        ArrayList<String> contents = extra.getStringArrayList("fields");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        //Set refrence to pets id
        DatabaseReference petIdReference = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        addDataRecyclerview = findViewById(R.id.custom_data_add_recyclerView);

        CustomDataAddAdapter  adapter = new CustomDataAddAdapter(contents);

        TextView header = (TextView) findViewById(R.id.textViewHeader);

        header.setText(dataHeader);


        addDataRecyclerview.setAdapter(adapter);
        addDataRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        Button save = (Button) findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> data =adapter.getEditTextFields();
                List<String> fields = adapter.getFields();


                Map<String, Object> datamap = new HashMap<>();

               for (int i = 0 ; i < data.size(); i++){
                   datamap.put(fields.get(i).toString(), data.get(i).toString());
               }

               DatabaseReference customDataReference = petIdReference.child("CustomData").child(dataHeader).push();
               customDataReference.setValue(datamap);


                Intent intent = new Intent(CustomDataAddActivity.this, CustomDataViewActivity.class);
                intent.putExtra("PetId",petId);
                intent.putExtra("dataHeader",dataHeader);
                startActivity(intent);












            }
        });




    }

    public void push(HashMap<String, Object> datamap, DatabaseReference dataBaseReference){

        }
















    }
