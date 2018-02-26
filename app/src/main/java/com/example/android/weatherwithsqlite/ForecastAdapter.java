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

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by hessro on 5/10/17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastItemViewHolder> {

    private ArrayList<OpenWeatherMapUtils.ForecastItem> mForecastItems;
    private OnForecastItemClickListener mForecastItemClickListener;
    private Context mContext;

    public interface OnForecastItemClickListener {
        void onForecastItemClick(OpenWeatherMapUtils.ForecastItem forecastItem);
    }

    public ForecastAdapter(Context context, OnForecastItemClickListener clickListener) {
        mContext = context;
        mForecastItemClickListener = clickListener;
    }

    public void updateForecastItems(ArrayList<OpenWeatherMapUtils.ForecastItem> forecastItems) {
        mForecastItems = forecastItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mForecastItems != null) {
            return mForecastItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_item, parent, false);
        return new ForecastItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        holder.bind(mForecastItems.get(position));
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mForecastDateTV;
        private TextView mForecastTempDescriptionTV;
        private final DateFormat mDateFormatter = DateFormat.getDateTimeInstance();

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastDateTV = itemView.findViewById(R.id.tv_forecast_date);
            mForecastTempDescriptionTV = itemView.findViewById(R.id.tv_forecast_temp_description);
            itemView.setOnClickListener(this);
        }

        public void bind(OpenWeatherMapUtils.ForecastItem forecastItem) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String temperatureUnitsValue = sharedPreferences.getString(
                    mContext.getString(R.string.pref_units_key),
                    mContext.getString(R.string.pref_units_default_value)
            );
            String temperatureUnitsAbbr = OpenWeatherMapUtils.getTemperatureUnitsAbbr(mContext, temperatureUnitsValue);

            String dateString = mDateFormatter.format(forecastItem.dateTime);
            String detailString = forecastItem.temperature + temperatureUnitsAbbr + " - " + forecastItem.description;
            mForecastDateTV.setText(dateString);
            mForecastTempDescriptionTV.setText(detailString);
        }

        @Override
        public void onClick(View v) {
            OpenWeatherMapUtils.ForecastItem forecastItem = mForecastItems.get(getAdapterPosition());
            mForecastItemClickListener.onForecastItemClick(forecastItem);
        }
    }
}
