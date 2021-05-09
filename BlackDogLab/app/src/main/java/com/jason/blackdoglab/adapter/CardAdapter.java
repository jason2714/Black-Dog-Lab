package com.jason.blackdoglab.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.button.MaterialButton;
import com.jason.blackdoglab.DogActivity;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.customclass.Food;
import com.jason.blackdoglab.utils.Utils;
import com.jason.blackdoglab.view.FrameSurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardAdapter extends PagerAdapter {

    private List<Food> foodList;
    private Context context;
    private PopupWindow popupWindow;
    private FrameSurfaceView dog;
    private LinearLayout ctLayout;

    public CardAdapter(Context context, List<Food> foodList, FrameSurfaceView dog, LinearLayout ctLayout) {
        this.foodList = foodList;
        this.context = context;
        this.dog = dog;
        this.ctLayout = ctLayout;
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
        CardView mCvFood = view.findViewById(R.id.cv_food);
        ImageView mImgFood = view.findViewById(R.id.img_food);
        TextView mTvFoodName = view.findViewById(R.id.tv_food_name);
        setImageDrawableFit(mImgFood, foodList.get(position).getDrawableID());
        mTvFoodName.setText(foodList.get(position).getName());
        mCvFood.setClickable(true);
        mCvFood.setOnClickListener(v -> {
            dog.setVisible(false);
            setPopupWindow(container, foodList.get(position));
            container.setVisibility(View.INVISIBLE);
        });
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

    private void setPopupWindow(ViewGroup container, Food food) {
        //initial view
        View view = LayoutInflater.from(context).inflate(R.layout.view_food_popup_window, null);
        ImageView mImgFood = view.findViewById(R.id.img_food_display);
        TextView mTvFoodDes = view.findViewById(R.id.tv_food_description);
        MaterialButton mBtnSubmit = view.findViewById(R.id.btn_food_submit);
        ImageView mBtnCancel = view.findViewById(R.id.btn_cancel);
        setImageDrawableFit(mImgFood, food.getDrawableID());
        setImageDrawableFit(mBtnCancel, R.drawable.icon_cancel);
        mTvFoodDes.setText(food.getDescription());
        mBtnCancel.setOnClickListener(v -> {
            dog.setVisible(true);
            container.setVisibility(View.VISIBLE);
            popupWindow.dismiss();
        });
        mBtnSubmit.setOnClickListener(v -> {
            ViewGroup.LayoutParams params = dog.getLayoutParams();
            params.width = context.getResources().getDimensionPixelOffset(R.dimen.dog_eat);
            params.height = context.getResources().getDimensionPixelOffset(R.dimen.dog_eat);
            dog.setLayoutParams(params);
            dog.animate()
                    .translationX(Utils.convertDpToPixel(context, 27))
                    .translationY(Utils.convertDpToPixel(context, 206));
            ctLayout.removeView(container);
            ctLayout.setOrientation(LinearLayout.VERTICAL);
            popupWindow.dismiss();
            //dog bowl
            ImageView dogBowl = new ImageView(context);
            LinearLayout.LayoutParams dogBowlParams = new LinearLayout.LayoutParams(
                    context.getResources().getDimensionPixelOffset(R.dimen.dog_bowl_width),
                    context.getResources().getDimensionPixelOffset(R.dimen.dog_bowl_height));
            setImageDrawableFit(dogBowl, R.drawable.icon_dog_bowl);
            //food
            ImageView imgFood = new ImageView(context);
            LinearLayout.LayoutParams foodParams = new LinearLayout.LayoutParams(
                    context.getResources().getDimensionPixelOffset(R.dimen.dog_food_eat_size),
                    context.getResources().getDimensionPixelOffset(R.dimen.dog_food_eat_size));
            if (food.getDrawableID() == R.drawable.icon_food_fishoil_bottle)
                setImageDrawableFit(imgFood, R.drawable.icon_food_fishoil);
            else
                setImageDrawableFit(imgFood, food.getDrawableID());
//            bring to front
            imgFood.setZ(1);
            int foodStartY = ctLayout.getMeasuredHeight() -
                    context.getResources().getDimensionPixelOffset(R.dimen.dog_bowl_width);
            imgFood.setTranslationY(Utils.convertDpToPixel(context, -foodStartY));
            int startDelay = 500;
            int fallDuration = 2000;
            int eatDuration = 2000;

            imgFood.animate()
                    .translationY(Utils.convertDpToPixel(context, 40))
                    .setDuration(fallDuration)
                    .setStartDelay(startDelay);
            ctLayout.addView(imgFood, 0, foodParams);
            ctLayout.addView(dogBowl, 1, dogBowlParams);
            ctLayout.postDelayed(() -> dog.setVisible(true), startDelay + fallDuration);
            dog.setBitmaps(getMotionList("eat"));
            dog.setOneShot(true);
            dog.setKeepLastFrame(true);
            dog.setTotalDuration(eatDuration);
            dog.startDelay(1000 + startDelay + fallDuration);
            imgFood.postDelayed(() -> {
                ctLayout.removeAllViews();
                if (context instanceof DogActivity) {
                    ((DogActivity) context).createSelectBtn((String) dog.getTag(),1);
                }
            }, startDelay + fallDuration + eatDuration * 5);
        });

//      match parent to prevent click outside
        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(container, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(() -> {
            Utils.setLog("dismiss");
            Utils.showBackgroundAnimator(context, 0.5f, 1.0f);
        });
        Utils.showBackgroundAnimator(context, 1.0f, 0.5f);
    }

    private List<Integer> getMotionList(String motion) {
        HashMap<String, Integer> dogMotion = new HashMap<>();
        dogMotion.put("eat", 289);
        dogMotion.put("sit", 116);
        dogMotion.put("sleep", 116);
        dogMotion.put("stand", 116);
        dogMotion.put("walk", 115);
        if (!dogMotion.containsKey(motion)) {
            Utils.setLog("don't have motion : " + motion);
            return null;
        }
        List<Integer> bitmaps = new ArrayList<>();
        for (int i = 0; i < dogMotion.get(motion); i++) {
            String id;
            if (i < 10)
                id = "00" + i;
            else if (i >= 10 && i < 100)
                id = "0" + i;
            else
                id = String.valueOf(i);
            String mDrawableName = dog.getTag() + "_dog_" + motion + id;
            int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
            bitmaps.add(resID);
        }
        return bitmaps;
    }
}
