package com.w2sv.androidutils.database

import android.database.Cursor
import androidx.annotation.IntRange

/**
 * Reads a string column by name or throws if the column is missing.
 *
 * This combines [Cursor.getColumnIndexOrThrow] and [Cursor.getString] so call
 * sites do not manually pass column indexes around.
 */
fun Cursor.getStringOrThrow(columnName: String): String =
    getString(getColumnIndexOrThrow(columnName))

/**
 * Reads a long column by name or throws if the column is missing.
 *
 * This combines [Cursor.getColumnIndexOrThrow] and [Cursor.getLong] so call
 * sites do not manually pass column indexes around.
 */
fun Cursor.getLongOrThrow(columnName: String): Long =
    getLong(getColumnIndexOrThrow(columnName))

/**
 * Reads a boolean column by name from a `0` or `1` value.
 *
 * Android cursors do not expose a boolean getter; this keeps boolean decoding
 * consistent and still throws if the named column is absent.
 */
fun Cursor.getBooleanOrThrow(columnName: String): Boolean =
    getBooleanOrThrow(getColumnIndexOrThrow(columnName))

/**
 * Reads a boolean column by index from a `0` or `1` value.
 *
 * Android cursors do not expose a boolean getter; this centralizes conversion
 * and rejects unexpected stored values.
 */
fun Cursor.getBooleanOrThrow(@IntRange(from = 0) i: Int): Boolean =
    when (getString(i)) {
        "0" -> false
        "1" -> true
        else -> throw IllegalStateException("Invalid string for boolean conversion")
    }
