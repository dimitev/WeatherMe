package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

import static screamofwoods.weatherme.CityInfo.getWeatherConditionImage;

public class FiveDay extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 4;
    private String condition = "";
    private String date = "";
    private float minTemp = (float) 0.0;
    private float maxTemp = (float) 0.0;

    @Override
    public String toString() {
        return "FiveDay[condition=" + condition+ ", date=" + date + ", minTemp=" + minTemp+
                ", maxTemp=" + maxTemp + "]";
    }

    public FiveDay(){ }

    public void Copy(FiveDay n)
    {
        this.condition=n.condition;
        this.date=n.date;
        this.minTemp=n.minTemp;
        this.maxTemp=n.maxTemp;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyPropertyChanged(BR._all);
    }
    @Bindable
    public Drawable getImage() {
        return getWeatherConditionImage(this.condition);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
        notifyPropertyChanged(BR._all);
    }
}
