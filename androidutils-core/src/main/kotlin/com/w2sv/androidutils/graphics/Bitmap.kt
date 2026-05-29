package com.w2sv.androidutils.graphics

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException
import kotlin.jvm.Throws

/**
 * Decodes a [Bitmap] from a content [Uri].
 *
 * This combines [ContentResolver.openInputStream] and [BitmapFactory.decodeStream]
 * into the common one-call path for content URIs.
 */
@Throws(FileNotFoundException::class)
fun ContentResolver.loadBitmap(uri: Uri): Bitmap? =
    BitmapFactory.decodeStream(openInputStream(uri))
