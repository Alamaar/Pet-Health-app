package mad.oamk.pettracker.customdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import mad.oamk.pettracker.R;
import mad.oamk.pettracker.customdata.adapters.CustomDataCreateAdapter;

public class CustomDataCreateActivity extends AppCompatActivity {

    private RecyclerView customRecyclerView;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_data_create);

        petId = getIntent().getStringExtra("PetId");

        customRecyclerView = findViewById(R.id.customDataRecyclerView);

        EditText textInputHeader = (EditText) findViewById(R.id.textInputHeader);

        List<String> contents = new ArrayList<>();
        contents.add(0,"");
        CustomDataCreateAdapter adapter = new CustomDataCreateAdapter(contents);


        customRecyclerView.setAdapter(adapter);
        customRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button buttonAddField = (Button) findViewById(R.id.buttonAddField);
        buttonAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addField();
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        });



        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataHeader = textInputHeader.getText().toString();


                List<String> saveContents = adapter.getContents();
                Bundle extra = new Bundle();
                extra.putStringArrayList("fields", (ArrayList<String>) saveContents);
                Intent intent = new Intent(CustomDataCreateActivity.this, CustomDataAddActivity.class);
                intent.putExtra("fields", extra);
                intent.putExtra("PetId",petId);
                intent.putExtra("dataHeader",dataHeader);
                startActivity(intent);
            }
        });










    }
}