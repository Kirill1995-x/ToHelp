package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R
import com.tohelp.tohelp.settings.Variable

class ResumeMainSkills(context: Context, document: Document, basicSkills: String, computerSkills: String,
                       program: String,  militaryService: Boolean, driversLicence:Int):
       Resume(context, document)
{
    val basicSkills:String
    val computerSkills:String
    val program:String
    val militaryService:Boolean
    val driversLicence:Int

    init {
       this.basicSkills = basicSkills
       this.computerSkills = computerSkills
       this.program = program
       this.militaryService = militaryService
       this.driversLicence = driversLicence
    }

    fun DriversLicence(code: Int):String
    {
        var drivers_licence = ""

        val category: Array<String?> = arrayOfNulls(9)

        category[0] = if ((code and 0x1) == 0x1) context.resources.getString(R.string.category_A) else ""
        category[1] = if ((code and 0x2) == 0x2) context.resources.getString(R.string.category_B) else ""
        category[2] = if ((code and 0x4) == 0x4) context.resources.getString(R.string.category_BE) else ""
        category[3] = if ((code and 0x8) == 0x8) context.resources.getString(R.string.category_C) else ""
        category[4] = if ((code and 0x10) == 0x10) context.resources.getString(R.string.category_CE) else ""
        category[5] = if ((code and 0x20) == 0x20) context.resources.getString(R.string.category_D) else ""
        category[6] = if ((code and 0x40) == 0x40) context.resources.getString(R.string.category_DE) else ""
        category[7] = if ((code and 0x80) == 0x80) context.resources.getString(R.string.category_M) else ""
        category[8] = if ((code and 0x100) == 0x100) context.resources.getString(R.string.category_Tb_and_Tm) else ""

        for (i in 0..8)
        {
            drivers_licence +=
                    if (drivers_licence=="")
                    {
                        if (category[i]=="") "" else category[i]
                    }
                    else
                    {
                        if (category[i]=="") "" else ", " + category[i]
                    }
        }

        return drivers_licence
    }

    override fun resume()
    {
        if(basicSkills!="" || computerSkills!="" || program!="" || militaryService || driversLicence!=0)
        {
            //Заголовок "Ключевые навыки"
            val paragraph = Paragraph()
            paragraph.add(Paragraph(context.resources.getString(R.string.main_skills), headingFont))
            CreateSeparateLine(paragraph)
            //Основные навыки
            if (basicSkills != "") paragraph.add(Paragraph(context.resources.getString(R.string.basic_skills) + " " + basicSkills, userFont))
            //Навыки работы с компьютером
            if (computerSkills != "") paragraph.add(Paragraph(context.resources.getString(R.string.computer_skills) + " " + computerSkills, userFont))
            //Программы
            if (program != "") paragraph.add(Paragraph(context.resources.getString(R.string.program) + " " + program, userFont))
            //Служба в армии
            if (context.getSharedPreferences(Variable.APP_PREFERENCES, Context.MODE_PRIVATE).getString("shared_sex", "") == "Мужской") {
                if (militaryService) paragraph.add(Paragraph(context.resources.getString(R.string.military_service) + " " + "проходил службу", userFont))
            }
            //Водительские права
            if (driversLicence != 0) paragraph.add(Paragraph(context.resources.getString(R.string.drivers_licence) + " " + DriversLicence(driversLicence), userFont))

            CreateMargin(paragraph)
            document.add(paragraph)
        }
    }
}