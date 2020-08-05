package com.example.cyborg.Utils;

import androidx.core.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String[] resolvePeriod(Pair<Long,Long> selectedPeriod){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return new String[]{dateFormat.format(new Date(selectedPeriod.first)),dateFormat.format(new Date(selectedPeriod.second))};

    }
}
