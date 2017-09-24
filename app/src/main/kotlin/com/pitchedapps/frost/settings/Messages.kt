package com.pitchedapps.frost.settings

import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import com.pitchedapps.frost.R
import com.pitchedapps.frost.activities.SettingsActivity
import com.pitchedapps.frost.utils.Prefs
import com.pitchedapps.frost.utils.iab.IS_FROST_PRO

/**
 * Created by Allan Wang on 2017-08-08.
 */
fun SettingsActivity.getMessagePrefs(): KPrefAdapterBuilder.() -> Unit = {

    checkbox(if (IS_FROST_PRO) R.string.incognito_mode else R.string.incognito_mode_pro,
            { Prefs.loadMediaOnMeteredNetwork }, { Prefs.loadMediaOnMeteredNetwork = it }) {
        descRes = R.string.incognito_mode_desc
    }

}