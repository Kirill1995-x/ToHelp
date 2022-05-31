package com.tohelp.tohelp.resume;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetArrayForResume {
    public String[] CreateArrayForEducationYears()
    {
        //формирование диапазона годов от 1990 года до (нынешнего года + 6 лет)
        //одна 1 добавляется, так как в списке должно быть "Год"
        int get_year = new GregorianCalendar().get(Calendar.YEAR)+7;
        int length = get_year-1990;
        String[] array_year = new String[length];
        for (int i = 0; i < length; i++)
        {
            array_year[i] = (i!=0)? String.valueOf(get_year-i):"Год";
        }

        return array_year;
    }

    public String[] CreateArrayForWorkExperienceYears()
    {
        //формирование диапазона годов от 1990 года до нынешнего года
        //одна 1 добавляется, так как в списке должно быть "Год"
        int get_year = new GregorianCalendar().get(Calendar.YEAR)+1;
        int length = get_year-1990;
        String[] array_year = new String[length];
        for (int i = 0; i < length; i++)
        {
            array_year[i] = (i!=0)? String.valueOf(get_year-i):"Год";
        }

        return array_year;
    }

    public String[] CreateArrayForWorkExperienceMonth()
    {
        return new String[]{"Месяц", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    }
}
