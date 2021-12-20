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

import java.util.HashMap;
import java.util.Map;

public class AddFeedingActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference dataReference;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feeding);

        dataReference = FirebaseDatabase.getInstance().getReference();

        //Get pets id from intent
        Intent intent = getIntent();
        petId = AppData.getInstance().getPetId();

        // Tarkistetaan onko käyttäjä edelleen kirjautunut:
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
        }

        // Nappia painamalla lisätään uusi ruoka
        Button btn_sendfood = (Button) findViewById(R.id.btn_sendfood);
        btn_sendfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendfeeding();
            }
        });
    }


    public void sendfeeding() { // Adding new feeding

        EditText edittext1 = (EditText) findViewById(R.id.edit_date); // Päivä
        String date = edittext1.getText().toString();
        EditText edittext2 = (EditText) findViewById(R.id.edit_time); // Kellonaika
        String time = edittext2.getText().toString();
        EditText edittext3 = (EditText) findViewById(R.id.edit_food); // Ruoka
        String food = edittext3.getText().toString();

        // Kaikkien kenttien pitää olla täytettynä:
        if(edittext1.getText().length() == 0  || edittext2.getText().length() == 0 || edittext3.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields), Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            // refrensi mihin sijaintiin pusketaan.
            dataReference = dataReference.child("Pets").child(fireuser.getUid()).child("Pets").child(petId);
            dataReference = dataReference.child("Feeding");

            Map<String, Object> foodmap = new HashMap<>();
            foodmap.put("date", date);  // "dd.mm.yyyy"
            foodmap.put("time", time);
            foodmap.put("food", food);

            //push luodaan uniikki id ja saadaan sen sijainti ja sen jälkeen asetetaan arvo.
            DatabaseReference ref = dataReference.push();
            ref.setValue(foodmap);

            Toast toast = Toast.makeText(this, getString(R.string.new_feeding_added),Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
}
