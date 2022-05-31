package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeWorkExperience(context: Context, document: Document,  work:String): Resume(context, document)
{
    val work:String

    init {
        this.work = work
    }
    
    override fun resume() {
        if(work!="") {
            val arrayWorkExperience: Array<String> = work.split("block").toTypedArray()
            if (arrayWorkExperience.size > 0) {
                //Заголовок "Опыт работы"
                val paragraph = Paragraph()
                paragraph.add(Paragraph(context.resources.getString(R.string.work_experience), headingFont))
                CreateSeparateLine(paragraph)
                for (i in 0..(arrayWorkExperience.size - 2)) {
                    val arrayBlock: Array<String> = arrayWorkExperience[i].split("tag").toTypedArray();
                    val period_of_work: String =
                            if (arrayBlock[6] == "по настоящий момент")  arrayBlock[4] + " " + arrayBlock[5] + " - " + arrayBlock[6]
                            else arrayBlock[4] + " " + arrayBlock[5] + " - " + arrayBlock[6] + " " + arrayBlock[7]
                    paragraph.add(Paragraph(context.resources.getString(R.string.old_organization) + " " + arrayBlock[0], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.old_position) + " " + arrayBlock[1], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.old_responsibility) + " " + arrayBlock[2], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.old_employment) + " " + arrayBlock[3], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.period_of_work) + " " + period_of_work, userFont))
                    CreateMargin(paragraph)
                }
                document.add(paragraph)
            }
        }
    }
}