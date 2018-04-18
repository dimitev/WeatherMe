package screamofwoods.weatherme;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import screamofwoods.weatherme.databinding.SingleCityBinding;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<CityInfo> mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SingleCityBinding mBinding;

        public MyViewHolder(SingleCityBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
        public void bind(@NonNull CityInfo city) {
            mBinding.setCity(city);
            mBinding.executePendingBindings();

        }

    }

    public MyAdapter(List<CityInfo> cities) {
        this.mDataset = cities;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater itemView = LayoutInflater.from(parent.getContext());
        SingleCityBinding binding = DataBindingUtil.inflate(itemView, R.layout.single_city, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CityInfo city = mDataset.get(position);
        holder.bind(city);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}