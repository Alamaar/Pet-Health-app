package mad.oamk.pettracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[];
    int image = R.drawable.ic_action_pets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initialize Firebase and check if the user is signed in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (false) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.pet_names);
        //TODO lemmikkien nimien hakeminen firebasesta ja tallentaminen s1:een

        MyAdapter myAdapter = new MyAdapter(this, s1, image);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}