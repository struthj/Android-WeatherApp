package com.example.android.weatherwithsqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.util.ArrayList;

/**
 * Created by hessro on 5/10/17.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationItemViewHolder> {

    private ArrayList<OpenWeatherMapUtils.LocationResult> locationItems;
    private OnLocationItemClickListener mLocationItemClickListener;
    private Context mContext;

    public interface OnLocationItemClickListener {
        void onLocationItemClick(OpenWeatherMapUtils.LocationResult item);
    }

    public LocationAdapter(Context context, OnLocationItemClickListener clickListener) {
        mContext = context;
        mLocationItemClickListener = clickListener;
    }

    public void updateLocationItems(ArrayList<OpenWeatherMapUtils.LocationResult> locationItem) {
        locationItems = locationItem;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (locationItems != null) {
            return locationItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public LocationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.location_item, parent, false);
        return new LocationItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationItemViewHolder holder, int position) {
        holder.bind(locationItems.get(position));
    }

    class LocationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mLocationResult;

        public LocationItemViewHolder(View itemView) {
            super(itemView);
            mLocationResult = itemView.findViewById(R.id.tv_loc_result);
            itemView.setOnClickListener(this);
        }

        public void bind(OpenWeatherMapUtils.LocationResult item) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String detailString = item.locationDesc;
            mLocationResult.setText(detailString);
        }

        @Override
        public void onClick(View v) {
            OpenWeatherMapUtils.LocationResult item = locationItems.get(getAdapterPosition());
            mLocationItemClickListener.onLocationItemClick(item);
        }
    }
}
