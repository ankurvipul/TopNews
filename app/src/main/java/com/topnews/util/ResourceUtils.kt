package com.topnews.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class ResourceUtils private constructor(private val context: Context) {

    /**
     * Method to get color resource
     */
    fun getColor(colorResId: Int): Int {
        return ContextCompat.getColor(context, colorResId)
    }

    /**
     * Method to get drawable resource
     */
    fun getDrawable(drawableResId: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)
    }

    /**
     * Method to get string resource
     */
    fun getString(resId: Int): CharSequence {
        return context.getString(resId)
    }

    companion object {
        private var resourceUtils: ResourceUtils? = null
        @JvmStatic
        fun init(context: Context) {
            if (resourceUtils == null) {
                synchronized(ResourceUtils::class.java) {
                    if (resourceUtils == null) {
                        resourceUtils = ResourceUtils(context)
                    }
                }
            }
        }

        @JvmStatic
        val instance: ResourceUtils?
            get() {
                checkNotNull(resourceUtils) { "Must call init() before getInstance()" }
                return resourceUtils
            }
    }

}