package com.niedzwiecki.przemyslguide.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private List<PlaceOfInterest> places;

    public PlacesAdapter() {
        places = new ArrayList<>();
    }

    public void setPlaces(List<PlaceOfInterest> placeOfInterests) {
        places = placeOfInterests;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new PlacesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int position) {
        PlaceOfInterest placeOfInterest = places.get(position);
//        holder.hexColorView.setImageURI(ribot.image);
//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
        if (Utils.isEmpty(placeOfInterest.image)) {
            return;
        }

        ImageView hexColorView = (ImageView) holder.hexColorView.findViewById(R.id.view_hex_color);
        TextView nameTextView = (TextView) holder.nameTextView.findViewById(R.id.text_name);
        TextView emailTextView = (TextView) holder.emailTextView.findViewById(R.id.text_email);

        Picasso.with(hexColorView.getContext())
                .load(placeOfInterest.image)
                .resize(700, 700)
                .centerCrop()
                .into(hexColorView);
        nameTextView.setText(String.format("%s",
                placeOfInterest.name));
        emailTextView.setText(placeOfInterest.email);
        holder.setIsRecyclable(true);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public PlaceOfInterest getPlace(int position) {
        return places.get(position);
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {

        ImageView hexColorView;
        TextView nameTextView;
        TextView emailTextView;


        public PlacesViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }
}
