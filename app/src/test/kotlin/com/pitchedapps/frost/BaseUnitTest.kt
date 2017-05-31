package com.pitchedapps.frost

import android.content.Context
import android.os.Build
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Created by Allan Wang on 2017-05-30.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP),
        assetDir = "build/intermediates/classes/test/")
abstract class BaseUnitTest {

    @JvmField
    @Rule
    var dblflowTestRule = DBFlowTestRule.create()

    val context: Context
        get() = RuntimeEnvironment.application
}
