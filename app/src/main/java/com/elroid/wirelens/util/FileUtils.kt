package com.elroid.wirelens.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.webkit.MimeTypeMap

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.UUID

import androidx.exifinterface.media.ExifInterface
import com.elroid.wirelens.BuildConfig
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.v
import com.github.ajalt.timberkt.w
import timber.log.Timber
import javax.inject.Inject

/**
 * Class: com.elroid.wirelens.util.FileUtils
 * Project: WireLens
 * Created Date: 18/12/2017 18:10
 *
 * @author [Elliot Long](mailto:e@elroid.com)
 * Copyright (c) 2017 Elroid Ltd. All rights reserved.
 */

const val DEFAULT_FOLDER_NAME = "images"
const val FILE_PROVIDER = "${BuildConfig.APPLICATION_ID}.fileprovider"


//this is a util class, methods should be public and may only be used once or not at all
@Suppress("unused")
class FileUtils @Inject constructor(val ctx:Context) {

	@Throws(IOException::class)
	fun pickedExistingPicture(photoUri:Uri?):File? {
		Timber.d("pickedExistingPicture(ctx:%s, photoUri:%s)", ctx, photoUri)
		if(photoUri == null) return null
		val pictureInputStream = ctx.contentResolver.openInputStream(photoUri)
			?: throw IOException("No image stream found")
		val directory = tempImageDirectory(ctx)
		val photoFile = File(directory,
			UUID.randomUUID().toString() + "." + getExtension(ctx, photoUri))
		if(!photoFile.createNewFile())
			Timber.w("Create file failed!")
		writeToFile(pictureInputStream, photoFile)
		return photoFile
	}

	@Throws(IOException::class)
	fun tempImageDirectory():File = tempImageDirectory(ctx)

	private fun createTmpFile(): File {
		val filesDir: File = ctx.filesDir ?: throw IOException("Unable to find files dir")
		val storageDir = File(filesDir, DEFAULT_FOLDER_NAME)
		if(!storageDir.exists()) storageDir.mkdirs()
		val file = File.createTempFile("tmpImg", ".jpg", storageDir)
		d { "Created file:$file" }
		return file
	}

	@Throws(IOException::class)
	fun createFile(bitmap:Bitmap):File = createFile(ctx, bitmap)

	companion object {

		@Throws(IOException::class)
		fun tempImageDirectory(ctx:Context):File {
			val privateTempDir = File(ctx.cacheDir, DEFAULT_FOLDER_NAME)
			if(!privateTempDir.exists()) {
				if(!privateTempDir.mkdirs())
					throw IOException("Unable to create dirs!")
			}
			return privateTempDir
		}
		@Throws(IOException::class)
		@JvmStatic
		fun createFile(ctx:Context, bitmap:Bitmap):File {
			val directory = tempImageDirectory(ctx)
			val photoFile = File(directory, UUID.randomUUID().toString() + ".jpg")
			if(!photoFile.createNewFile())
				throw IOException("Unable to create file: $photoFile")
			val out = FileOutputStream(photoFile)
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
			return photoFile
		}

		fun writeToFile(inputStream:InputStream, file:File) {
			try {
				val out = FileOutputStream(file)
				val buf = ByteArray(1024)
				var len = inputStream.read(buf)
				while(len > 0) {
					out.write(buf, 0, len)
					len = inputStream.read(buf)
				}
				out.close()
				inputStream.close()
			} catch(e:Exception) {
				e.printStackTrace()
			}

		}

		fun writeToFile(bitmap:Bitmap, file:File) {
			try {
				val out = FileOutputStream(file)
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
				out.close()
			} catch(e:Exception) {
				e.printStackTrace()
			}

		}

		@Throws(IOException::class)
		@JvmStatic
		@Deprecated(
			message = "moved to loadBitmap",
			replaceWith = ReplaceWith(expression = "loadBitmap"),
			level = DeprecationLevel.WARNING)
		fun createBitmapFromFile(imgFile:File):Bitmap {
			Timber.d("createBitmapFromFile(imgFile:%s)", imgFile)
			/*val bmOptions = BitmapFactory.Options()
			return BitmapFactory.decodeFile(imgFile.absolutePath, bmOptions)*/
			return loadBitmap(imgFile)
		}

		@Throws(IOException::class)
		fun getBitmapFromURL(urlStr:String):Bitmap? {
			try {
				val url = URL(urlStr)
				val connection = url.openConnection()
				connection.doInput = true
				connection.connect()
				val input = connection.getInputStream()
				return BitmapFactory.decodeStream(input)
			} catch(e:IOException) {
				Timber.w(e, "Error getting bitamp from: %s", urlStr)
				return null
			}

		}

		/**
		 * To find out the extension of required object in given uri
		 * Solution by http://stackoverflow.com/a/36514823/1171484
		 */
		fun getExtension(context:Context, uri:Uri):String? {
			//Check uri format to avoid null
			return if(uri.scheme == ContentResolver.SCHEME_CONTENT) {
				//If scheme is a content
				val mime = MimeTypeMap.getSingleton()
				mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
			} else {
				//If scheme is a File
				//This will replace white spaces with %20 and also other special characters.
				// This will avoid returning null values on file name with spaces and special characters.
				uri.path?.apply{
					MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(this)).toString())
				}
			}
		}

		fun scaleBitmapDown(bitmap:Bitmap, maxDimension:Int):Bitmap {

			val originalWidth = bitmap.width
			val originalHeight = bitmap.height
			var resizedWidth = maxDimension
			var resizedHeight = maxDimension

			if(originalHeight > originalWidth) {
				resizedHeight = maxDimension
				resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
			} else if(originalWidth > originalHeight) {
				resizedWidth = maxDimension
				resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
			} else if(originalHeight == originalWidth) {
				resizedHeight = maxDimension
				resizedWidth = maxDimension
			}
			return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
		}

		@JvmStatic
		fun scaleBitmapToWidth(bitmap:Bitmap, maxWidth:Int):Bitmap {

			val originalWidth = bitmap.width
			val originalHeight = bitmap.height
			val resizedWidth = Math.min(maxWidth, originalWidth)
			val resizedHeight = GenUtils.getHeightAtWidth(originalWidth, originalHeight, resizedWidth)

			return if(resizedWidth == originalWidth)
				bitmap
			else
				Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
		}

		@Throws(IOException::class)
		fun getResizedBitmapFromURL(urlStr:String, localFile:File, maxWidth:Int):Bitmap {
			Timber.v("getResizedBitmapFromURL(urlStr:%s, localFile:%s, maxWidth:%s)", urlStr, localFile,
				maxWidth)

			val url = URL(urlStr)
			val connection = url.openConnection()
			connection.doInput = true
			connection.connect()
			val input = connection.getInputStream()
			writeToFile(input, localFile)
			val fin = FileInputStream(localFile)

			//check rotation
			val exif = ExifInterface(localFile.absolutePath)
			val exifOrientation = exif.getAttributeInt(
				ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL)

			var rotate = 0

			when(exifOrientation) {
				ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
				ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
				ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
			}

			var bitmap = BitmapFactory.decodeStream(fin)

			if(rotate != 0) {
				Timber.v("rotating to %s", rotate)
				val w = bitmap.width
				val h = bitmap.height

				// Setting pre rotate
				val mtx = Matrix()
				mtx.preRotate(rotate.toFloat())

				// Rotating Bitmap & convert to ARGB_8888, required by tess
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false)
			}

			if(maxWidth > 0) {
				//resize
				bitmap = scaleBitmapToWidth(bitmap, maxWidth)
			}

			return bitmap.copy(Bitmap.Config.ARGB_8888, true)
		}

		fun loadBitmap(imgFile:File): Bitmap {
			val rawBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
				?: throw IOException("unable to load bitmap: $this")
			val exif = android.media.ExifInterface(imgFile.absolutePath)
			val orientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION,
				android.media.ExifInterface.ORIENTATION_UNDEFINED)
			v { "Orientation:$orientation" }
			val matrix = when(orientation) {
				android.media.ExifInterface.ORIENTATION_ROTATE_90 -> rotatedMatrix(90)
				android.media.ExifInterface.ORIENTATION_ROTATE_180 -> rotatedMatrix(180)
				android.media.ExifInterface.ORIENTATION_ROTATE_270 -> rotatedMatrix(270)
				else -> {
					w { "Orientation unrecognised: $orientation" }
					rotatedMatrix(0)
				}
			}
			return if(matrix == null) {
				rawBitmap
			} else { //rotate
				Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height,
					matrix, true)
			}
		}

		private fun rotatedMatrix(degrees: Int): Matrix? {
			if(degrees == 0) return null
			val matrix = Matrix()
			matrix.postRotate(degrees.toFloat())
			return matrix
		}
	}
}




