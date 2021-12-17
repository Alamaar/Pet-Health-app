package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;


    private List<String> petIdKeyList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Delete back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        DatabaseReference baseRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets");


        recyclerView = findViewById(R.id.recyclerView);


        //TODO lemmikkien nimien hakeminen firebasesta ja tallentaminen s1:een

        MyAdapter myAdapter = new MyAdapter(this, petIdKeyList, R.drawable.ic_action_pets, baseRefrence);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        ValueEventListener petListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> newPetIdKeyList = new ArrayList<String>();

                petIdKeyList.clear();


                for(DataSnapshot child : snapshot.getChildren()){
                    String id = child.getKey().toString();
                    newPetIdKeyList.add(id);

                }

                petIdKeyList.addAll(newPetIdKeyList);

                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        baseRefrence.addValueEventListener(petListListener);


        // Avataan lemmikinlisäysikkuna:
        ImageButton add_pet_activity = (ImageButton) findViewById(R.id.btnAddPet);
        add_pet_activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPetActivity.class);
                startActivity(intent);
            }
        });
    }
}