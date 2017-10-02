package com.niedzwiecki.przemyslguide.ui.main;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private List<InterestPlace> places;

    @Inject
    public PlacesAdapter() {
        places = new ArrayList<>();
    }

    public void setPlaces(List<InterestPlace> ribots) {
        places = ribots;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new PlacesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int position) {
        InterestPlace ribot = places.get(position);
//        holder.hexColorView.setImageURI(ribot.image);
//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
        Picasso.with(holder.hexColorView.getContext())
                .load(ribot.image)
                .resize(700, 700)
                .centerCrop()
                .into(holder.hexColorView);
        holder.nameTextView.setText(String.format("%s",
                ribot.name));
        holder.emailTextView.setText(ribot.address);
        holder.setIsRecyclable(true);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public InterestPlace getPlace(int position) {
        return places.get(position);
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {

        @BindView(R.id.view_hex_color)
        ImageView hexColorView;

        @Nullable
        @BindView(R.id.text_name)
        TextView nameTextView;
        @BindView(R.id.text_email)
        TextView emailTextView;

        public PlacesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }
}
