package com.imzhiqiang.aip.stringtoolbox

import com.android.SdkConstants
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import org.apache.commons.lang3.StringEscapeUtils

object StringPsi {

    fun hasStringTag(xmlFile: XmlFile): Boolean {
        val resources = xmlFile.rootTag
        val tags = resources?.subTags
        if (tags.isNullOrEmpty()) return false
        tags.forEach { tag -> if (tag.localName == SdkConstants.TAG_STRING) return true }
        return false
    }

    fun modifyString(rootTag: XmlTag, resourceKey: String, newValue: String) {
        val xmlTags = rootTag.findSubTags(SdkConstants.TAG_STRING, rootTag.namespace)
        kotlin.run loop@{
            xmlTags.forEach { tag ->
                if (resourceKey == tag.getAttributeValue(SdkConstants.ATTR_NAME)) {
                    tag.value.text = escape(newValue)
                    return@loop
                }
            }
        }
    }

    fun addString(rootTag: XmlTag, resourceKey: String, value: String, translatable: Boolean) {
        val string = rootTag.createChildTag(SdkConstants.TAG_STRING, rootTag.namespace, escape(value), false)
        string.setAttribute(SdkConstants.ATTR_NAME, resourceKey)
        if (!translatable) {
            string.setAttribute(SdkConstants.ATTR_TRANSLATABLE, java.lang.Boolean.FALSE.toString())
        }
        rootTag.addSubTag(string, false)
    }

    private fun escape(value: String): String {
        return StringEscapeUtils.escapeXml10(value)
    }
}