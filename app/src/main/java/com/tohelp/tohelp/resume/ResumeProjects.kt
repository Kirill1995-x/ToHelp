package com.tohelp.tohelp.resume

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.tohelp.tohelp.R

class ResumeProjects(context: Context, document: Document, projects:String): Resume(context, document)  {

   val projects:String

   init {
       this.projects = projects
   }

    override fun resume() {
        if(projects!="") {
            val arrayProjects: Array<String> = projects.split("block").toTypedArray();
            if (arrayProjects.size > 0) {
                //Заголовок "Образование"
                val paragraph = Paragraph()
                paragraph.add(Paragraph(context.resources.getString(R.string.projects), headingFont))
                CreateSeparateLine(paragraph)
                for (i in 0..(arrayProjects.size - 2)) {
                    val arrayProject: Array<String> = arrayProjects[i].split("tag").toTypedArray();
                    paragraph.add(Paragraph(context.resources.getString(R.string.project) + " " + arrayProject[0], userFont))
                    paragraph.add(Paragraph(context.resources.getString(R.string.work_in_project) + " " + arrayProject[1], userFont))
                    CreateMargin(paragraph)
                }
                document.add(paragraph)
            }
        }
    }
}