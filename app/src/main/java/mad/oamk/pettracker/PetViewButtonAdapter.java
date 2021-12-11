package mad.oamk.pettracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class PetViewButtonAdapter  extends RecyclerView.Adapter<PetViewButtonAdapter.ViewHolder> {


    private LinkedHashMap<String,Object> buttonsMap = new LinkedHashMap<>();
    private ArrayList<String> buttonNames;
    private Context context;

    public PetViewButtonAdapter(Context context, LinkedHashMap<String,Object> buttonsMap ) {
        this.context = context;
        this.buttonsMap = buttonsMap;
    }

    @NonNull
    @Override
    public PetViewButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_view_button, parent, false);

        return  new PetViewButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewButtonAdapter.ViewHolder holder, int position) {

        //Updates buttonNames list
        buttonNames = new ArrayList<>(buttonsMap.keySet());

        //Get buttons name an set name to button
        position = holder.getLayoutPosition();
        String buttonName =  buttonNames.get(position);
        Button button = (Button) holder.button;
        button.setText(buttonName);

        //Button click lissener to go to activity listed in buttonsMap

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object activity = buttonsMap.get(buttonName);

                //Sets Dataheader to match the buttons name
                AppData appData = AppData.getInstance();
                appData.setDataHeader(buttonName);
                Intent intent = new Intent(context, (Class<?>) activity);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return buttonsMap.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = (Button) itemView.findViewById(R.id.petViewButton);
        }
    }

}
