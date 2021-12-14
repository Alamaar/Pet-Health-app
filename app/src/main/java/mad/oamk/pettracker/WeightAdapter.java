package mad.oamk.pettracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import mad.oamk.pettracker.models.Pet;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {

    String data1[];
    int image;
    private Context context;

    //Array list to contain all pet id's
    private List<String[]> stringArrayList = new ArrayList<String[]>();



    public WeightAdapter(Context ct, List<String[]> stringArrayList) {
        this.stringArrayList = stringArrayList;
        context = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.weight_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myText1.setText("testtest");
        String[] list = stringArrayList.get(position);
        String date = list[0];
        holder.myText1.setText(date);
        String weight = list[1];
        holder.myText2.setText(weight);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView myText1;
        TextView myText2;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.weightlisttextview);
            myText2 = itemView.findViewById(R.id.weightlisttextview2);

        }
    }
}
