package com.qin.mvvm.utils

import android.content.Context

@JvmOverloads
fun <T> getSpValue(
    filename: String,
    context: Context,
    key: String,
    defaultValue: T
): T {
    val sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    return when(defaultValue) {
        is Boolean -> sp.getBoolean(key, defaultValue) as T
        is String -> sp.getString(key, defaultValue) as T
        is Int -> sp.getInt(key, defaultValue) as T
        is Long -> sp.getLong(key, defaultValue) as T
        is Float -> sp.getFloat(key, defaultValue) as T
        is Set<*> -> sp.getStringSet(key, defaultValue as Set<String>) as T
        else -> throw IllegalArgumentException("Unrecognized default value $defaultValue")
    }
}

@JvmOverloads
fun <T> putSpValue(
    filename: String,
    context: Context,
    key: String,
    value: T
) {
    val editor = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()
    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        is Set<*> -> editor.putStringSet(key, value as Set<String>)
        else -> throw UnsupportedOperationException("Unrecognized value $value")
    }
    editor.apply()
}

@JvmOverloads
fun removeSpValue(filename: String, context: Context, key: String) {
    context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        .edit()
        .remove(key)
        .apply()
}

@JvmOverloads
fun clearSpValue(filename: String, context: Context) {
    context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        .edit()
        .clear()
        .apply()
}