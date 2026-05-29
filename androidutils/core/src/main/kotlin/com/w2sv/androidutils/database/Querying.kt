package com.w2sv.androidutils.database

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri

/**
 * Queries a [Uri], moves to the first row, and closes the cursor.
 *
 * This wraps [ContentResolver.query] with `use` and first-row handling so
 * callers can focus on reading from a valid [Cursor].
 */
fun <R> ContentResolver.query(
    uri: Uri,
    columns: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    onCursor: (Cursor) -> R
): R? =
    query(
        uri,
        columns,
        selection,
        selectionArgs,
        null
    )
        ?.use { cursor ->
            if (cursor.moveToFirst()) {
                onCursor(cursor)
            } else {
                null
            }
        }
