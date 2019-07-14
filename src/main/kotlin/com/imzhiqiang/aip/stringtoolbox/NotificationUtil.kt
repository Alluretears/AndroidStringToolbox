package com.imzhiqiang.aip.stringtoolbox

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

object NotificationUtil {

    fun sendNotification(msg: String, type: NotificationType = NotificationType.INFORMATION) {
        val notification = Notification("Android String Toolbox", "Android String Toolbox", msg, type)
        Notifications.Bus.notify(notification)
    }
}