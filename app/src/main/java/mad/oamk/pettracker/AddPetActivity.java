package mad.oamk.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mad.oamk.pettracker.models.Pet;

public class AddPetActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Tarkistetaan onko käyttäjä edelleen kirjautunut:
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        // Nappia painamalla lisätään uusi lemmikki:
        Button add_pet = (Button) findViewById(R.id.btnAdd);
        add_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    public void add() { // Adding new pet

        EditText editText1 = (EditText) findViewById(R.id.txtInput1); // name
        String name = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.txtInput2); // species
        String species = editText2.getText().toString();
        EditText editText3 = (EditText) findViewById(R.id.txtInput3); // breed
        String breed = editText3.getText().toString();
        EditText editText4 = (EditText) findViewById(R.id.txtInput4); // date of birth
        String date_of_birth = editText4.getText().toString();

        // Kaikkien kenttien pitää olla täytettynä:
        if(editText1.getText().length() == 0  || editText2.getText().length() == 0 || editText3.getText().length() == 0 || editText4.getText().length() == 0) {
            Toast toast = Toast.makeText(this, "Fill in the required fields.",Toast.LENGTH_LONG);
            toast.show();
        }
        // Kun kaikki tiedot on täytetty lisätään lemmikki firebaseen:
        else {
            String userID = fireuser.getUid();
            // Reference eli sijainti
            // .push() luo uuden uniikin avaimen ja palautaa sijainnin sen jälkeen set valuella lyödään lemmikki luokka sinne.
            DatabaseReference petsref = mDatabase.child("Pets").child(userID).child("Pets").push();
            // Pet constructor
            Pet pet = new Pet(name, date_of_birth, species, breed);
            pet.setPhotoUrl("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/dog-puppy-on-garden-royalty-free-image-1586966191.jpg");
            petsref.setValue(pet);

            Toast toast = Toast.makeText(this, "New pet added.",Toast.LENGTH_LONG);
            toast.show();

            AddPetActivity.this.finish();
        }
    }
}