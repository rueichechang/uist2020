package com.example.uist2020v2;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;

public class QADialog extends Dialog {
    RadioGroup radioGroup;
    TextView questionTextView;
    JSONObject qaJson;
    ColorStateList colorStateList = new ColorStateList(
            new int[][]{

                    new int[]{-android.R.attr.state_enabled}, //disabled
                    new int[]{android.R.attr.state_enabled} //enabled
            },
            new int[] {

                    Color.BLACK //disabled
                    ,Color.BLUE //enabled

            }
    );

    public QADialog(@NonNull Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.qa_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        radioGroup = (RadioGroup) findViewById(R.id.choiceRadioGroup);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        Button leaveButton = findViewById(R.id.leaveButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        qaJson = loadQAJSONFromAsset(context);
        loadNextQAIntoDialog(qaJson, 0);
    }

    public void loadNextQAIntoDialog(JSONObject json, int questionIndex){
        try {
            JSONArray questions  = json.getJSONArray("questions");
            final JSONObject question = questions.getJSONObject(questionIndex);
            questionTextView.setText(question.getString("question"));
            String questionType = question.getString("question_type");
            if(questionType.equals("single_select")){
                JSONArray options = question.getJSONArray("options");
                if(options != null) {
                    radioGroup.removeAllViews();
                    for (int i = 0; i < options.length(); i++) {
                        RadioButton rdbtn = new RadioButton(this.getContext());
                        rdbtn.setButtonTintList(colorStateList);
                        rdbtn.setTextSize(18);
                        rdbtn.setTextColor(Color.BLACK);
                        rdbtn.setId(View.generateViewId());
                        rdbtn.setText(options.getString(i));
                        final int finalI = i;
                        rdbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadNextQAIntoDialog(question, finalI);
                            }
                        });
                        radioGroup.addView(rdbtn);
                    }
                }
            } else if (questionType.equals("answer")) {
                radioGroup.removeAllViews();
                questionTextView.setText(question.getString("question"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject loadQAJSONFromAsset(Context context) {
        String json = null;
        JSONObject obj = null;
        try {
            InputStream is = context.getAssets().open("qa.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            obj = new JSONObject(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;

    }
}
