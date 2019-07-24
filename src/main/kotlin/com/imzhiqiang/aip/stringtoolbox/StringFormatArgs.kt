package com.imzhiqiang.aip.stringtoolbox

import java.util.regex.Pattern

object StringFormatArgs {

    private const val formatSpecifier = "%([-#+0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z])"

    @JvmStatic
    private val fsPattern = Pattern.compile(formatSpecifier)

    fun hasFormatArgs(value: String): Boolean {
        val matcher = fsPattern.matcher(value)
        return matcher.find()
    }

    fun fixFormatArgs(value: String): String {
        if (!hasFormatArgs(value)) return value
        val sb = StringBuilder()
        val matcher = fsPattern.matcher(value)
        var groupIndex = 0
        var lastGroupEnd = 0
        while (matcher.find()) {
            val groupStart = matcher.start()
            sb.append(value, lastGroupEnd, groupStart)
            lastGroupEnd = matcher.end()
            sb.append(matcher.group())
            sb.insert(groupStart + groupIndex * 2 + 1, (++groupIndex).toString() + "$")
        }
        if (lastGroupEnd > 0) {
            sb.append(value, lastGroupEnd, value.length)
        }
        return sb.toString()
    }
}