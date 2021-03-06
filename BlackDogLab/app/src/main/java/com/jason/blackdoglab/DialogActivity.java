package com.jason.blackdoglab;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.view.animation.LinearInterpolator;
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
        int titleMarginBottom = (int) Utils.convertDpToPixel(this, 10);
        TextView mTvTitle = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.bottomMargin = titleMarginBottom;
        mTvTitle.setLayoutParams(titleParams);
        mTvTitle.setTextColor(getResources().getColor(R.color.grey1));
        mTvTitle.setTextSize(12);
        mTvTitle.setText("??????????????????????\n??????????????????????");
        mTvTitle.setLineSpacing(10, 1);
        mTvTitle.setLetterSpacing(0.2f);
        mTvTitle.setGravity(Gravity.CENTER);

        createExpSelection("?????????", "?????????",
                () -> {
                    setImageDrawableFit(mBgDialog, R.drawable.bg_dialog_sad);
                }, () -> {
                    mLlDialogBox.removeAllViews();
                    secondSelectBox();
                }, () -> {
                    mLlDialogBox.removeAllViews();
                    arriveLab();
                },
                "???????????????", 1, true);
        mLlDialogBox.addView(mTvTitle, 0);
    }

    private void secondSelectBox() {
        dialogIndex = 0;
        dialogList.clear();
        dialogList.add(new Dialog(0, "?????????????\n????????????????"));
        dialogList.add(new Dialog(1, "???????????????????????????????????????..."));
        dialogList.add(new Dialog(1, "????????????????????????????????????\n" +
                "???????????????????????????\n" +
                "?????????????????????????????????..."));
        dialogList.add(new Dialog(1, "?????????????????????..."));
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
                    mBtnInjure.setText("?????????????????????????????????????????????!");
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
                    mBtnComprehend.setText("???????????????????????????\n?????????????????????????????????????????????");
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
        dialogList.add(new Dialog(1, "?????????????????????\n" +
                "????????????????????????????????????..."));
        dialogList.add(new Dialog(1, "??????????????????????????????????????????..."));
        dialogList.add(new Dialog(0, "?????????????????????\n" +
                "???????????????????????????????????????"));
        dialogList.add(new Dialog(1, "?????????????????????????\n" +
                "????????????????????????"));
        dialogList.add(new Dialog(1, "?????????\n" +
                "?????????????????????"));
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
        dialogList.add(new Dialog(1, "????????????????????????????????????..."));
        dialogList.add(new Dialog(0, "??????...??????????????????????????????\n" +
                "????????????????????????????????????????????????"));
        dialogList.add(new Dialog(1, "?????????????????????????????????????????????"));
        dialogList.add(new Dialog(1, "??????????????????????????????????????????\n" +
                "??????????????????????????????????????????\n" +
                "??????????????????????????????"));
        dialogList.add(new Dialog(1, "??????????????????????????????????????????"));
        dialogList.add(new Dialog(0, "??????????????????????????????"));
        dialogList.add(new Dialog(0, "????????????????????????\n" +
                "???????????????????????????????????????\n" +
                "??????????????????????????????????????????"));
        dialogList.add(new Dialog(1, "??????????????????!"));
        dialogList.add(new Dialog(1, "????????????????????????????????????????????????"));
        dialogList.add(new Dialog(1, "??????! ??????????????????????????????\n" +
                "????????????????????????????????????"));
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
                                    addNote("????????????????????????????????????", "?????????????????????????????????????????????" +
                                            "???????????????????????????????????????????????????????????????????????????", 2);
                                    addPoint("????????????", 1, 1);
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
                dialogList.add(new Dialog(2, "?????????????????????????\n????????????????????????????????????????"));
                dialogList.add(new Dialog(2, "???????????????????????????????????????????"));
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
                            mBtnOk.setText("???????????????");
                            mBtnConsider.setText("??????????????????");
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
        dialogList.add(new Dialog(2, "?????????????????????????????????\n" +
                "???????????????????????????????????????"));
        dialogList.add(new Dialog(2, "???????????????????????????????????????\n" +
                "?????????????????????"));
        dialogList.add(new Dialog(2, "?????????????????????????????????\n" +
                "??????????????????????????????\n" +
                "??????????????????!"));
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
                    mBtnSure.setText("?????????????????????????????????");
                    mBtnNo.setText("??????????????????");
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
                            showToast("?????????????????????");
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
        dialogList.add(new Dialog(2, "????????????????????????\n" +
                "???????????????????????????????????????", 6));
        dialogList.add(new Dialog(3, "??????????????????\n??????????????????????????????", 7));
        dialogList.add(new Dialog(4, "???????????????????????????????????????????????????\n" +
                "?????????????????????????????????", 2));
        dialogList.add(new Dialog(2, "?????????????????????!", 6));
        dialogList.add(new Dialog(2, "??????!\n" +
                "????????????????????????????????????", 6));
        dialogList.add(new Dialog(2, "???????????????????????????\n???????????????????????????????????????\n" +
                "???????????????????????????"));
        dialogList.add(new Dialog(2, " ???????????????????????????!\n" +
                "?????????????????????"));
        dialogList.add(new Dialog(2, "??????????????????????????????\n" +
                "???????????????????????????"));
        dialogList.add(new Dialog(2, "?????????????????????????????????????????????\n" +
                "???????????????????????????"));
        dialogList.add(new Dialog(2, "????????????????????????????????????????\n" +
                "????????????????????????????????????????????????\n" +
                "?????????????????????????????????\n" +
                "???????????????????????????????????????!"));
        dialogList.add(new Dialog(2, "???????????????????????????????????????????????????~\n" +
                "?????????????????????????????????\n" +
                "???????????????????????????????????????\n" +
                "????????????????????????????????????!"));
        dialogList.add(new Dialog(2, "?????????????????????????????????????????????\n" +
                "??????????????????????????????????????????????????????!"));
        dialogList.add(new Dialog(0, "??????\n" +
                "????????????????????????????????????????????????????????????\n" +
                "??????????????????????????????????????????!"));
        dialogList.add(new Dialog(2, "??????????????????????????????\n" +
                "??????????????????????????????????????????????????????", 4));
        dialogList.add(new Dialog(0, "???????????????????????????????????????????\n" +
                "??????????????????????????????????????????"));
        dialogList.add(new Dialog(3, "???????????????????????????????????????\n" +
                "??????????????????????????????\n" +
                "??????", 3));
        dialogList.add(new Dialog(3, "??????????????????????????????\n" +
                "????????????????????????????????????????????????\n" +
                "??????????????????????????????????????????\n" +
                "????????????????????????????????????", 3));
        dialogList.add(new Dialog(0, "?????????????????????????????????????\n" +
                "?????????????????????????????????"));
        dialogList.add(new Dialog(3, "?????????????????????????????????????????????\n" +
                "?????????????????????????????????\n" +
                "????????????????????????????????????????????????", 4));
        dialogList.add(new Dialog(0, "?????????????????????????????????????????????"));
        dialogList.add(new Dialog(3, "???????????????????????????????????????\n" +
                "????????????????????????", 9));
        dialogList.add(new Dialog(3, "???????????????????????????????????????????????????\n" +
                "???????????????????????????????????????\n" +
                "??????????????????????????????", 9));
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
                                addNote("??????????????????????????????",
                                        "?????????????????????????????????" +
                                                "???????????????????????????????????????????????????" +
                                                "?????????????????????????????????????????????????????????????????????????????????", 0);
                            } else if (dialogIndex == 19) {
                                addNote("????????????????????????",
                                        "????????????????????????????????????????????????" +
                                                "????????????????????????????????????????????????:\n" +
                                                "???????????????\n" +
                                                "???????????????\n" +
                                                "???????????????\n" +
                                                "???????????????", 0);
                            }
                            addPoint("????????????", 1, 1);
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

    private void createExpSelection(String expBtnText, String wrongBtnText,
                                    Runnable expBtnClick, Runnable expBtnAnimEnd, Runnable wrongBtnClick,
                                    String pointTitle, int pointValue, boolean isExpFirst) {
        //exp btn
        RelativeLayout rlExp = new RelativeLayout(this);
        LinearLayout.LayoutParams rlExpParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                getResources().getDimensionPixelOffset(R.dimen.btn_height)
        );
        rlExp.setLayoutParams(rlExpParams);
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

        //btn exp
        MaterialButton mBtnExp = createSelectBtn();
        MaterialButton mBtnWrong = createSelectBtn();
        RelativeLayout.LayoutParams btnExpParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        btnExpParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        mBtnExp.setLayoutParams(btnExpParams);
        mBtnExp.setText(expBtnText);
        mBtnExp.setTranslationZ(1);
        mBtnExp.setOnClickListener(btn -> {
            MaterialButton btnClick = btnOnclick(btn);
            btnClick.post(expBtnClick);
            addPoint(pointTitle, pointValue, 2);
            mBtnExp.setOnClickListener(null);
            mBtnWrong.setOnClickListener(null);

            ValueAnimator anim = ValueAnimator.ofInt(btnClick.getMeasuredWidth(),
                    (int) Utils.convertDpToPixel(this, 145),
                    (int) Utils.convertDpToPixel(this, 145)
                    , btnClick.getMeasuredWidth());
            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                btnExpParams.width = val;
                btnClick.setLayoutParams(btnExpParams);
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    btnClick.postDelayed(expBtnAnimEnd, 1000);
                }
            });
            anim.setDuration(3000);
            anim.start();

            Keyframe translationFrame1 = Keyframe.ofFloat(0.0f, mImgExp.getTranslationX());
            Keyframe translationFrame2 = Keyframe.ofFloat(0.8f, 0);
            Keyframe translationFrame3 = Keyframe.ofFloat(1.0f, 0);
            PropertyValuesHolder translationX = PropertyValuesHolder.ofKeyframe
                    ("translationX", translationFrame1, translationFrame2, translationFrame3);
            ObjectAnimator btnTransAnimator = ObjectAnimator.ofPropertyValuesHolder(mImgExp, translationX);
            btnTransAnimator.setRepeatCount(1);
            btnTransAnimator.setRepeatMode(ValueAnimator.REVERSE);
            btnTransAnimator.setInterpolator(new LinearInterpolator());
            btnTransAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    Utils.setLog("start");
                    Utils.setLog("isReverse : " + isReverse);
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    showToast("end");
                    Utils.setLog("end");
                    Utils.setLog("isReverse : " + isReverse);
                }
            });
            btnTransAnimator.setDuration(1500);
            btnTransAnimator.start();
        });
        rlExp.addView(mBtnExp);
        rlExp.addView(mImgExp);
        //------------------------------------relative---------------------------------------------
        //ignore
        LinearLayout.LayoutParams btnWrongParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.dialog_btn_width),
                getResources().getDimensionPixelOffset(R.dimen.btn_height)
        );
        mBtnWrong.setLayoutParams(btnWrongParams);
        mBtnWrong.setText(wrongBtnText);
        mBtnWrong.setOnClickListener(btn -> {
            MaterialButton btnClick = btnOnclick(btn);
            mBtnExp.setOnClickListener(null);
            mBtnWrong.setOnClickListener(null);
            btnClick.post(wrongBtnClick);
        });

        if (isExpFirst) {
            mLlDialogBox.addView(rlExp, 0);
            mLlDialogBox.addView(mBtnWrong, 1);
        } else {
            mLlDialogBox.addView(mBtnWrong, 0);
            mLlDialogBox.addView(rlExp, 1);
        }

        ctDialogBox.setBackgroundColor(Color.WHITE);
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