package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeEducation(context: Context, document: Document, education:String): Resume(context, document)
{

    val education:String

    init {
        this.education = education
    }

    override fun resume() {
       if (education!="") {
           val arrayEducation: Array<String> = education.split("block").toTypedArray();
           if (arrayEducation.size > 0) {
               //Заголовок "Образование"
               val paragraph = Paragraph()
               paragraph.add(Paragraph(context.resources.getString(R.string.education), headingFont))
               CreateSeparateLine(paragraph)
               for (i in 0..(arrayEducation.size - 2)) {
                   val arrayBlock: Array<String> = arrayEducation[i].split("tag").toTypedArray();
                   paragraph.add(Paragraph(context.resources.getString(R.string.name_of_education) + " " + arrayBlock[0], userFont))
                   paragraph.add(Paragraph(context.resources.getString(R.string.period_of_education) + " " + arrayBlock[1]+" - "+arrayBlock[2], userFont))
                   paragraph.add(Paragraph(context.resources.getString(R.string.level_of_education_resume) + " " + arrayBlock[3], userFont))
                   CreateMargin(paragraph)
               }
               document.add(paragraph)
           }
       }
    }
}