package de.vierheller.todocalendar.extensions

import android.app.Activity
import android.content.res.TypedArray

/**
 * Created by Vierheller on 05.12.2017.
 */

/**
 * This function returns an Array of ResourceIds based on the resourceArray provided
 */
fun Activity.getListFromResourceArray(resourceArray: TypedArray): List<Int> {
    val list = List(resourceArray.length()) { i ->
        resourceArray.getResourceId(i, -1)
    }
    resourceArray.recycle()
    return list
}