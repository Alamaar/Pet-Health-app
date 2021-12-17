package mad.oamk.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import mad.oamk.pettracker.models.Pet;

public class UpdatePetActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;

    private DatabaseReference petIdRefrence;

    private FirebaseUser user;

    private String petId;

    private Pet pet;

    private ValueEventListener petListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);

        // Get pet's id from intent
        Intent intent = getIntent();
        petId = intent.getStringExtra("PetId");

        // Get current user from firebase:
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        petIdRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get pet to pet class
                pet = snapshot.getValue(Pet.class);
                //setting default values from pet class
                setDefaultValues();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // Napista päivitetään uudet tiedot:
        Button update_pet = (Button) findViewById(R.id.btnUpdate);
        update_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        petIdRefrence.addValueEventListener(petListener);

        editText1 = (EditText) findViewById(R.id.txtUpdateInput1);
        editText2 = (EditText) findViewById(R.id.txtUpdateInput2);
        editText3 = (EditText) findViewById(R.id.txtUpdateInput3);
        editText4 = (EditText) findViewById(R.id.txtUpdateInput4);
    }

    private void setDefaultValues() {
        //Setting default values
        editText1.setText(pet.getName());
        editText2.setText(pet.getSpecies());
        editText3.setText(pet.getBreed());
        editText4.setText(pet.getDateOfBirth());
    }

    public void update() { // Tietojen päivitys-metodi

        String name = editText1.getText().toString();
        String species = editText2.getText().toString();
        String breed = editText3.getText().toString();
        String date_of_birth = editText4.getText().toString();

        // Kaikkien kenttien pitää olla täytettynä joko uusilla tai vanhoilla tiedoilla:
        if(editText1.getText().length() == 0  || editText2.getText().length() == 0 || editText3.getText().length() == 0 || editText4.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields),Toast.LENGTH_LONG);
            toast.show();
        }
        // Uudet tiedot päivitetään firebaseen:
        else {
            DatabaseReference petsref = petIdRefrence;

            // Uudet tiedot hashmappiin:
            HashMap map = new HashMap();
            map.put("name", name);
            map.put("species", species);
            map.put("breed", breed);
            map.put("dateOfBirth", date_of_birth);

            petsref.updateChildren(map);

            Toast toast = Toast.makeText(this, getString(R.string.pet_information_updated),Toast.LENGTH_LONG);
            toast.show();

            UpdatePetActivity.this.finish();
        }
    }
}
