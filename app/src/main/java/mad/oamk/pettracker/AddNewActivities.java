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

public class AddNewActivities extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference mDatabase;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activities);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        // Nappia painamalla lisätään aktiviteetti ja päivämäärä
        Button add_activity= (Button) findViewById(R.id.btnAddActivity);
        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() { // Adding new activity

        EditText edittext1 = (EditText) findViewById(R.id.txtDate); // date
        String date = edittext1.getText().toString();
        EditText edittext2 = (EditText) findViewById(R.id.txtType); // type
        String type = edittext2.getText().toString();
        EditText edittext3 = (EditText) findViewById(R.id.txtLength); // length
        String length = edittext3.getText().toString();

        // Kaikkien kenttien pitää olla täytettynä:
        if(edittext1.getText().length() == 0  || edittext2.getText().length() == 0 || edittext3.getText().length() == 0) {
            Toast toast = Toast.makeText(this, "Fill in the required fields.", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            // refrensi mihin sijaintiin pusketaan.
            DatabaseReference petsref = mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").child(petId);
            petsref = petsref.child("Activities");

            Map<String, Object> activitymap = new HashMap<>();
            activitymap.put("date", date);  // "dd.mm.yyyy"
            activitymap.put("type", type);
            activitymap.put("length", length); // Minutes

            //push luodaan uniikki id ja saadaan sen sijainti ja sen jälkeen asetetaan arvo.
            DatabaseReference ref = petsref.push();
            ref.setValue(activitymap);

            Toast toast = Toast.makeText(this, "New activity added.",Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
}
