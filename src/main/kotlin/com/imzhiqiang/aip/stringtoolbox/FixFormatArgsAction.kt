package com.imzhiqiang.aip.stringtoolbox

import com.android.SdkConstants
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.xml.XmlFile


class FixFormatArgsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val xmlFile = e.getData(DataKeys.PSI_FILE) as XmlFile
        if (!StringPsi.hasStringTag(xmlFile)) return
        WriteCommandAction.runWriteCommandAction(e.project, FixFormatArgsCommand(xmlFile))
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(DataKeys.PSI_FILE)
        if (file is XmlFile) {
            e.presentation.isVisible = StringPsi.hasStringTag(file)
        } else {
            e.presentation.isVisible = false
        }
    }

    class FixFormatArgsCommand(private val xmlFile: XmlFile) : Runnable {
        override fun run() {
            val resources = xmlFile.rootTag ?: return
            val tags = resources.subTags
            var fixed = 0
            for (tag in tags) {
                val oldStr = tag.value.text
                if (StringFormatArgs.hasFormatArgs(oldStr)) {
                    val newStr = StringFormatArgs.fixFormatArgs(oldStr)
                    val resourcesKey = tag.getAttributeValue(SdkConstants.ATTR_NAME)
                    resourcesKey?.let {
                        StringPsi.modifyString(resources, it, newStr)
                        fixed++
                    }
                }
            }
            if (fixed > 0) {
                NotificationUtil.sendNotification(String.format("Fixed %d format arguments", fixed))
            } else {
                NotificationUtil.sendNotification("Cannot find format arguments need to be fixed")
            }
        }
    }

}