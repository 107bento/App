package com.example.aishalien.bento;

/**
 * Created by yuting on 2017/11/24.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义组件：购买数量，带减少增加按钮
 */
public class AmountView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private AttributeSet mAttrs;
    private View rootView;

    private int minValue = 1; //最小數量
    private int maxValue = 99; //最大數量
    private int currentValue; //現在的數量

    private OnAmountChangeListener mListener;

    private TextView tvAmount;
    private Button btnDecrease;
    private Button btnIncrease;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        this.mAttrs = attrs;
        initView();
    }

    private void initView() {

        rootView = LayoutInflater.from(mContext).inflate(R.layout.view_amount, this);
        tvAmount = (TextView) rootView.findViewById(R.id.tvAmount);
        btnDecrease = (Button) rootView.findViewById(R.id.btnDecrease);
        btnIncrease = (Button) rootView.findViewById(R.id.btnIncrease);

        // +-按鈕監聽
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);

        TypedArray typedArray = getContext().obtainStyledAttributes(mAttrs, R.styleable.AmountView);
        int btnWidth = typedArray.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = typedArray.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = typedArray.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = typedArray.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        tvAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            tvAmount.setTextSize(tvTextSize);
        }

        // 最小值
        minValue = typedArray.getInteger(R.styleable.AmountView_minValue, 1);
        if (minValue < 1) {
            minValue = 1;
        }
        // 最大值
        maxValue = typedArray.getInteger(R.styleable.AmountView_maxValue, 99);
        if (maxValue < minValue) {
            maxValue = minValue;
        }

        currentValue = typedArray.getInteger(R.styleable.AmountView_currentValue, minValue);
        typedArray.recycle();


        tvAmount.setText(String.valueOf(currentValue));

//        addView(rootView);
    }

    public void setListener(Object object) {
        this.mListener = (AmountView.OnAmountChangeListener) object;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) { //設置最小值
        if (minValue > maxValue) {
            minValue = maxValue;
        }
        this.minValue = minValue;
        setCurrentValue(getCurrentValue());
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) { // 設置最大值
        if (maxValue < minValue) {
            maxValue = minValue;
        }
        this.maxValue = maxValue;
        setCurrentValue(getCurrentValue());
    }

    public int getCurrentValue() {
        return Integer.parseInt(tvAmount.getText().toString());
    }

    public void setCurrentValue(int currentValue) { // 重新設置現在的值
        this.currentValue = currentValue;
        tvAmount.setText(String.valueOf(currentValue));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) { // 減少
            if (currentValue > getMinValue()) {
                currentValue--;
                tvAmount.setText(currentValue + "");
            }
        } else if (i == R.id.btnIncrease) {
            if (currentValue < getMaxValue()) { // 增加
                currentValue++;
                tvAmount.setText(currentValue + "");
            }
        }

        if (mListener != null) {
            mListener.onAmountChange(currentValue);
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(int amount);
    }
}