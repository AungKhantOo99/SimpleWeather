package com.testing.myweather

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


const val apiKey="c2a1a17e1d6bddfa9c3d506f2ba2ff59"

@RequiresApi(Build.VERSION_CODES.O)

 fun getday(year:Int, month:Int, day:Int):String{
    val date = LocalDate.of(year, month, day)
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return dayOfWeek
}
@SuppressLint("SimpleDateFormat")
fun getsunraisesettime(time: Long):String {

    //change seconds to milliseconds
    val getTime: Long = time * 1000
// Create Date objects
    val getDate = Date(getTime)

// Create a SimpleDateFormat instance to format the time
    val dateFormat = SimpleDateFormat("HH:mm")

// Set the timezone (if needed, adjust to your local timezone)
    dateFormat.timeZone = TimeZone.getDefault()

// Format the dates to display in hours (HH:mm format)
    return dateFormat.format(getDate)
}

fun createAppShortcut(context: Context, shortcutId: String, label: String, iconResId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        val shortcutIntent = Intent(Intent.ACTION_WALLPAPER_CHANGED)
        shortcutIntent.addCategory(Intent.ACTION_QUICK_CLOCK)

        val shortcut = ShortcutInfo.Builder(context, shortcutId)
            .setShortLabel(label)
            .setIcon(Icon.createWithResource(context, iconResId))
            .setIntent(shortcutIntent)
            .build()

        shortcutManager.requestPinShortcut(shortcut, null)
    }
}
 fun addShortcut(context: Context) {
    //Adding shortcut for MainActivity
    //on Home screen
    val shortcutIntent = Intent(
       context,
        MainActivity::class.java
    )
    shortcutIntent.action = Intent.ACTION_MAIN
    val addIntent = Intent()
    addIntent
        .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "HelloWorldShortcut")
    addIntent.putExtra(
        Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        Intent.ShortcutIconResource.fromContext(
            context,
            R.drawable.ic_media_play
        )
    )
    addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
//    addIntent.putExtra("duplicate", false) //may it's already there so don't duplicate
    context.sendBroadcast(addIntent)
}





