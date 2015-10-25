package com.moopflops.safespace.ui.adapters;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.RatingUtils;
import com.moopflops.safespace.engine.model.RatedCarPark;

import java.util.List;

/**
 * Created by tom on 25/10/2015.
 */
public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.CPViewHolder> {

  List<RatedCarPark> mCarParks;

  public CarParkAdapter(List<RatedCarPark> carParks) {
    mCarParks = carParks;
  }

  @Override
  public CPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(CPViewHolder holder, int position) {
    RatedCarPark carPark = mCarParks.get(position);
    holder.bindData(carPark);
  }

  @Override
  public int getItemCount() {
    return mCarParks.size();
  }

  static class CPViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView spaces;
    TextView rating;
    RelativeLayout background;

    public CPViewHolder(View itemView) {
      super(itemView);

      title = (TextView) itemView.findViewById(R.id.preview_carpark_name);
      spaces = (TextView) itemView.findViewById(R.id.preview_spaces_available);
      rating = (TextView) itemView.findViewById(R.id.preview_safety_rating);
      background = (RelativeLayout) itemView.findViewById(R.id.preview_safety_circle_background);
    }

    public void bindData(RatedCarPark carPark) {
      title.setText(carPark.carPark.name);
      spaces.setText(carPark.carPark.spacesNow+"");
      rating.setText(RatingUtils.getRating(carPark.rating));

      Drawable tintedDrawable = itemView.getContext().getResources().getDrawable(R.drawable.safety_circle).getConstantState().newDrawable().mutate();
      tintedDrawable.setColorFilter(new PorterDuffColorFilter(RatingUtils.getColour(itemView.getContext(), carPark.rating), PorterDuff.Mode.MULTIPLY));

      background.setBackground(tintedDrawable);
    }
  }
}
