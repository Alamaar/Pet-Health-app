package mad.oamk.pettracker.customdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mad.oamk.pettracker.AppData;
import mad.oamk.pettracker.LoginActivity;
import mad.oamk.pettracker.R;
import mad.oamk.pettracker.customdata.adapters.CustomDataViewAdapter;

public class CustomDataViewActivity extends AppCompatActivity {


    private String header;
    private List<String> fields = new ArrayList<>();
    // fields haetaan adapterista

    private List<String> customDataIDs = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference customDataRefrence;
    private String petId;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_data_view);

        //intent get extra pet id ja custom datasetin otsikko

        // on click add -> pitää rakentaa arraylist jossa on kaikki fields ja header
        //
        /*intent.putExtra("PetId",petId);
        intent.putExtra("dataHeader",dataHeader);*/

        //TODO addbuttoni


        AppData appData = AppData.getInstance();
        header = appData.getDataHeader();
        petId = appData.getPetId();


        /*Intent intent = getIntent();
        petId = intent.getStringExtra("PetId");
        header = intent.getStringExtra("dataHeader"); //get header from cu
        //check contents???*/



        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginintent = new Intent(this, LoginActivity.class);
            startActivity(loginintent);
        }

        customDataRefrence = FirebaseDatabase.getInstance().getReference();
        customDataRefrence = customDataRefrence.child("Pets").child(user.getUid()).child("Pets").child(petId);
        customDataRefrence = customDataRefrence.child("CustomData").child(header);


        //recyclerview adapter
        CustomDataViewAdapter adapter = new CustomDataViewAdapter(this, customDataIDs, customDataRefrence);

        recyclerView = findViewById(R.id.custom_data_view_recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        ValueEventListener customDataSetListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customDataIDs.clear();

                for(DataSnapshot child : snapshot.getChildren()){
                    String id = child.getKey();
                    customDataIDs.add(id);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        customDataRefrence.addValueEventListener(customDataSetListener);


















    }
}