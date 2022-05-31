package com.tohelp.tohelp.prepare;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetArrayOfBirth
{
    public String[] CreateArrayDate()
    {
        String[] array_date = new String[32];
        for(int i=0; i<array_date.length; i++)
        {
            array_date[i] = (i!=0)?String.valueOf(i):"День";
        }

        return array_date;
    }

    public Integer[] CreateArrayDateNumber()
    {
        Integer[] array_date = new Integer[32];
        for (int i=0; i<array_date.length; i++)
        {
            array_date[i]=i;
        }

        return array_date;
    }

    public String[] CreateArrayMonth()
    {
        return new String[]{"Месяц", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    }

    public Integer[] CreateArrayMonthNumber()
    {
        Integer[] array_month = new Integer[13];
        for(int i=0; i<array_month.length; i++)
        {
            array_month[i] = i;
        }

        return array_month;
    }


    public String[] CreateArrayYears()
    {
        //формирование диапазона возрастов от 1960 года до (нынешнего года - 14 лет)
        //одна 1 добавляется, так как в списке должно быть "Год"
        int get_year = new GregorianCalendar().get(Calendar.YEAR)-13;
        int length = get_year-1960+1;
        String[] array_year = new String[length];
        for (int i = 0; i < length; i++)
        {
            array_year[i] = (i!=0)? String.valueOf(get_year-i):"Год";
        }

        return array_year;
    }

    public Integer[] CreateArrayYearsNumber()
    {
        //формирование диапазона возрастов от 1960 года до (нынешнего года - 14 лет)
        //одна 1 добавляется, так как в списке должно быть "0"
        int get_year = new GregorianCalendar().get(Calendar.YEAR)-13;
        int length = get_year-1960+1;
        Integer[] array_year = new Integer[length];
        for (int i = 0; i < array_year.length; i++)
        {
            array_year[i] = (i!=0)? get_year-i:0;
        }

        return array_year;
    }

    public String[] CreateArrayYearsStrong()
    {
        //формирование диапазона возрастов от 1960 года до (нынешнего года - 18 лет)
        //одна 1 добавляется, так как в списке должно быть "Год"
        int get_year = new GregorianCalendar().get(Calendar.YEAR)-17;
        int length = get_year-1960+1;
        String[] array_year = new String[length];
        for (int i = 0; i < length; i++)
        {
            array_year[i] = (i!=0)? String.valueOf(get_year-i):"Год";
        }

        return array_year;
    }
}
