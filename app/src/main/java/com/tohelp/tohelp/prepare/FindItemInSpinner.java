package com.tohelp.tohelp.prepare;

import android.content.Context;
import com.tohelp.tohelp.R;
import com.tohelp.tohelp.resume.Education;
import com.tohelp.tohelp.resume.GetArrayForResume;
import com.tohelp.tohelp.resume.WorkExperience;

public class FindItemInSpinner
{
    String name_of_spinner;
    String item;
    Context context;

    public FindItemInSpinner(String name_of_spinner, String item, Context context)
    {
        this.name_of_spinner = name_of_spinner;
        this.item = item;
        this.context = context;
    }

    public int getPosition()
    {
        String[] array;
        switch(name_of_spinner) {
            case "date":
                array = new GetArrayOfBirth().CreateArrayDate();
                break;
            case "month":
                array = new GetArrayOfBirth().CreateArrayMonth();
                break;
            case "year":
                array = new GetArrayOfBirth().CreateArrayYears();
                break;
            case "sex":
                array = context.getResources().getStringArray(R.array.array_sex);
                break;
            case "type_of_flat":
                array = context.getResources().getStringArray(R.array.array_type_of_flat);
                break;
            case "level_of_education":
                array = context.getResources().getStringArray(R.array.array_level_education);
                break;
            case "employment":
                array = context.getResources().getStringArray(R.array.array_employment);
                break;
            case "schedule":
                array = context.getResources().getStringArray(R.array.array_schedule);
                break;
            case "marital_status":
                array = context.getResources().getStringArray(R.array.array_marital_status);
                break;
            case "computer_skills":
                array = context.getResources().getStringArray(R.array.array_skill_of_using_computer);
                break;
            case "level_language":
                array = context.getResources().getStringArray(R.array.array_level_language);
                break;
            case "level_of_education_for_resume":
                array = context.getResources().getStringArray(R.array.array_level_education_for_resume);
                break;
            case "year_work":
                array = new GetArrayForResume().CreateArrayForWorkExperienceYears();
                break;
            case "month_work":
                array = new GetArrayForResume().CreateArrayForWorkExperienceMonth();
                break;
            case "year_education":
                array = new GetArrayForResume().CreateArrayForEducationYears();
                break;
            default:
                return 0;
        }

        for(int i=0; i<array.length; i++)
        {
            if(item.equals(array[i]))
            {
                return i;
            }
        }
        return 0;
    }
}


