package com.example.polyschedule.presentation

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.polyschedule.R
import java.util.*


class ScheduleWidget : AppWidgetProvider() {
    private val LOG_TAG = "myLogs"


    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds))
        appWidgetIds.forEach { appWidgetId ->
            val view = RemoteViews(
                context.packageName,
                R.layout.schedule_widget
            )
            view.setTextViewText(R.id.schedule_widget_tv, "!!!!!!!")
            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, view)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds))
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(LOG_TAG, "onDisabled")
    }
}