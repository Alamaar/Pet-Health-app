package mad.oamk.pettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import mad.oamk.pettracker.R;
import mad.oamk.pettracker.customdata.adapters.CustomDataViewAdapter;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {

    private ArrayList<String> IdList;
    private LinkedHashMap<String,Object> imagesHasMAp;
    private DatabaseReference databaseReference;
    public ImageViewAdapter(ArrayList<String> idList, LinkedHashMap<String, Object> imagesHasMAp, DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.IdList = idList;
        this.imagesHasMAp = imagesHasMAp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_list_view, parent, false);

        return new ImageViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        position = holder.getAdapterPosition();

        String key = IdList.get(position);


        Map<String,Object> map = (Map<String, Object>) imagesHasMAp.get(key);
        if (map == null) {
            return;
        }

        String imageUrl = (String) map.get("ImageUrl");


        Glide.with(holder.itemView).load(imageUrl).into(holder.imageView);


        //todo poisto





    }

    @Override
    public int getItemCount() {
        if ( IdList == null){
            return 0;
        }
        return IdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_list_view_image);

        }
    }
}
