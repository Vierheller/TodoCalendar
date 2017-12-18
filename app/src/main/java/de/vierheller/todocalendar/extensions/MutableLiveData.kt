package de.vierheller.todocalendar.extensions

import android.arch.lifecycle.MutableLiveData

/**
 * Created by Vierheller on 18.12.2017.
 */
fun <T> MutableLiveData<T>.apply(job:(T)->Unit){
    val oldValue = this.value
    if(oldValue!=null)
        job.invoke(oldValue)
    this.value = oldValue
}