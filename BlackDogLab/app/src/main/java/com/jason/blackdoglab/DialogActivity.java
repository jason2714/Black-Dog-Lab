package com.jason.blackdoglab;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;
import com.jason.blackdoglab.customclass.Dialog;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DialogActivity extends BaseActivity {


    private ConstraintLayout ctMain;
    private GifImageView mBgDialog;
    private ConstraintLayout ctDialogBox;
    private LinearLayout mLlDialogBox;
    private PopupWindow startPopup;
    private List<Dialog> dialogList;
    private TextView mTvDialogName, mTvDialogText;
    private ImageView mImgDialogNext, mImgDialogNote;
    private int dialogIndex;
    private boolean noteFlag;
    private FileController fcBasicInfo,fcDailyMood,fcLoginDate;

    @Override
    protected void initView() {
        mBgDialog = findViewById(R.id.gif_bg_dialog_cry);
        setImageDrawableFit(mBgDialog, R.drawable.bg_main_park_blue);

        ctMain = findViewById(R.id.cl_dialog_container);
        ctDialogBox = findViewById(R.id.cl_dialog_box_container);
        mLlDialogBox = findViewById(R.id.ll_dialog_box);
        mTvDialogName = findViewById(R.id.tv_dialog_name);
        mTvDialogText = findViewById(R.id.tv_dialog_text);
        mImgDialogNext = findViewById(R.id.img_dialog_next);
        mImgDialogNote = findViewById(R.id.img_dialog_note);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_dialog;
    }

    @Override
    protected int setThemeColor() {
        return R.style.Theme_BlackDogLab_Default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctMain.post(() -> {
            setPopupWindow();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startPopup.dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        //TODO set file controller
    }

    private void setPopupWindow() {
        //initial view
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_popup_window, null);
        startPopup = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        startPopup.showAtLocation(ctMain, Gravity.CENTER, 0, 0);
        startPopup.setFocusable(false);
        startPopup.setOutsideTouchable(false);
        startPopup.setTouchable(true);
        startPopup.getContentView().setOnClickListener(v -> {
            startPopup.dismiss();
            Utils.showBackgroundAnimator(this, 0.7f, 1.0f);
            mBgDialog.setImageResource(R.drawable.gif_dialog_cry);
            mLlDialogBox.postDelayed(() -> {
                firstSelectBox();
            }, 500);
        });
        Utils.showBackgroundAnimator(this, 1.0f, 0.7f);
    }

    private void firstSelectBox() {
        //title
        TextView mTvTitle = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mTvTitle.setLayoutParams(titleParams);
        mTvTitle.setTextColor(getResources().getColor(R.color.grey1));
        mTvTitle.setTextSize(12);
        mTvTitle.setText("為什麼他在哭呢?\n要過去陪伴他嗎?");
        mTvTitle.setLineSpacing(10, 1);
        mTvTitle.setLetterSpacing(0.2f);
        mTvTitle.setGravity(Gravity.CENTER);

        //------------------------------------relative---------------------------------------------
        //accompany
        RelativeLayout rlAccompany = new RelativeLayout(this);
        LinearLayout.LayoutParams rlAccompanyParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                getResources().getDimensionPixelOffset(R.dimen.btn_height)
        );
        int accompanyMarginVertical = (int) Utils.convertDpToPixel(this, 10);
        rlAccompanyParams.setMargins(0, accompanyMarginVertical, 0, 0);
        rlAccompany.setLayoutParams(rlAccompanyParams);
        //image
        //TODO set exp
        ImageView mImgExp = new ImageView(this);
        RelativeLayout.LayoutParams imgExpParams = new RelativeLayout.LayoutParams(
                (int) Utils.convertDpToPixel(this, 80),
                (int) Utils.convertDpToPixel(this, 30)
        );
        imgExpParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        imgExpParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mImgExp.setLayoutParams(imgExpParams);
        mImgExp.setScaleType(ImageView.ScaleType.FIT_XY);
        mImgExp.setTranslationX(Utils.convertDpToPixel(this, -100));
        setImageDrawableFit(mImgExp, R.drawable.icon_exp_plus1);
        //btn
        MaterialButton mBtnAccompany = createSelectBtn();
        MaterialButton mBtnIgnore = createSelectBtn();
        RelativeLayout.LayoutParams btnAccompanyParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        btnAccompanyParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        mBtnAccompany.setLayoutParams(btnAccompanyParams);
        mBtnAccompany.setText("陪伴他");
        mBtnAccompany.setTranslationZ(1);
        mBtnAccompany.setOnClickListener(mBtnAcn -> {
            mBtnAccompany.setOnClickListener(null);
            mBtnIgnore.setOnClickListener(null);
            setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_sad);
            mBtnAccompany.setTextColor(Color.WHITE);
            mBtnAcn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.grey1));
            ValueAnimator anim = ValueAnimator.ofInt(mBtnAcn.getMeasuredWidth(),
                    (int) Utils.convertDpToPixel(this, 145),
                    (int) Utils.convertDpToPixel(this, 145)
                    , mBtnAcn.getMeasuredWidth());
            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                btnAccompanyParams.width = val;
                mBtnAcn.setLayoutParams(btnAccompanyParams);
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mLlDialogBox.postDelayed(() -> {
                        mLlDialogBox.removeAllViews();
                        secondSelectBox();
                    }, 1000);
                }
            });
            mImgExp.animate().translationX(0).setDuration(1000);
            anim.setDuration(3000);
            anim.start();
            mHandler.postDelayed(() -> {
                mImgExp.animate().translationX(-100)
                        .alpha(0)
                        .setDuration(1000);
            }, 1500);

        });
        rlAccompany.addView(mBtnAccompany);
        rlAccompany.addView(mImgExp);
        //------------------------------------relative---------------------------------------------
        //ignore
        LinearLayout.LayoutParams ignoreParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                getResources().getDimensionPixelOffset(R.dimen.btn_height)
        );
        mBtnIgnore.setLayoutParams(ignoreParams);
        mBtnIgnore.setText("不管他");
        mBtnIgnore.setOnClickListener(view -> {
            mBtnAccompany.setOnClickListener(null);
            mBtnIgnore.setOnClickListener(null);
            mLlDialogBox.removeAllViews();
            arriveLab();
        });


        mLlDialogBox.addView(mTvTitle, 0);
        mLlDialogBox.addView(rlAccompany, 1);
        mLlDialogBox.addView(mBtnIgnore, 2);
        ctDialogBox.setBackgroundColor(Color.WHITE);
    }

    private void secondSelectBox() {
        mTvDialogName.setVisibility(View.VISIBLE);
        dialogIndex = 0;
        dialogList = new ArrayList<>();
        dialogList.add(new Dialog(0, "你還好嗎?\n為什麼在哭?"));
        dialogList.add(new Dialog(1, "我的家人對我說了很過分的話..."));
        dialogList.add(new Dialog(1, "黑狗研究所的研究員告訴我\n" +
                "他是因為生病才這樣\n" +
                "不應該把他的話聽進心裡..."));
        dialogList.add(new Dialog(1, "但我還是很難過..."));
        setCtDialogBox(dialogList.get(dialogIndex));
        dialogIndex += 1;
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    setCtDialogBox(dialogList.get(dialogIndex));
                    dialogIndex += 1;
                } else {
                    dialogIndex = 0;
                    dialogList.clear();
                    ctDialogBox.setOnClickListener(null);
                    ctDialogBox.setBackgroundColor(Color.WHITE);
                    mTvDialogName.setVisibility(View.INVISIBLE);
                    mTvDialogText.setText(null);
                    mImgDialogNext.setImageResource(0);

                    MaterialButton mBtnInjure = createSelectBtn();
                    MaterialButton mBtnComprehend = createSelectBtn();
                    LinearLayout.LayoutParams injureParams = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.btn_height)
                    );
                    mBtnInjure.setLayoutParams(injureParams);
                    mBtnInjure.setText("就算是生病也不能說出傷人的話啊!");
                    mBtnInjure.setOnClickListener(view -> {
                        mBtnInjure.setOnClickListener(null);
                        mBtnComprehend.setOnClickListener(null);
                        mBtnInjure.setTextColor(Color.WHITE);
                        view.setBackgroundColor(getResources().getColor(R.color.grey1));
                        mLlDialogBox.postDelayed(() -> {
                            mLlDialogBox.removeAllViews();
                            injureDialogBox();
                        }, 500);
                    });

                    LinearLayout.LayoutParams comprehendParams = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.dialog_big_btn_height)
                    );
                    mBtnComprehend.setLayoutParams(comprehendParams);
                    mBtnComprehend.setText("我能夠理解你的感受\n我的朋友也常會說一些傷人的話。");
                    mBtnComprehend.setOnClickListener(view -> {
                        mBtnInjure.setOnClickListener(null);
                        mBtnComprehend.setOnClickListener(null);
                        view.setBackgroundColor(getResources().getColor(R.color.grey1));
                        mBtnComprehend.setTextColor(Color.WHITE);
                        mLlDialogBox.postDelayed(() -> {
                            mLlDialogBox.removeAllViews();
                            comprehendDialogBox();
                        }, 1000);
                    });
                    mLlDialogBox.addView(mBtnInjure, 0);
                    mLlDialogBox.addView(mBtnComprehend, 1);
                }
            }
        });
    }

    private void injureDialogBox() {
        mTvDialogName.setVisibility(View.VISIBLE);
        dialogList.add(new Dialog(1, "雖然是這樣沒錯\n" +
                "不過你可能不了解憂鬱症吧..."));
        dialogList.add(new Dialog(1, "患者就是會無法控制自己的情緒..."));
        dialogList.add(new Dialog(0, "我的確是不了解\n" +
                "所以才想去黑狗研究所看看。"));
        dialogList.add(new Dialog(1, "你想去黑狗研究所?\n" +
                "直走右轉就是了。"));
        dialogList.add(new Dialog(1, "謝謝你\n" +
                "停下來關心我。"));
        setCtDialogBox(dialogList.get(dialogIndex));
        dialogIndex += 1;
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    setCtDialogBox(dialogList.get(dialogIndex));
                    dialogIndex += 1;
                } else {
                    arriveLab();
                }
            }
        });
    }

    private void comprehendDialogBox() {
        noteFlag = false;
        mTvDialogName.setVisibility(View.VISIBLE);
        dialogList.add(new Dialog(1, "沒想到你身邊也有人會這樣..."));
        dialogList.add(new Dialog(0, "對呀...我就是不知道該怎麼辦\n" +
                "才想去你剛提到的黑狗研究所看看。"));
        dialogList.add(new Dialog(1, "其實黑狗研究所的研究員有提醒我"));
        dialogList.add(new Dialog(1, "當陪伴者也非常需要其他人的陪伴，\n" +
                "狀態不好的時候才有人可以\n" +
                "抒發情緒或是協助自己。"));
        dialogList.add(new Dialog(1, "但我朋友今天在忙沒辦法陪我。"));
        dialogList.add(new Dialog(0, "那我來當你的朋友吧。"));
        dialogList.add(new Dialog(0, "我們有相似的經驗\n" +
                "你需要我的時候我可以聽你說\n" +
                "我需要你的時候你也可以幫我。"));
        dialogList.add(new Dialog(1, "好啊，謝謝你!"));
        dialogList.add(new Dialog(1, "有你陪我聊聊天心情真的有比較好。"));
        dialogList.add(new Dialog(1, "對了! 如果你想去黑狗研究所\n" +
                "從這裡直走右轉就是了喔。"));

        setCtDialogBox(dialogList.get(dialogIndex));
        dialogIndex += 1;
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    if (dialogIndex == 4) {
                        if (noteFlag)
                            return;
                        noteFlag = true;
                        setImageDrawableFit(mImgDialogNote, R.drawable.icon_note);
                        mImgDialogNote.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                        View explainView = LayoutInflater.from(DialogActivity.this).inflate(
                                R.layout.view_dialog_explain_popup_window, null);
                        PopupWindow explainPopup = new PopupWindow(explainView,
                                (int) Utils.convertDpToPixel(DialogActivity.this, 240),
                                (int) Utils.convertDpToPixel(DialogActivity.this, 96));
                        explainPopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_explain_box));
                        explainPopup.setFocusable(false);
                        explainPopup.setOutsideTouchable(false);
                        explainPopup.setTouchable(true);
                        explainPopup.showAsDropDown(mImgDialogNote, -(int) Utils.convertDpToPixel(DialogActivity.this, 230), 10);
                        mImgDialogNote.setOnClickListener(view -> {
                            view.setOnClickListener(null);
                            explainPopup.dismiss();
                            Utils.showBackgroundAnimator(this, 0.7f, 1.0f);
                            mLlDialogBox.postDelayed(() -> {
                                ValueAnimator animText = ValueAnimator.ofFloat(12, 0);
                                animText.addUpdateListener(valueAnimator -> {
                                    Float val = (Float) valueAnimator.getAnimatedValue();
                                    mTvDialogText.setTextSize(val);
                                });
                                mTvDialogText.animate().translationX((ctDialogBox.getMeasuredWidth() / 2f) - 60)
                                        .translationY((-ctDialogBox.getMeasuredHeight() / 2f) + 40)
                                        .setDuration(1000);
                                animText.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        //TODO set energy
                                        mTvDialogText.setText(null);
                                        mTvDialogText.setTextSize(12);
                                        mTvDialogText.animate().translationX(0).translationY(0);
                                        mImgDialogNote.clearColorFilter();
                                        ConstraintSet c = new ConstraintSet();
                                        ImageView mImgEnergy = new ImageView(DialogActivity.this);
                                        ConstraintLayout.LayoutParams energyParams = new ConstraintLayout.LayoutParams(0, 0);
                                        mImgEnergy.setLayoutParams(energyParams);
                                        mImgEnergy.setId(View.generateViewId());
                                        mImgEnergy.setImageResource(R.drawable.icon_energy_plus1_inverse);
                                        ctDialogBox.addView(mImgEnergy);
                                        c.clone(ctDialogBox);
                                        c.connect(mImgEnergy.getId(), ConstraintSet.TOP, ctDialogBox.getId(), ConstraintSet.TOP,
                                                getResources().getDimensionPixelOffset(R.dimen.dialog_next_margin));
                                        c.connect(mImgEnergy.getId(), ConstraintSet.END, ctDialogBox.getId(), ConstraintSet.END,
                                                getResources().getDimensionPixelOffset(R.dimen.dialog_next_margin)
                                                        + getResources().getDimensionPixelOffset(R.dimen.dialog_next_size)
                                                        + 10);
                                        c.applyTo(ctDialogBox);
                                        int energySize = (int) Utils.convertDpToPixel(DialogActivity.this, 40);
                                        ValueAnimator animEnergy = ValueAnimator.ofInt(0, energySize, energySize, 0);
                                        animEnergy.addUpdateListener(valueAnimator -> {
                                            int val = (Integer) valueAnimator.getAnimatedValue();
                                            energyParams.width = val * 2;
                                            energyParams.height = val;
                                            mImgEnergy.setLayoutParams(energyParams);
                                        });
                                        animEnergy.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                ctDialogBox.removeView(mImgEnergy);
                                                mImgDialogNote.setImageResource(0);
                                                setCtDialogBox(dialogList.get(dialogIndex));
                                                dialogIndex += 1;
                                                ctDialogBox.performClick();
                                            }
                                        });
                                        animEnergy.setDuration(2000);
                                        animEnergy.start();
                                    }
                                });
                                animText.setDuration(1000);
                                animText.start();
                            }, 200);

                        });
                        Utils.showBackgroundAnimator(this, 1.0f, 0.7f);
                    } else {
                        if (dialogIndex == 8)
                            setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_happy);
                        setCtDialogBox(dialogList.get(dialogIndex));
                        dialogIndex += 1;
                    }
                } else {
                    arriveLab();
                }
            }
        });
    }

    private void setCtDialogBox(Dialog dialog) {
        ctDialogBox.setBackgroundColor(getResources().getColor(dialog.getBgColor()));
        mTvDialogName.setText(dialog.getName());
        mTypeText.showText(mTvDialogText, dialog.getText(), 100);
        mTvDialogText.setTextColor(getResources().getColor(dialog.getTextColor()));
        setImageDrawableFit(mImgDialogNext, dialog.getNextDrawable());
    }

    private MaterialButton createSelectBtn() {
        MaterialButton btn = new MaterialButton(this);
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.grey4));
        btn.setRippleColor(ContextCompat.getColorStateList(this, R.color.grey1));
        btn.setLetterSpacing(0.15f);
        btn.setLineSpacing(10, 1);
        btn.setTextColor(getResources().getColor(R.color.grey1));
        btn.setCornerRadius((int) Utils.convertDpToPixel(this, 10));
        btn.setTextSize(12);
        btn.setElevation(5);
        return btn;
    }

    private void arriveLab() {
        ctDialogBox.setOnClickListener(null);
        mTvDialogText.setText(null);
        mTvDialogName.setVisibility(View.INVISIBLE);
        mImgDialogNext.setImageResource(0);
        ctDialogBox.setBackgroundColor(Color.TRANSPARENT);
        mBgDialog.animate().alpha(0).setDuration(500);
        mBgDialog.postDelayed(() -> {
            ConstraintSet c = new ConstraintSet();
            VideoView mVvWhiteDog = new VideoView(this);
            ConstraintLayout.LayoutParams videoParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mVvWhiteDog.setLayoutParams(videoParams);
            ctMain.addView(mVvWhiteDog);
            mVvWhiteDog.setOnPreparedListener(mp -> mVvWhiteDog.start());
            mVvWhiteDog.setOnCompletionListener(mp -> {
                ctMain.removeView(mVvWhiteDog);
                firstLabDialog();
            });
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            Uri animationUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_white_dog);
            mVvWhiteDog.setVideoURI(animationUri);
        }, 500);
    }

    private void firstLabDialog() {
        setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_lab);
        mBgDialog.animate().alpha(1).setDuration(2000);
    }

}