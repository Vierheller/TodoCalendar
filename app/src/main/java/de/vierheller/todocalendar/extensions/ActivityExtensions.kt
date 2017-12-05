package de.vierheller.todocalendar.extensions

import android.app.Activity
import android.content.res.TypedArray

/**
 * Created by Vierheller on 05.12.2017.
 */

fun Activity.getListFromResourceArray(resourceArray: TypedArray): MutableList<Int> {
    val list = MutableList<Int>(resourceArray.length()) { i ->
        resourceArray.getResourceId(i, -1)
    }
    resourceArray.recycle()
    return list
}