package in.cbslgroup.ezeepeafinal.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {


    public static int daysBetween2Dates(String d1 ,String d2) throws Exception{

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputString1 = d1;
        String inputString2 = d2;

        int date = 0;

        if(inputString1.equalsIgnoreCase(d2)){

            date = 1;
        }
        else{


            try {

                Log.e("dates-->","from->"+inputString1+"\n"+"to->"+inputString2);

                Date date1 = myFormat.parse(inputString1);
                Date date2 = myFormat.parse(inputString2);
                long diff = date2.getTime() - date1.getTime();
                date = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);


                System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                long diffInDays = TimeUnit.MILLISECONDS.toDays(diff);
                System.out.println ("Days: " + diffInDays);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return date;
    }

    public static Date stringToDate(String date){

        Date dt = null;

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            dt = myFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;


    }

    public static String getCurrentTimeStamp(){

        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    }


    public static String getDate() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
