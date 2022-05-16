package com.tim.uv_dn5;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class poskus {
    public static void main(String[] args) {

        //pridobi današnji datum
        Calendar cal = Calendar.getInstance();
        String yearToday = Integer.toString(cal.get(Calendar.YEAR));
        String monthToday = Integer.toString(cal.get(Calendar.MONTH) + 1);
        if (monthToday.length() == 1) {
            monthToday = "0" + monthToday;
        }
        String dayToday = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (dayToday.length() == 1) {
            dayToday = "0" + dayToday;
        }

        //izračunaj razliko v dnevih
        SimpleDateFormat myFormat = new SimpleDateFormat("MM dd yyyy");
        String inputString1 = String.format("%s %s %s", 05, 18, 2021);
        String inputString2 = String.format("%s %s %s", dayToday, monthToday, yearToday);

        System.out.println("Datum podan: " + inputString1);
        System.out.println("Datum danes: " +inputString2);

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            if (date1.after(date2)) {
                System.out.println(true);
            }
            else {
                System.out.println(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
