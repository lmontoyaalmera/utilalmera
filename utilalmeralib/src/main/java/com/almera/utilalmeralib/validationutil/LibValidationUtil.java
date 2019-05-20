package com.almera.utilalmeralib.validationutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class LibValidationUtil {
    /**
     * @param nombre
     * @param pattern
     * @return
     */
    public static boolean validarPatron(String nombre, String pattern) {
        Pattern patron = Pattern.compile(pattern);
        return patron.matcher(nombre).matches();
    }

    /**
     * @param nombre
     * @param maxLength
     * @return
     */
    public static boolean validarMaxLength(String nombre, int maxLength) {
        return nombre.length() > maxLength;
    }
    public static boolean validarMin_Max(Float valor, Float minimo, Float maximo) {
        if (valor >= minimo && valor <= maximo) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
    public  static boolean isFechaWithFormat(String cadena, String formato){
        final SimpleDateFormat date_format = new SimpleDateFormat(formato);
        try {
             date_format.parse(cadena);
            return  true;
        } catch (ParseException e) {
            return false;
        }
    }

}
