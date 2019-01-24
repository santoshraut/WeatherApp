package com.examples.weatherapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.weatherapp.R;
import com.examples.weatherapp.models.WeatherDataModel;

import java.util.ArrayList;

public class WeatherDataViewAdapter extends RecyclerView.Adapter<WeatherDataViewAdapter.ContactsViewHolder> {

    private Context context;
    private ArrayList<WeatherDataModel> weather_data_list;
    public WeatherDataViewAdapter(Context context, ArrayList<WeatherDataModel> weather_data_list){
        this.weather_data_list = weather_data_list;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherDataViewAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        final WeatherDataModel current = weather_data_list.get(position);
        holder.Month.setText("Month : " +String.valueOf(current.getMonth()));
        holder.Tmin.setText("Tmin : " + String.valueOf(current.getTmin()));
        holder.Tmax.setText("Tmax : " + String.valueOf(current.getTmax()));
        holder.Rainfall.setText("Rainfall : " + String.valueOf(current.getRainfall()));
    }

    @Override
    public int getItemCount() {
        return weather_data_list.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView Month, Tmin, Tmax, Rainfall;
//        ImageView editNo, deleteNo;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            Month = itemView.findViewById(R.id.month);
            Tmin = itemView.findViewById(R.id.Tmin);
            Tmax = itemView.findViewById(R.id.Tmax);
            Rainfall = itemView.findViewById(R.id.Rainfall);
        }
    }
}
