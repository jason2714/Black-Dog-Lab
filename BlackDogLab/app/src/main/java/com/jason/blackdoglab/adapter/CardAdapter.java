package com.jason.blackdoglab.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.jason.blackdoglab.R;
import com.jason.blackdoglab.customclass.Food;
import com.jason.blackdoglab.utils.Utils;

import java.util.List;

public class CardAdapter extends PagerAdapter {

    private List<Food> foodList;
    private Context context;

    public CardAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dog_food_item, container, false);
        ImageView mImgFood = view.findViewById(R.id.img_food);
        TextView mTvFoodName = view.findViewById(R.id.tv_food_name);
        setImageDrawableFit(mImgFood, foodList.get(position).getDrawableID());
        mTvFoodName.setText(foodList.get(position).getName());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private Bitmap decodeBitmap(int drawableID, int ctWidth, int ctHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Get Bitmap width and height without loading into memory
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
//        Utils.setLog("Real Image Width：" + realWidth + ",Height:" + realHeight);
//        Utils.setLog("view Width：" + ctWidth + ",Height:" + ctHeight);
        // Calculate scale
        float scale = Utils.getDensity(context);
        if (realHeight > ctHeight)
            scale *= (realHeight / ctHeight);
//        Utils.setLog("scale : " + (int) scale);
        options.inSampleSize = (int) scale;
        options.inJustDecodeBounds = false;
//        calculate density yourself will have closer resolution
//        options.inDensity = densityDpi;
        // Set options.inJustDecodeBounds to false to read the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
//        Utils.setLog("Image After Scaling Width:" + w + ",Height:" + h);
        return bitmap;
    }

    private void setImageDrawableFit(ImageView imageView, int drawableID) {
        imageView.post(() -> {
            imageView.setImageBitmap(decodeBitmap(drawableID, imageView.getWidth(), imageView.getHeight()));
        });
    }
}
