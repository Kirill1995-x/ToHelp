package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeCourses(context: Context, document: Document, courses:String): Resume(context, document)
{
    val courses:String

    init
    {
        this.courses = courses
    }

    override fun resume() {
        if(courses!="") {
            val arrayCourses: Array<String> = courses.split("block").toTypedArray();
            if (arrayCourses.size > 0) {
                //Заголовок "Образование"
                val paragraph = Paragraph()
                paragraph.add(Paragraph(context.resources.getString(R.string.courses), headingFont))
                CreateSeparateLine(paragraph)
                for (i in 0..(arrayCourses.size - 2)) {
                    val arrayCourse: Array<String> = arrayCourses[i].split("tag").toTypedArray();
                    paragraph.add(Paragraph(context.resources.getString(R.string.course) + " " + arrayCourse[0], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.skills) + " " + arrayCourse[1], userFont))
                    CreateMargin(paragraph)
                }
                document.add(paragraph)
            }
        }
    }
}