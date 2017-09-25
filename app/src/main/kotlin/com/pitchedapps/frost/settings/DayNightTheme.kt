package com.pitchedapps.frost.settings

import ca.allanwang.kau.kpref.activity.KPrefAdapterBuilder
import ca.allanwang.kau.kpref.activity.items.KPrefColorPicker
import ca.allanwang.kau.ui.views.RippleCanvas
import ca.allanwang.kau.utils.string
import com.pitchedapps.frost.R
import com.pitchedapps.frost.activities.SettingsActivity
import com.pitchedapps.frost.enums.Theme
import com.pitchedapps.frost.injectors.CssAssets
import com.pitchedapps.frost.utils.*
import com.pitchedapps.frost.utils.iab.IS_FROST_PRO

/**
 * Created by Allan Wang on 2017-08-08.
 */
fun SettingsActivity.getDayNightThemePrefs(): KPrefAdapterBuilder.() -> Unit = {

    header(R.string.theme_customization)

    checkbox(R.string.enable_day_night_theme)

    text(R.string.theme, { Prefs.themeNight }, { Prefs.themeNight = it }) {
        onClick = { _, _, item ->
            materialDialogThemed {
                title(R.string.theme)
                items(Theme.values
                        .filter { it != Theme.CUSTOM_NIGHT }
                        .map { if (it == Theme.CUSTOM && !IS_FROST_PRO) R.string.custom_pro else it.textRes }
                        .map { string(it) })
                itemsCallbackSingleChoice(item.pref) { _, _, which, _ ->
                    if (item.pref != which) {
                        if (which == Theme.CUSTOM.ordinal && !IS_FROST_PRO) {
                            purchasePro()
                            return@itemsCallbackSingleChoice true
                        }
                        item.pref = if (which == Theme.CUSTOM.ordinal) Theme.CUSTOM_NIGHT.ordinal else which
                        shouldRestartMain()
                        reload()
                        setFrostTheme(true)
                        themeExterior()
                        invalidateOptionsMenu()
                        frostAnswersCustom("Theme Night", "Count" to Theme(which).name)
                    }
                    true
                }
            }
            true
        }
        textGetter = {
            string(Theme(it).textRes)
        }
    }

    fun KPrefColorPicker.KPrefColorContract.dependsOnCustom() {
        enabler = { Prefs.isCustomTheme }
        onDisabledClick = { _, _, _ -> frostSnackbar(R.string.requires_custom_theme); true }
        allowCustom = true
    }

    fun invalidateCustomTheme() {
        CssAssets.CUSTOM.injector = null
    }

    colorPicker(R.string.text_color, { Prefs.customTextColorNight }, {
        Prefs.customTextColorNight = it
        reload()
        invalidateCustomTheme()
        shouldRestartMain()
    }) {
        dependsOnCustom()
        allowCustomAlpha = false
    }

    colorPicker(R.string.accent_color, { Prefs.customAccentColorNight }, {
        Prefs.customAccentColorNight = it
        reload()
        invalidateCustomTheme()
        shouldRestartMain()
    }) {
        dependsOnCustom()
        allowCustomAlpha = false
    }


    colorPicker(R.string.background_color, { Prefs.customBackgroundColorNight }, {
        Prefs.customBackgroundColorNight = it
        bgCanvas.ripple(it, duration = 500L)
        invalidateCustomTheme()
        setFrostTheme(true)
        shouldRestartMain()
    }) {
        dependsOnCustom()
        allowCustomAlpha = true
    }

    colorPicker(R.string.header_color, { Prefs.customHeaderColorNight }, {
        Prefs.customHeaderColorNight = it
        frostNavigationBar()
        toolbarCanvas.ripple(it, RippleCanvas.MIDDLE, RippleCanvas.END, duration = 500L)
        reload()
        shouldRestartMain()
    }) {
        dependsOnCustom()
        allowCustomAlpha = true
    }

    colorPicker(R.string.icon_color, { Prefs.customIconColorNight }, {
        Prefs.customIconColorNight = it
        invalidateOptionsMenu()
        shouldRestartMain()
    }) {
        dependsOnCustom()
        allowCustomAlpha = false
    }

}