package com.pitchedapps.frost.utils

import android.graphics.Color
import ca.allanwang.kau.kotlin.lazyResettable
import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.StringSet
import ca.allanwang.kau.kpref.kpref
import ca.allanwang.kau.utils.isColorVisibleOn
import com.pitchedapps.frost.enums.FACEBOOK_BLUE
import com.pitchedapps.frost.enums.FeedSort
import com.pitchedapps.frost.enums.MainActivityLayout
import com.pitchedapps.frost.enums.Theme
import com.pitchedapps.frost.injectors.InjectorContract

/**
 * Created by Allan Wang on 2017-05-28.
 *
 * Theme specific preferences
 * TODO deprecate Pref themes
 */
object ThemePrefs : KPref() {
    val themeLoader = lazyResettable {
        
    }
    var themeDayNight: Int by lazyResettable {  //global toggle for the right theme use

    }

    /*
     * Theme
     */

    var theme: Int by kpref("theme", 0, postSetter = { _: Int -> loader.invalidate() })

    var customTextColor: Int by kpref("color_text", Prefs.customTextColor)

    var customAccentColor: Int by kpref("color_accent", Prefs.customAccentColor)

    var customBackgroundColor: Int by kpref("color_bg", Prefs.customBackgroundColor)

    var customHeaderColor: Int by kpref("color_header", Prefs.customHeaderColor)

    var customIconColor: Int by kpref("color_icons", Prefs.customIconColor)

    private val loader = lazyResettable { Theme.values[theme] }

    private val t: Theme by loader

    val textColor: Int
        get() = t.textColor

    val accentColor: Int
        get() = t.accentColor

    val accentColorForWhite: Int
        get() = if (accentColor.isColorVisibleOn(Color.WHITE)) accentColor
        else if (textColor.isColorVisibleOn(Color.WHITE)) textColor
        else FACEBOOK_BLUE

    val bgColor: Int
        get() = t.bgColor

    val headerColor: Int
        get() = t.headerColor

    val iconColor: Int
        get() = t.iconColor

    val themeInjector: InjectorContract
        get() = t.injector

    val isCustomTheme: Boolean
        get() = t == Theme.CUSTOM
    /*
     * Night Theme
     */

    var themeNightEnabled: Boolean by kpref("theme_night_enabled", false)

    var themeNight: Int by kpref("theme_night", 0, postSetter = { _: Int -> loaderNight.invalidate() })

    var customTextColorNight: Int by kpref("color_text_night", 0xffeceff1.toInt())

    var customAccentColorNight: Int by kpref("color_accent_night", 0xff0288d1.toInt())

    var customBackgroundColorNight: Int by kpref("color_bg_night", 0xff212121.toInt())

    var customHeaderColorNight: Int by kpref("color_header_night", 0xff01579b.toInt())

    var customIconColorNight: Int by kpref("color_icons_night", 0xffeceff1.toInt())

    private val loaderNight = lazyResettable { Theme.values[Prefs.themeNight] }

    private val tNight: Theme by loaderNight
}