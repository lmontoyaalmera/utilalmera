package com.almera.utilalmeralib.viewUtil;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LibCampoDateUtil {

    public static void editTextFecha(final EditText mDisplayDate, final int resoursesStyle, final Context context, String formato, final Date minima, final Date maxima)  {
        final SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
        mDisplayDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    final Calendar cal = Calendar.getInstance();
                    if (!mDisplayDate.getText().equals("")) {
                        Date dateAux = null;
                        try {
                            dateAux = formatoDelTexto.parse(mDisplayDate.getText() + "");
                            cal.setTime(dateAux);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            resoursesStyle,
                            // android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    month += 1;
                                    String dateSelecionada = String.format(year + "-" + month + "-" + day);

                                    Date fechaSeleccionada = null;
                                    try {
                                        fechaSeleccionada = formatoDelTexto.parse(dateSelecionada);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    mDisplayDate.setText(formatoDelTexto.format(fechaSeleccionada));
                                }
                            },
                            year, month, day);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            mDisplayDate.setText("");
                        /*if (getCampo().getValidacion().equals("R")) {
                            getErrorView().setError("Requerido");
                            getErrorView().requestFocus();
                        }*/
                        }
                    });
                    if (minima != null) {
                        dialog.getDatePicker().setMinDate(minima.getTime());

                    }
                    if (maxima != null) {
                        dialog.getDatePicker().setMaxDate(maxima.getTime());
                    }
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                }
            }
        });
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = 0;
                int month = 0;
                int day = 0;
                final Calendar cal = Calendar.getInstance();
                if (!mDisplayDate.getText().equals("")) {
                    Date dateAux = null;
                    try {
                        dateAux = formatoDelTexto.parse(mDisplayDate.getText() + "");
                        cal.setTime(dateAux);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                       context,
                        resoursesStyle,
                        // android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month += 1;
                                String dateSelecionada = String.format(year + "-" + month + "-" + day);

                                Date fechaSeleccionada = null;
                                try {
                                    fechaSeleccionada = formatoDelTexto.parse(dateSelecionada);
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                                mDisplayDate.setText(formatoDelTexto.format(fechaSeleccionada));
                            }
                        },
                        year, month, day);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mDisplayDate.setText("");
                        /*if (getCampo().getValidacion().equals("R")) {
                            getErrorView().setError("Requerido");
                            getErrorView().requestFocus();
                        }*/
                    }
                });
                if (minima != null) {
                    dialog.getDatePicker().setMinDate(minima.getTime());

                }
                if (maxima != null) {
                    dialog.getDatePicker().setMaxDate(maxima.getTime());
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
    }
}
