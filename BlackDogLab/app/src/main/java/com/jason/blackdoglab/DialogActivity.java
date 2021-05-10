package com.jason.blackdoglab;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
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
import com.jason.blackdoglab.customclass.DailyMoods;
import com.jason.blackdoglab.customclass.Dialog;
import com.jason.blackdoglab.loginpage.FirstLoginActivity;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    private ImageView mImgDialogChar;
    private boolean exit;
    private final int REQUEST_CODE_LOGIN = 1;
    private final int REQUEST_CODE_FEED_DOG = 2;
    private int themeColor = R.style.Theme_BlackDogLab_Default;
    private FileController[] fcPoints, fcNotes;

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
        mImgDialogChar = findViewById(R.id.img_dialog_character);
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
//            firstLabDialog();
//            introduceLab();
//            mImgDialogChar.animate().alpha(1);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (startPopup != null)
            startPopup.dismiss();
        Utils.setLog("Activity Destroy");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialogList = new ArrayList<>();
        fcBasicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fcDailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        fcLoginDate = new FileController(this, getResources().getString(R.string.login_date));
        String[] noteArray = getResources().getStringArray(R.array.note_types_file_array);
        String[] pointArray = getResources().getStringArray(R.array.user_points_file_array);
        fcNotes = new FileController[noteArray.length];
        fcPoints = new FileController[pointArray.length];
        for (int i = 0; i < noteArray.length; i++) {
            fcNotes[i] = new FileController(this, noteArray[i]);
            Utils.setLog(noteArray[i]);
        }
        for (int i = 0; i < pointArray.length; i++)
            fcPoints[i] = new FileController(this, pointArray[i]);
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
        mBtnAccompany.setOnClickListener(btn -> {
            addPoint("研究員履歷", 1, 2);
            MaterialButton btnClick = btnOnclick(btn);
            mBtnAccompany.setOnClickListener(null);
            mBtnIgnore.setOnClickListener(null);
            setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_sad);
            ValueAnimator anim = ValueAnimator.ofInt(btnClick.getMeasuredWidth(),
                    (int) Utils.convertDpToPixel(this, 145),
                    (int) Utils.convertDpToPixel(this, 145)
                    , btnClick.getMeasuredWidth());
            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                btnAccompanyParams.width = val;
                btnClick.setLayoutParams(btnAccompanyParams);
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
        mBtnIgnore.setOnClickListener(btn -> {
            btnOnclick(btn);
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
        dialogIndex = 0;
        dialogList.clear();
        dialogList.add(new Dialog(0, "你還好嗎?\n為什麼在哭?"));
        dialogList.add(new Dialog(1, "我的家人對我說了很過分的話..."));
        dialogList.add(new Dialog(1, "黑狗研究所的研究員告訴我\n" +
                "他是因為生病才這樣\n" +
                "不應該把他的話聽進心裡..."));
        dialogList.add(new Dialog(1, "但我還是很難過..."));
        resetDialogName();
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    setCtDialogBox(dialogList.get(dialogIndex), false);
                    dialogIndex += 1;
                } else {
                    dialogIndex = 0;
                    dialogList.clear();
                    ctDialogBox.setOnClickListener(null);
                    clearDialogBox(Color.WHITE);

                    MaterialButton mBtnInjure = createSelectBtn();
                    MaterialButton mBtnComprehend = createSelectBtn();
                    LinearLayout.LayoutParams injureParams = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.btn_height)
                    );
                    mBtnInjure.setLayoutParams(injureParams);
                    mBtnInjure.setText("就算是生病也不能說出傷人的話啊!");
                    mBtnInjure.setOnClickListener(btn -> {
                        btnOnclick(btn);
                        mBtnInjure.setOnClickListener(null);
                        mBtnComprehend.setOnClickListener(null);
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
                    mBtnComprehend.setOnClickListener(btn -> {
                        btnOnclick(btn);
                        mBtnInjure.setOnClickListener(null);
                        mBtnComprehend.setOnClickListener(null);
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
        ctDialogBox.performClick();
    }

    private void injureDialogBox() {
        dialogList.add(new Dialog(1, "雖然是這樣沒錯\n" +
                "不過你可能不了解憂鬱症吧..."));
        dialogList.add(new Dialog(1, "患者就是會無法控制自己的情緒..."));
        dialogList.add(new Dialog(0, "我的確是不了解\n" +
                "所以才想去黑狗研究所看看。"));
        dialogList.add(new Dialog(1, "你想去黑狗研究所?\n" +
                "直走右轉就是了。"));
        dialogList.add(new Dialog(1, "謝謝你\n" +
                "停下來關心我。"));
        resetDialogName();
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    setCtDialogBox(dialogList.get(dialogIndex), false);
                    dialogIndex += 1;
                } else {
                    arriveLab();
                }
            }
        });
        ctDialogBox.performClick();
    }

    private void comprehendDialogBox() {
        noteFlag = false;
        dialogList.add(new Dialog(1, "沒想到你身邊也有人會這樣..."));
        dialogList.add(new Dialog(0, "對呀...我就是不知道該怎麼辦\n" +
                "才想去你剛提到的黑狗研究所看看。"));
        dialogList.add(new Dialog(1, "其實黑狗研究所的研究員有提醒我"));
        dialogList.add(new Dialog(1, "陪伴者也非常需要其他人的陪伴\n" +
                "這樣狀態不好的時候才有人能夠\n" +
                "抒發情緒或是協助自己"));
        dialogList.add(new Dialog(1, "但我朋友今天在忙沒辦法陪我。"));
        dialogList.add(new Dialog(0, "那我來當你的朋友吧。"));
        dialogList.add(new Dialog(0, "我們有相似的經驗\n" +
                "你需要我的時候我可以聽你說\n" +
                "我需要你的時候你也可以幫我。"));
        dialogList.add(new Dialog(1, "好啊，謝謝你!"));
        dialogList.add(new Dialog(1, "有你陪我聊聊天心情真的有比較好。"));
        dialogList.add(new Dialog(1, "對了! 如果你想去黑狗研究所\n" +
                "從這裡直走右轉就是了喔。"));
        resetDialogName();
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    if (dialogIndex == 4) {
                        if (noteFlag)
                            return;
                        noteFlag = true;
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
                        saveNote(() -> {
                                    addNote("陪伴者也需要其他人的陪伴", "陪伴者也非常需要其他人的陪伴，" +
                                            "這樣狀態不好的時候才有人能夠抒發情緒或是協助自己。", 2);
                                    addPoint("收藏知識", 1, 1);
                                    explainPopup.dismiss();
                                    Utils.showBackgroundAnimator(this, 0.7f, 1.0f);
                                },
                                () -> {
                                    setCtDialogBox(dialogList.get(dialogIndex), false);
                                    dialogIndex += 1;
                                    ctDialogBox.performClick();
                                });

                        Utils.showBackgroundAnimator(this, 1.0f, 0.7f);
                    } else {
                        if (dialogIndex == 8)
                            setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_happy);
                        setCtDialogBox(dialogList.get(dialogIndex), false);
                        dialogIndex += 1;
                    }
                } else {
                    arriveLab();
                }
            }
        });
        ctDialogBox.performClick();
    }

    private void arriveLab() {
        clearDialogBox(Color.TRANSPARENT);
        mBgDialog.animate().alpha(0).setDuration(500);
        ctDialogBox.setAlpha(0);
        mBgDialog.postDelayed(() -> {
            VideoView mVvWhiteDog = new VideoView(this);
            ConstraintLayout.LayoutParams videoParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mVvWhiteDog.setLayoutParams(videoParams);
            ctMain.addView(mVvWhiteDog);
            mVvWhiteDog.setOnPreparedListener(mp -> mVvWhiteDog.start());
            mVvWhiteDog.setOnCompletionListener(mp -> {
                ctMain.removeView(mVvWhiteDog);
                clearDialogBox(Color.TRANSPARENT);
                firstLabDialog();
            });
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            Uri animationUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_white_dog);
            mVvWhiteDog.setVideoURI(animationUri);
        }, 500);
    }

    private void firstLabDialog() {
        setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_lab);
        setImageDrawableFit(mImgDialogChar, R.drawable.char_tsai6);
        ValueAnimator alphaAnim = ValueAnimator.ofFloat(0, 1);
        alphaAnim.setDuration(500);
        alphaAnim.addUpdateListener(valueAnimator -> {
            Float alpha = (Float) valueAnimator.getAnimatedValue();
            mBgDialog.setAlpha(alpha);
            ctDialogBox.setAlpha(alpha);
        });
        alphaAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImgDialogChar.animate().alpha(1).setDuration(1000);
                ctDialogBox.setBackgroundColor(getResources().getColor(R.color.grey1));
                dialogIndex = 0;
                dialogList.clear();
                dialogList.add(new Dialog(2, "欸?你不是上次那位\n不知道該怎麼幫助朋友的人嗎?"));
                dialogList.add(new Dialog(2, "你想清楚要加入黑狗研究所了嗎?"));
                ctDialogBox.setOnClickListener(v -> {
                    if (mTypeText.isRunning())
                        mTypeText.endType();
                    else {
                        if (dialogIndex < dialogList.size()) {
                            setCtDialogBox(dialogList.get(dialogIndex), false);
                            dialogIndex += 1;
                        } else {
                            clearDialogBox(Color.WHITE);
                            MaterialButton mBtnOk = createSelectBtn();
                            MaterialButton mBtnConsider = createSelectBtn();
                            LinearLayout.LayoutParams okParams = new LinearLayout.LayoutParams(
                                    getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                                    getResources().getDimensionPixelOffset(R.dimen.btn_height)
                            );
                            LinearLayout.LayoutParams considerParams = new LinearLayout.LayoutParams(
                                    getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                                    getResources().getDimensionPixelOffset(R.dimen.btn_height)
                            );
                            considerParams.topMargin = (int) Utils.convertDpToPixel(DialogActivity.this, 15);
                            mBtnOk.setText("我想清楚了");
                            mBtnConsider.setText("我再考慮一下");
                            View.OnClickListener onclick = btn -> {
                                btnOnclick(btn);
                                mBtnOk.setOnClickListener(null);
                                mBtnConsider.setOnClickListener(null);
                                mLlDialogBox.postDelayed(() -> {
                                    mLlDialogBox.removeAllViews();
                                    secondLabDialog();
                                }, 500);
                            };
                            mBtnOk.setOnClickListener(onclick);
                            mBtnConsider.setOnClickListener(onclick);
                            mLlDialogBox.addView(mBtnOk, 0, okParams);
                            mLlDialogBox.addView(mBtnConsider, 1, considerParams);
                        }
                    }
                });
                ctDialogBox.postDelayed(() -> {
                    resetDialogName();
                    ctDialogBox.performClick();
                }, 500);
            }
        });
        alphaAnim.start();
    }

    private void secondLabDialog() {
        dialogIndex = 0;
        dialogList.clear();
        dialogList.add(new Dialog(2, "黑狗研究所歡迎所有想要\n" +
                "或是需要了解憂鬱症的人加入"));
        dialogList.add(new Dialog(2, "但是和憂鬱的情緒長時間相處\n" +
                "是很耗費精力的"));
        dialogList.add(new Dialog(2, "你要確定無論如何都能夠\n" +
                "「為自己留一點空間」\n" +
                "才可以加入喔!"));
        resetDialogName();
        setImageDrawableFit(mImgDialogChar, R.drawable.char_tsai3);
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (dialogIndex < dialogList.size()) {
                    setCtDialogBox(dialogList.get(dialogIndex), false);
                    dialogIndex += 1;
                } else {
                    clearDialogBox(Color.WHITE);
                    MaterialButton mBtnSure = createSelectBtn();
                    MaterialButton mBtnNo = createSelectBtn();
                    LinearLayout.LayoutParams sureParams = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.btn_height)
                    );
                    LinearLayout.LayoutParams NoParams = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.btn_height)
                    );
                    NoParams.topMargin = (int) Utils.convertDpToPixel(DialogActivity.this, 15);
                    mBtnSure.setText("我確定要加入黑狗研究所");
                    mBtnNo.setText("我再考慮一下");
                    mBtnSure.setOnClickListener(btn -> {
                        btnOnclick(btn);
                        mBtnSure.setOnClickListener(null);
                        mBtnNo.setOnClickListener(null);
                        mLlDialogBox.postDelayed(() -> {
                            mLlDialogBox.removeAllViews();
                            Intent intent = new Intent(DialogActivity.this, FirstLoginActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_LOGIN);
                        }, 500);
                    });
                    exit = false;
                    mBtnNo.setOnClickListener(btn -> {
                        if (!exit)
                            showToast("你考慮清楚了嗎");
                        else
                            ActivityUtils.getInstance().exitSystem();
                        exit = !exit;
                    });
                    mLlDialogBox.addView(mBtnSure, 0, sureParams);
                    mLlDialogBox.addView(mBtnNo, 1, NoParams);
                }
            }
        });
        ctDialogBox.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideNavigationBar(this);
        if (requestCode == REQUEST_CODE_LOGIN) {
            try {
                //set basic theme color
                String[] splitFileData = fcDailyMood.readFileSplit();
                for (String lineData : splitFileData) {
                    String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                    if (lineDataArray[0].equals(fcLoginDate.readFile())) {
                        Utils.setLog("Mood Type = " + lineDataArray[1]);
                        switch (Integer.parseInt(lineDataArray[1])) {
                            case 3:
                            case 4:
                                themeColor = R.style.Theme_BlackDogLab_Blue;
                                break;
                            case 2:
                                themeColor = R.style.Theme_BlackDogLab_Green;
                                break;
                            case 0:
                            case 1:
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
            this.setTheme(themeColor);
            introduceLab();
            Utils.setLog("back");
        } else if (requestCode == REQUEST_CODE_FEED_DOG) {

        }
    }

    private void introduceLab() {
        noteFlag = false;
        int[] bgDisplay = new int[]{Utils.getAttrID(DialogActivity.this, R.attr.bg_main_lab, Utils.RESOURCE_ID),
                Utils.getAttrID(DialogActivity.this, R.attr.bg_main_street, Utils.RESOURCE_ID),
                Utils.getAttrID(DialogActivity.this, R.attr.bg_main_park, Utils.RESOURCE_ID),
                R.drawable.bg_display_dog, R.drawable.bg_display_calendar,
                R.drawable.bg_display_note, R.drawable.bg_display_user};
        clearDialogBox(Color.TRANSPARENT);
        dialogIndex = 0;
        dialogList.clear();
        dialogList.add(new Dialog(2, "你好，我是菜菜子\n" +
                "剛從實習研究員晉升成研究員", 6));
        dialogList.add(new Dialog(3, "這位是艾博士\n是黑狗研究所的創立人", 7));
        dialogList.add(new Dialog(4, "還有一位裴婆婆是我們研究所裡的志工\n" +
                "你未來會有機會見到她的", 2));
        dialogList.add(new Dialog(2, "歡迎你加入我們!", 6));
        dialogList.add(new Dialog(2, "走吧!\n" +
                "我帶你去看看研究室的環境", 6));
        dialogList.add(new Dialog(2, "這裡是我們的研究室\n你可以在這裡找到我跟艾博士\n" +
                "有問題可以來找我們"));
        dialogList.add(new Dialog(2, " 這家早餐店很好吃喔!\n" +
                "下次可以買買看"));
        dialogList.add(new Dialog(2, "累的時候就來公園坐坐\n" +
                "是放鬆心情的好地方"));
        dialogList.add(new Dialog(2, "這邊是黑狗研究所照顧的黑狗們你\n" +
                "很快就會認識他們了"));
        dialogList.add(new Dialog(2, "還記得剛才填寫的心情紀錄嗎?\n" +
                "在這裡我們會記錄你每天的情緒變化\n" +
                "長時間處於負面情緒的話\n" +
                "我們會提醒妳要先照顧自己喔!"));
        dialogList.add(new Dialog(2, "生活中其實充滿值得我們學習的知識喔~\n" +
                "如果在黑狗研究所有獲得\n" +
                "什麼有用的資訊可以點擊收藏\n" +
                "想查看就來翻閱研究筆記吧!"));
        dialogList.add(new Dialog(2, "個人設置可以看到每天的行為紀錄\n" +
                "你可以看看哪些行為會幫助你累積積分喔!"));
        dialogList.add(new Dialog(0, "謝謝\n" +
                "我加入黑狗研究所是想了解憂鬱症到底是什麼\n" +
                "為什麼我朋友身邊會多一條黑狗!"));
        dialogList.add(new Dialog(2, "黑狗研究所成立的意義\n" +
                "就是藉由研究黑狗來學會如何和情緒相處", 4));
        dialogList.add(new Dialog(0, "所以有憂鬱的情緒就是憂鬱症嗎?\n" +
                "那我有時候也會有憂鬱的情緒阿"));
        dialogList.add(new Dialog(3, "有時候有憂鬱的情緒是正常的\n" +
                "我們可以允許它的存在\n" +
                "但是", 3));
        dialogList.add(new Dialog(3, "憂鬱症是一種心理疾病\n" +
                "患者會「長時間」處於負面情緒當中\n" +
                "人際關係、生理功能、認知思考\n" +
                "及行為等最終都會受到影響", 3));
        dialogList.add(new Dialog(0, "為什麼我朋友會得這個病呢?\n" +
                "他明明之前都還很正常啊"));
        dialogList.add(new Dialog(3, "我們很難去判斷為什麼患者會生病\n" +
                "導致他生病的原因有很多\n" +
                "包括生理、心理、遺傳和社會等因素", 4));
        dialogList.add(new Dialog(0, "原來憂鬱症沒有我想的那麼簡單啊"));
        dialogList.add(new Dialog(3, "如果你想要更快的了解憂鬱症\n" +
                "我交給你一個任務", 9));
        dialogList.add(new Dialog(3, "以後研究所內的黑狗糧食就交由你負責\n" +
                "你也知道它們不是那麼好照顧\n" +
                "記得別被它們影響了。", 9));
        resetDialogName();
        ctDialogBox.setOnClickListener(v -> {
            if (mTypeText.isRunning())
                mTypeText.endType();
            else {
                if (noteFlag)
                    return;
                if (dialogIndex < dialogList.size()) {
                    if (dialogIndex >= 5 && dialogIndex <= 11) {
                        setImageDrawableFit(mBgDialog, bgDisplay[dialogIndex - 5]);
                        ctDialogBox.animate().alpha(0.7f);
                    } else if (dialogIndex == 12) {
                        ctDialogBox.animate().alpha(1);
                        setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_lab);
                    } else if (dialogIndex == 16 || dialogIndex == 18) {
                        saveNote(() -> {
                            noteFlag = true;
                            mTypeText.endType();
                            if (dialogIndex == 17) {
                                addNote("憂鬱症是一種心理疾病",
                                        "憂鬱症是一種心理疾病，" +
                                                "患者會「長時間」處於負面情緒當中，" +
                                                "人際關係、生理功能、認知思考及行為等最終都會受到影響。", 0);
                            } else if (dialogIndex == 19) {
                                addNote("罹患憂鬱症的原因",
                                        "我們很難去判斷為什麼患者會生病，" +
                                                "導致他生病的原因有很多，其中包含:\n" +
                                                "．生理因素\n" +
                                                "．心理因素\n" +
                                                "．遺傳因素\n" +
                                                "．社會因素", 0);
                            }
                            addPoint("收藏知識", 1, 1);
                        }, () -> {
                            noteFlag = false;
                            ctDialogBox.performClick();
                        });
                    } else if (dialogIndex == 17 || dialogIndex == 19) {
                        mImgDialogNote.setOnClickListener(null);
                        mImgDialogNote.setImageResource(0);
                        mImgDialogNote.clearColorFilter();
                    }
                    setCtDialogBox(dialogList.get(dialogIndex), true);
                    dialogIndex += 1;
                } else {
                    FileController fcDogEvent = new FileController(DialogActivity.this, getResources().getString(R.string.dog_event));
                    try {
                        fcDogEvent.write("yellow$sit$\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(DialogActivity.this, MainPage.class);
                    startActivityForResult(intent, REQUEST_CODE_FEED_DOG);
                }
            }
        });
        ctDialogBox.performClick();
    }

    private void setCtDialogBox(Dialog dialog, boolean setChar) {
        ctDialogBox.setBackgroundColor(Utils.getAttrID(DialogActivity.this, dialog.getBgColor(), Utils.DATA));
        mTvDialogName.setText(dialog.getName());
        mTypeText.showText(mTvDialogText, dialog.getText(), 100);
        mTvDialogText.setTextColor(Utils.getAttrID(DialogActivity.this, dialog.getTextColor(), Utils.DATA));
        mTvDialogName.setTextColor(Utils.getAttrID(DialogActivity.this, dialog.getNameTextColor(), Utils.DATA));
        mTvDialogName.setBackgroundResource(dialog.getNameBg());
        setImageDrawableFit(mImgDialogNext, Utils.getAttrID(DialogActivity.this, dialog.getNextDrawable(), Utils.RESOURCE_ID));
        int charDrawable = dialog.getCharDrawable();
        if (setChar) {
            if (charDrawable == -1) {
                mImgDialogChar.setImageResource(0);
            } else {
                setImageDrawableFit(mImgDialogChar, charDrawable);
            }
        }
    }

    private void resetDialogName() {
        mTvDialogName.setElevation(Utils.convertDpToPixel(DialogActivity.this, 5));
    }

    private void clearDialogBox(int dialogBgColor) {
        ctDialogBox.setBackgroundColor(dialogBgColor);
        ctDialogBox.setOnClickListener(null);
        mTvDialogText.setText(null);
        mTvDialogName.setText(null);
        mTvDialogName.setBackgroundResource(0);
        mTvDialogName.setElevation(0);
        mImgDialogNext.setImageResource(0);
    }

    private MaterialButton createSelectBtn() {
        MaterialButton btn = new MaterialButton(this);
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this,
                Utils.getAttrID(this, R.attr.dialog_btn_color, Utils.RESOURCE_ID)));
        btn.setRippleColor(ContextCompat.getColorStateList(this,
                Utils.getAttrID(this, R.attr.dialog_btn_click_color, Utils.RESOURCE_ID)));
        btn.setLetterSpacing(0.15f);
        btn.setLineSpacing(10, 1);
        btn.setTextColor(Utils.getAttrID(this, R.attr.dialog_btn_text_color, Utils.DATA));
        btn.setCornerRadius((int) Utils.convertDpToPixel(this, 10));
        btn.setTextSize(12);
        btn.setElevation(Utils.convertDpToPixel(DialogActivity.this, 5));
        return btn;
    }

    private MaterialButton btnOnclick(View view) {
        if (view instanceof MaterialButton) {
            MaterialButton btn = (MaterialButton) view;
            btn.setTextColor(Utils.getAttrID(this, R.attr.dialog_btn_text_click_color, Utils.DATA));
            btn.setBackgroundTintList(ContextCompat.getColorStateList(this,
                    Utils.getAttrID(this, R.attr.dialog_btn_click_color, Utils.RESOURCE_ID)));
            btn.setRippleColor(ContextCompat.getColorStateList(this,
                    Utils.getAttrID(this, R.attr.dialog_btn_color, Utils.RESOURCE_ID)));
            return btn;
        } else {
            Utils.setLog("set material button fail");
            return null;
        }
    }

    private void saveNote(Runnable onNoteClick, Runnable onAnimEnd) {
        setImageDrawableFit(mImgDialogNote, R.drawable.icon_note);
        mImgDialogNote.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mImgDialogNote.setOnClickListener(view -> {
            mHandler.post(onNoteClick);
            view.setOnClickListener(null);
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
                                mHandler.post(onAnimEnd);
                                ctDialogBox.removeView(mImgEnergy);
                                mImgDialogNote.setImageResource(0);
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
    }

    private void addNote(String noteTitle, String noteText, int noteIndex) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(noteTitle)
                .append(FileController.getWordSplitChar())
                .append(noteText)
                .append(FileController.getLineSplitChar());
        Utils.setLog(stringBuffer.toString());
        if (fcNotes[noteIndex].fileExist()) {
            try {
                if (fcNotes[noteIndex].readFile().isEmpty()){
                    Utils.setLog("File Is Empty");
                    fcNotes[noteIndex].write(stringBuffer.toString());
                }else{
                    HashMap<String, String> noteSet = new HashMap<>();
                    for (String noteLine : fcNotes[noteIndex].readFileSplit()) {
                        String[] noteArray = noteLine.split(FileController.getWordSplitRegex());
                        noteSet.put(noteArray[0], noteArray[1]);
                    }
                    if (!noteSet.containsKey(noteTitle))
                        fcNotes[noteIndex].append(stringBuffer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        } else {
            try {
                Utils.setLog("create note");
                fcNotes[noteIndex].write(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        }
    }

    private void addPoint(String pointTitle, int pointValue, int pointType) {
        StringBuffer stringBuffer = new StringBuffer();
        if (fcPoints[pointType].fileExist()) {
            try {
                if (fcPoints[pointType].readFile().isEmpty()) {
                    Utils.setLog("File is empty");
                    stringBuffer.append(pointTitle)
                            .append(FileController.getWordSplitChar())
                            .append(pointValue)
                            .append(FileController.getLineSplitChar());
                    fcPoints[pointType].write(stringBuffer.toString());
                } else {
                    HashMap<String, Integer> pointSet = new HashMap<>();

                    for (String pointLine : fcPoints[pointType].readFileSplit()) {
                        String[] pointArray = pointLine.split(FileController.getWordSplitRegex());
                        pointSet.put(pointArray[0], Integer.valueOf(pointArray[1]));
                    }
                    if (pointSet.containsKey(pointTitle)) {
                        pointValue += pointSet.get(pointTitle);
                        if (pointValue == 0)
                            pointSet.remove(pointTitle);
                        else
                            pointSet.put(pointTitle, pointValue);
                        for (Map.Entry<String, Integer> entry : pointSet.entrySet()) {
                            stringBuffer.append(entry.getKey())
                                    .append(FileController.getWordSplitChar())
                                    .append(entry.getValue())
                                    .append(FileController.getLineSplitChar());
                        }
                        fcPoints[pointType].write(stringBuffer.toString());
                    } else {
                        stringBuffer.append(pointTitle)
                                .append(FileController.getWordSplitChar())
                                .append(pointValue)
                                .append(FileController.getLineSplitChar());
                        fcPoints[pointType].append(stringBuffer.toString());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        } else {
            try {
                stringBuffer.append(pointTitle)
                        .append(FileController.getWordSplitChar())
                        .append(pointValue)
                        .append(FileController.getLineSplitChar());
                fcPoints[pointType].write(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        }
    }

}