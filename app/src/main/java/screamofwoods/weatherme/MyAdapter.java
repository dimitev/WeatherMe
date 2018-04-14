package screamofwoods.weatherme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<CityInfo> mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView city, country;

        public MyViewHolder(View view) {
            super(view);
            city = (TextView) view.findViewById(R.id.city_name);
            country = (TextView) view.findViewById(R.id.country);
        }
    }

    public MyAdapter(List<CityInfo> cities) {
        this.mDataset = cities;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_city, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CityInfo city = mDataset.get(position);
        holder.city.setText(city.getName());
        holder.country.setText(city.getCountry());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}