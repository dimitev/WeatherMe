package screamofwoods.weatherme;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import screamofwoods.weatherme.databinding.SingleHourBinding;

public class MyAdapterHourly extends RecyclerView.Adapter<MyAdapterHourly.MyViewHolder> {
    private Hourly[] mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SingleHourBinding mBinding;

        public MyViewHolder(SingleHourBinding binding) {
            super(binding.getRoot());
            mBinding=binding;
        }
        public void bind(@NonNull Hourly hour) {
            mBinding.setHour(hour);
            mBinding.executePendingBindings();

        }

    }

    public MyAdapterHourly(Hourly[] hours) {
        this.mDataset = hours;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater itemView = LayoutInflater.from(parent.getContext());
        SingleHourBinding binding = DataBindingUtil.inflate(itemView, R.layout.single_hour, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hourly hour = mDataset[position];
        holder.bind(hour);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        mDataset=new Hourly[24];
        return mDataset.length;
    }
}