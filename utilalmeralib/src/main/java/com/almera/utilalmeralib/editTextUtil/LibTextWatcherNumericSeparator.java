package com.almera.utilalmeralib.editTextUtil;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LibTextWatcherNumericSeparator implements TextWatcher {

    public Locale locale = new Locale("es", "CO");
    public int decimalDigits = 2;
    int seleccion = 0;
    int previusSelection = 0;
    int adicion;
    private char GROUPING_SEPARATOR = ' ';
    private char DECIMAL_SEPARATOR = '.';
    private String LEADING_ZERO_FILTER_REGEX;
    private String mDefaultText = null;
    private String mPreviousText = "";
    private String mNumberFilterRegex;
    private Single<Double> observableValueEditTextNumeric;
    private EditText editText;
    SingleObserver<Double> observerValue;
    private boolean validateLock = false;

    /**
     * @param editText         editText
     * @param decimalSeparator
     * @param grupingSeparator thousands separator
     * @param decimalDigits    limit of decimal digits
     * @param observerValue    observer of the value (Double) of the editText
     */
    public LibTextWatcherNumericSeparator(EditText editText, @Nullable char decimalSeparator, @Nullable char grupingSeparator, @Nullable int decimalDigits, String typeInput, SingleObserver<Double> observerValue) {
        this.editText = editText;
        this.decimalDigits = decimalDigits;
        this.GROUPING_SEPARATOR = grupingSeparator;
        this.DECIMAL_SEPARATOR = decimalSeparator;
        this.observerValue = observerValue;
        this.editText.setKeyListener(DigitsKeyListener.getInstance(typeInput));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EditTextSelection", "onClick: " + editText.getSelectionEnd());
            }
        });
        reload();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        previusSelection = seleccion;
        if (count > before) {
            seleccion = (start + count);
        } else {
            seleccion = (start - before) + 1;
        }
        Log.d("seleccion", "beforeTextChanged: " + seleccion);

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();


        if (validateLock) {
            return;
        }


        // If user presses GROUPING_SEPARATOR, convert it to DECIMAL_SEPARATOR
        if (text.endsWith(GROUPING_SEPARATOR + "") || text.endsWith(".") || text.endsWith(",")) {
            text = text.substring(0, text.length() - 1) + DECIMAL_SEPARATOR;
        }

        // Limit decimal digits
        if (decimalDigitLimitReached(text)) {
            validateLock = true;
            editText.setText(mPreviousText); // cancel change and revert to previous input
            editText.setSelection(mPreviousText.length());
            validateLock = false;
            return;
        }

        // valid decimal number should not have thousand separators after a decimal separators
        if (hasGroupingSeperatorAfterDecimalSeperator(text)) {
            validateLock = true;
            editText.setText(mPreviousText); // cancel change and revert to previous input
            editText.setSelection(mPreviousText.length());
            validateLock = false;
            return;
        }


        // valid decimal number should not have more than 2 decimal separators
        if (countMatches(text, String.valueOf(DECIMAL_SEPARATOR)) > 1) {
            validateLock = true;
            editText.setText(mPreviousText); // cancel change and revert to previous input
            editText.setSelection(mPreviousText.length());
            validateLock = false;
            return;
        }

        // validate minus sign
        if (text.indexOf("-") > 0 || countMatches(text, String.valueOf("-")) > 1) {
            validateLock = true;
            editText.setText(mPreviousText); // cancel change and revert to previous input
            editText.setSelection(mPreviousText.length());
            validateLock = false;
            return;
        }


        if (text.length() == 0) {
            handleNumericValueCleared();
            return;
        }


        // If only decimal separator is inputted, add a zero to the left of it
      /*  if (text.indexOf(String.valueOf(DECIMAL_SEPARATOR)) == 0) {
            text = '0' + text;
        }*/

        setTextInternal(format(text));
        try {
            editText.setSelection(seleccion + adicion);
        } catch (Exception e) {
            editText.setSelection(editText.getText().length());
        }
        handleNumericValueChanged();
        notifiChangeValue();

    }


    private void notifiChangeValue() {
        observableValueEditTextNumeric.just(getNumericValue()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribeWith(observerValue);


    }


    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
        reload();
    }

    private void reload() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        //GROUPING_SEPARATOR = symbols.getGroupingSeparator();
        //DECIMAL_SEPARATOR = ',';
        LEADING_ZERO_FILTER_REGEX = "^-?0+(?!$)";
        mNumberFilterRegex = "[^-?\\d\\" + this.DECIMAL_SEPARATOR + "]";

    }

    //region Utils

    private void handleNumericValueCleared() {
        mPreviousText = "";

    }

    private void handleNumericValueChanged() {
        mPreviousText = editText.getText().toString();

    }


    public void setDefaultNumericValue(double defaultNumericValue, final String defaultNumericFormat) {
        mDefaultText = String.format(defaultNumericFormat, defaultNumericValue);
        setTextInternal(mDefaultText);
    }

    public void clear() {
        setTextInternal(mDefaultText != null ? mDefaultText : "");
        if (mDefaultText != null) {
            handleNumericValueChanged();
        }
    }

    public double getNumericValue() {
        String original = editText.getText().toString().replace(GROUPING_SEPARATOR + "", "").replace(DECIMAL_SEPARATOR + "", ".");
        try {
            return NumberFormat.getInstance().parse(original).doubleValue();
        } catch (ParseException e) {
            return Double.NaN;
        }
    }


    private String format(String original) {
        int lon1 = original.length();
        if (original.indexOf(String.valueOf(DECIMAL_SEPARATOR)) == 0) {
            original = '0' + original;
        }
        String[] parts = splitOriginalText(original);
        String number = parts[0].replaceAll(mNumberFilterRegex, "").replaceFirst(LEADING_ZERO_FILTER_REGEX, "");

        number = reverse(reverse(number).replaceAll("(.{3})", "$1" + GROUPING_SEPARATOR));
        number = removeStart(number, String.valueOf(GROUPING_SEPARATOR));

        if (parts.length > 1) {
            parts[1] = parts[1].replaceAll(mNumberFilterRegex, "");
            number += DECIMAL_SEPARATOR + parts[1];
        }

        if (number.indexOf("-" + GROUPING_SEPARATOR) == 0) {
            number = number.replaceFirst("-" + GROUPING_SEPARATOR, "-");
        }
        if (number.indexOf("-" + DECIMAL_SEPARATOR) == 0) {
            number = number.replaceFirst("-" + DECIMAL_SEPARATOR, "-0" + DECIMAL_SEPARATOR);
        }
        int concurrentseleccion = editText.getSelectionEnd();
        int lon2 = number.length();
        adicion = lon2 - lon1;
        return number;
    }

    private String[] splitOriginalText(String original) {
        //Dot is special character in regex, so we have to treat it specially.
        final String[] parts;
        if (DECIMAL_SEPARATOR == '.') {
            parts = original.split("\\.", -1);
        } else {
            parts = original.split(DECIMAL_SEPARATOR + "", -1);
        }
        return parts;
    }

    boolean noIgnorar = true;

    private void setTextIgnorarListener(String text) {
        noIgnorar = true;
        editText.setText(text);
    }

    private void setTextNOIgnorarListener(String text) {
        noIgnorar = false;
        editText.setText(text);

    }

    private void setTextInternal(String text) {
        editText.removeTextChangedListener(this);
        editText.setText(text);
        editText.addTextChangedListener(this);
    }

    private String reverse(String original) {
        if (original == null || original.length() <= 1) {
            return original;
        }
        return TextUtils.getReverse(original, 0, original.length()).toString();
    }

    private String removeStart(String str, String remove) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    private int countMatches(String str, String sub) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int lastIndex = str.lastIndexOf(sub);
        if (lastIndex < 0) {
            return 0;
        } else {
            return 1 + countMatches(str.substring(0, lastIndex), sub);
        }
    }

    private boolean hasGroupingSeperatorAfterDecimalSeperator(String text) {
        //Return true if thousand seperator (.) comes after a decimal seperator. (,)

        if (text.contains(GROUPING_SEPARATOR + "") && text.contains(DECIMAL_SEPARATOR + "")) {
            int firstIndexOfDecimal = text.indexOf(DECIMAL_SEPARATOR);
            int lastIndexOfGrouping = text.lastIndexOf(GROUPING_SEPARATOR);

            if (firstIndexOfDecimal < lastIndexOfGrouping) {
                return true;
            }
        }

        return false;
    }

    private boolean decimalDigitLimitReached(String text) {
        //Return true if decimal digit limit is reached
        if (text.contains(DECIMAL_SEPARATOR + "")) {

            if (DECIMAL_SEPARATOR == '.') {
                //Dot is special character in regex, so we have to treat it specially.
                String[] parts = text.split("\\.");
                if (parts.length > 0) {
                    String lastPart = parts[parts.length - 1];

                    if (lastPart.length() == decimalDigits + 1) {
                        return true;
                    }
                }
            } else {
                //If decimal seperator is not a dot, we can safely split.
                String[] parts = text.split(DECIMAL_SEPARATOR + "");
                if (parts.length > 1) {
                    String lastPart = parts[parts.length - 1];

                    if (lastPart.length() == decimalDigits + 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    //endregion
}

