package com.jason.blackdoglab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.jason.blackdoglab.adapter.CardAdapter;
import com.jason.blackdoglab.customclass.Food;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;
import com.jason.blackdoglab.view.FrameSurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DogActivity extends BaseActivity {

    private HashMap<String, Integer> dogMotion;
    private GifImageView mGifDogBg;
    private ImageView mBgDog;
    private ImageButton mBtnLeft, mBtnRight;
    private ImageView mImgThreeBowls;
    private HashMap<String, ImageView> notifications;
    private final String[] dogColors = {"yellow", "blue", "red"};
    private LinearLayout mLlBtnContainer;
    private List<Food> foodList;
    private ConstraintLayout mClDogContainer;
    private HashMap<String, FrameSurfaceView> dogs;

    @Override
    protected void initView() {
        dogs = new HashMap<>();
        for (String dogColor : dogColors) {
            String mDogID = dogColor + "_dog";
            Utils.setLog(mDogID);
            int resID = getResources().getIdentifier(mDogID, "id", getPackageName());
            dogs.put(dogColor, findViewById(resID));
        }
        dogs.get("red").setDuration(10);
        dogs.get("red").setOneShot(true);
        dogs.get("red").setKeepLastFrame(true);
        dogs.get("red").setSingleBitmap(R.drawable.red_dog_sleep000);
        dogs.get("red").setOnTop(true);
        dogs.get("red").start();

        mGifDogBg = findViewById(R.id.gif_dog_background);
        mGifDogBg.setImageResource(R.drawable.gif_dog_bg);

        mBgDog = findViewById(R.id.bg_dog);
        setImageDrawableFit(mBgDog, R.drawable.bg_dog);


        mBtnLeft = findViewById(R.id.btn_dog_left);
        mBtnRight = findViewById(R.id.btn_dog_right);
        mImgThreeBowls = findViewById(R.id.img_three_bowls);
        setImageDrawableFit(mImgThreeBowls, R.drawable.bg_three_dog_bowls);
        //no use, not same frame
//        mImgThreeBowls.bringToFront();

        notifications = new HashMap<>();
        for (String dogColor : dogColors) {
            ImageView ntfView;
            String mNtfID = "dog_ntf_" + dogColor;
            int resID = getResources().getIdentifier(mNtfID, "id", getPackageName());
            //set id
            ntfView = findViewById(resID);
            notifications.put(dogColor, ntfView);
            //set bg
            ntfView.setTag(dogColor);
        }

        mLlBtnContainer = findViewById(R.id.ll_dog_btn_container);
        mClDogContainer = findViewById(R.id.cl_dog_container);
    }

    @Override
    protected void initListener() {
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
//        dogs.get("red").setOnClickListener(this);
        for (String dogColor : dogColors)
            notifications.get(dogColor).setOnClickListener(this);
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_dog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().cleanActivity(this);
        ActivityUtils.getInstance().printActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.red_dog:
//                if (!dogs.get("red").isRunning()) {
//                    int motion = (int) (Math.random() * 5);
//                    String[] move = {"eat", "sit", "sleep", "stand", "walk"};
//                    dogs.get("red").setBitmaps(getMotionList("red", move[motion]));
//                    dogs.get("red").start();
//                }
//                break;
            case R.id.btn_dog_left:
                Intent intent = new Intent(DogActivity.this, MainPage.class);
                startActivity(intent);
                break;
            case R.id.btn_dog_right:
                for (String dogColor : dogColors)
                    setNtfLocation(dogColor);
                break;
            case R.id.dog_ntf_yellow:
            case R.id.dog_ntf_red:
            case R.id.dog_ntf_blue:
                if (v instanceof ImageView) {
                    removeNtfIcon();
//                    mLlBtnContainer.removeView(mImgThreeBowls);
                    mLlBtnContainer.removeAllViews();
                    Utils.setLog(v.getTag() + "");
                    String dogColor = (String) v.getTag();
                    String mNtfDrawable = "bg_dog_" + dogColor;
                    int resID = getResources().getIdentifier(mNtfDrawable, "drawable", getPackageName());
                    setImageDrawableFit(mBgDog, resID);
                    mLlBtnContainer.setOrientation(LinearLayout.HORIZONTAL);
                    createSelectBtn(dogColor);
                    dogs.get("red").setTranslationX(Utils.convertDpToPixel(this, 51));
                    dogs.get("red").setTranslationY(Utils.convertDpToPixel(this, 254));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int setThemeColor() {
        return getThemeColor();
    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        getThemeColor();
        initDogMotion();
        initDogFood();
    }

    private int getThemeColor() {
        int themeColor = R.style.Theme_BlackDogLab_Default;
        fc_loginDate = new FileController(this, getResources().getString(R.string.login_date));
        fc_dailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        try {
            //set basic theme color
            String[] splitFileData = fc_dailyMood.readFileSplit();
            for (String lineData : splitFileData) {
                String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                if (lineDataArray[0].equals(fc_loginDate.readFile())) {
                    Utils.setLog("Mood Type = " + lineDataArray[1]);
                    switch (Integer.parseInt(lineDataArray[1])) {
                        case 0:
                        case 1:
                            themeColor = R.style.Theme_BlackDogLab_Blue;
                            break;
                        case 2:
                            themeColor = R.style.Theme_BlackDogLab_Green;
                            break;
                        case 3:
                        case 4:
                            themeColor = R.style.Theme_BlackDogLab_Brown;
                            break;
                        default:
                            themeColor = R.style.Theme_BlackDogLab_Default;
                            Utils.setLog("Not Set Today's Mood Yet");
                            break;
                    }
                }
            }
            Utils.setLog("Set Theme Success");
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
        return themeColor;
    }

    private List<Integer> getMotionList(String dogColor, String motion) {
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
            String mDrawableName = dogColor + "_dog_" + motion + id;
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            bitmaps.add(resID);
        }
        return bitmaps;
    }

    private void initDogMotion() {
        dogMotion = new HashMap<>();
        dogMotion.put("eat", 289);
        dogMotion.put("sit", 116);
        dogMotion.put("sleep", 116);
        dogMotion.put("stand", 116);
        dogMotion.put("walk", 115);
    }

    private void setNtfLocation(String ntfColor) {
        if (!notifications.containsKey(ntfColor)) {
            Utils.setLog("Don't Have Color : " + ntfColor);
            return;
        }
        setImageDrawableFit(notifications.get(ntfColor),
                Utils.getAttrID(DogActivity.this, R.attr.notification, Utils.RESOURCE_ID));
        Utils.setLog("set Drawable " + ntfColor + " success");
    }

    private void removeNtfIcon() {
        for (String dogColor : dogColors)
            notifications.get(dogColor).setImageResource(0);
    }

    private void createSelectBtn(String dogColor) {
        //Btn Feed
        MaterialButton mBtnFeed = new MaterialButton(this);
        mBtnFeed.setWidth(getResources().getDimensionPixelOffset(R.dimen.dog_btn_width));
        mBtnFeed.setHeight(getResources().getDimensionPixelOffset(R.dimen.btn_height));
        mBtnFeed.setIconTintResource(R.color.blue2);
        mBtnFeed.setText("餵食");
        mBtnFeed.setTextSize(14);
        mBtnFeed.setCornerRadius((int) Utils.convertDpToPixel(this, 10));
        mBtnFeed.setLetterSpacing(0.4f);
        mBtnFeed.setOnClickListener(view -> {
            mLlBtnContainer.removeAllViews();
            dogs.get("red").setTranslationY(Utils.convertDpToPixel(this, 0));
            String mDrawableID = "bg_dog_feed_" + dogColor;
            int resID = getResources().getIdentifier(mDrawableID, "drawable", getPackageName());
            setImageDrawableFit(mBgDog,resID);
            createFoodPager(dogColor);
        });
        //Btn Feed
        MaterialButton mBtnAccompany = new MaterialButton(this);
        LinearLayout.LayoutParams accompanyParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.dog_btn_width),
                getResources().getDimensionPixelOffset(R.dimen.btn_height)
        );
        int accompanyMargin = (int) Utils.convertDpToPixel(this, 40);
        accompanyParams.setMargins(accompanyMargin, 0, 0, 0);
        mBtnAccompany.setLayoutParams(accompanyParams);
        mBtnAccompany.setIconTintResource(R.color.blue2);
        mBtnAccompany.setText("陪伴");
        mBtnAccompany.setTextSize(14);
        mBtnAccompany.setCornerRadius((int) Utils.convertDpToPixel(this, 10));
        mBtnAccompany.setLetterSpacing(0.4f);
        mBtnAccompany.setOnClickListener(view -> {
            mLlBtnContainer.removeAllViews();
        });
        mLlBtnContainer.addView(mBtnFeed, 0);
        mLlBtnContainer.addView(mBtnAccompany, 1);
    }

    private void initDogFood() {
        foodList = new ArrayList<>();
        foodList.add(new Food(R.drawable.icon_food_banana, "香蕉", "香蕉是色胺素和維生素B6的主要來源，是營養素和神經傳導的重要物質。它能夠幫助大腦製造血清素，緩解緊張的情緒。含有生物鹼（alkaloid），有助於提升精神和自信。鎂的含量很高，若是再補充足夠的鈣便可讓情緒穩定下來。此外，香蕉裡的酪胺酸能增加大腦中多巴胺的分泌量，可以提高創造性思維能力，解決生活上的疑難雜症。"));
        foodList.add(new Food(R.drawable.icon_food_cauliflower, "花椰菜", "花椰菜有豐富的蘿蔔硫素，是一種能保護神經的物質，有神經安定和抗憂鬱的功用。高膳食纖維的食物，如：花椰菜、地瓜葉、洋蔥、青蔥、大蒜等都是腸道好菌的最愛。腸道的環境只要足夠好，身體就能合成出足夠的血清素，並且還能合成出助眠物質「褪黑激素」。所以一旦腸胃發炎則會影響大腦的運作，會讓人情緒低落，因此好菌是保護腸道健康的重要因素。"));
        foodList.add(new Food(R.drawable.icon_food_cheese, "起司", "起司內含色胺酸，吃下去後能讓大腦和心情變得更加開心。它也富含蛋白質，因此能夠幫助身體吸收所需的養分。此外，像是牛奶、蛋黃等食物其實都很類似，不僅可以幫助抗憂鬱，對身體還有很大的幫助。"));
        foodList.add(new Food(R.drawable.icon_food_egg, "蛋", "蛋有優質蛋白質的成分，富含色胺酸、酪胺酸及卵磷脂。色胺酸幫助人們穩定心情，酪胺酸可提高人們的專注力而卵磷脂可以幫助人們對抗壓力及強化神經傳導功能。但是要注意的是蛋黃有膽固醇高的疑慮，因此一般人一天只能吃一顆雞蛋，有高血脂的人一個禮拜只能吃三顆。"));
        foodList.add(new Food(R.drawable.icon_food_fishoil_bottle, "深海魚油", "魚油中的OMEGA-3脂肪酸是組成大腦及神經細胞與傳導不可缺少的成分。它除了提供豐富的優質蛋白質外，因其具有抗氧化和清除自由基能力，使得抗發炎功效增加血清素的分泌量，改善憂鬱情緒，讓心情變得更好。富含OMEGA-3的深海魚有：鮪魚、鯖魚、鮭魚、沙丁魚等等。"));
        foodList.add(new Food(R.drawable.icon_food_grapefruit, "葡萄柚", "維生素C是腎上腺素的原始材料，可當作抗氧化劑。當人面臨很大的壓力時會分泌大量的腎上腺素，且消耗大量的維生素C。平時應該要多吃富有維生素C高的水果，如：葡萄柚、柳丁等柑橘類水果。它可以有效的緩解緊張的情緒，協助人體製造副腎上腺皮質素來對抗壓力，更是人體製造多巴胺時不可或缺的重要成分之一。除此之外，它獨特的芳香不但可以淨化繁瑣的思維，還可以幫助提神。"));
        foodList.add(new Food(R.drawable.icon_food_nut, "堅果", "堅果類食物屬於多元不飽和脂肪酸，含有很多色胺酸及鈣質能讓血管細胞更加柔軟並對神經傳導有幫助。它可以增加大腦中的血清素分泌，減輕壓力及改善失眠的問題並且穩定情緒。"));
        foodList.add(new Food(R.drawable.icon_food_spinach, "菠菜", "菠菜含有葉酸，可合成血清素，有舒緩神經達到放鬆的狀態。雖然大部分的綠色蔬菜和水果都有葉酸的成分在，但CP值最高的還是非菠菜莫屬。此外，它也有鐵質的成分，有助於大腦神經細胞的代謝和平衡情緒的作用。"));
    }

    private class CustomPagerTransformer implements ViewPager.PageTransformer {
        private int maxTranslateOffsetX;
        private ViewPager viewPager;

        public CustomPagerTransformer(Context context) {
            this.maxTranslateOffsetX = (int) Utils.convertDpToPixel(context, 100);
        }

        public void transformPage(View view, float position) {
            if (viewPager == null) {
                viewPager = (ViewPager) view.getParent();
            }
            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
            int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
            float offsetRate = (float) offsetX * 0.48f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                //不用往下
//                view.setTranslationX(-maxTranslateOffsetX * offsetRate);
            }
        }
    }

    private void createFoodPager(String dogColor) {
        //initial viewpager
        ViewPager foodPager = new ViewPager(this);
        int pageHeight = (int) getResources().getDimensionPixelSize(R.dimen.dog_food_item_height) + 35;
        int pagePadding = (int) (Utils.getScreenSizePixel(this).getWidth() -
                getResources().getDimensionPixelSize(R.dimen.dog_food_item_width) -
                2 * getResources().getDimensionPixelSize(R.dimen.dog_food_item_margin)) / 2;
        LinearLayout.LayoutParams pagerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, pageHeight);
        foodPager.setLayoutParams(pagerParams);
        foodPager.setClipToPadding(false);
        foodPager.setClipChildren(false);
        foodPager.setPadding(pagePadding, 0, pagePadding, 0);
//        foodPager.setBackgroundResource(R.color.blue4);
        mLlBtnContainer.addView(foodPager);
        Utils.setLog("food pager padding = " + pagePadding);
        //set adapter and pageTransformer
        foodPager.setAdapter(new CardAdapter(this, foodList, dogs.get(dogColor),mLlBtnContainer));
        foodPager.setPageTransformer(false, new CustomPagerTransformer(this));
        foodPager.setOffscreenPageLimit(foodList.size() - 1);
    }


}