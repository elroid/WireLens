package com.elroid.wirelens.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.elroid.wirelens.R
import com.elroid.wirelens.model.CredentialsImage
import com.elroid.wirelens.model.WifiNetwork
import com.elroid.wirelens.model.WifiState
import com.elroid.wirelens.ui.CAMERA_REQUEST_CODE
import com.elroid.wirelens.ui.GALLERY_REQUEST_CODE
import com.elroid.wirelens.ui.base.BaseActivity
import com.elroid.wirelens.util.FILE_PROVIDER
import com.elroid.wirelens.util.createString
import com.elroid.wirelens.util.printCallingMethod
import com.elroid.wirelens.util.toLog
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.v
import com.github.ajalt.timberkt.w
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class MainActivity:BaseActivity(), MainContract.View {

	companion object create {
		fun intent(ctx:Context):Intent {
			return Intent(ctx, MainActivity::class.java)
		}
	}

	@Inject
	lateinit var presenter:MainPresenter

	override fun onCreate(savedInstanceState:Bundle?) {
		AndroidInjection.inject(this)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		cameraButton.setOnClickListener { presenter.onCameraButtonClicked() }
		galleryButton.setOnClickListener { presenter.onGalleryButtonClicked() }
		qrButton.setOnClickListener { presenter.onQrButtonClicked() }
	}

	override fun showQrCode(qrCode:Bitmap) {
		todo(qrCode)
	}

	override fun showCurrentWifiData(network:WifiNetwork, state:WifiState) {
		todo(network, state)
	}

	override fun showWifiList(list:List<WifiNetwork>) {
		todo(list)
	}

	private val opts:QuickPermissionsOptions by lazy {
		QuickPermissionsOptions(
			rationaleMessage = createString(R.string.permission_justification_camera)
		)
	}

	override fun takePictureWithPermissions(tmpFile:File) = runWithPermissions(
		Manifest.permission.CAMERA, options = opts) {
		val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		getCtx().let { ctx ->
			pictureIntent.resolveActivity(ctx.packageManager)?.also {
				try {
					val uri = FileProvider.getUriForFile(ctx, FILE_PROVIDER, tmpFile)
					d { "file:$tmpFile, uri:$uri" }
					pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
					startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE)

				} catch(ex:Exception) {
					w(ex)
					showError("Unable to create file")
				}
			}
		}
	}

	override fun openGalleryWithPermissions() {
		val intent = Intent(Intent.ACTION_PICK)
		val imageTypes = arrayOf("image/*")//, "image/png", "image/jpg")
		intent.type = imageTypes[0]
		intent.putExtra(Intent.EXTRA_MIME_TYPES, imageTypes)
		try {
			startActivityForResult(Intent.createChooser(intent, "Select an image"),
				GALLERY_REQUEST_CODE)

		} catch(ex: Exception) {
			w(ex)
			showError("Unable to open gallery: ${ex.message}")
		}
	}

	override fun startQrScanner() {
		todo()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		v { "onActivityResult(request:$requestCode, result:$resultCode, data:${data?.toLog()})" }
		when(requestCode) {
			GALLERY_REQUEST_CODE -> {
				when(resultCode) {
					Activity.RESULT_OK -> {
						data?.data?.let { uri ->
							presenter.onGalleryImageSelected(uri)
						}
					}
					Activity.RESULT_CANCELED -> toast("Image import cancelled")
					else -> w { "Unrecognised result:$resultCode" }
				}
			}
			CAMERA_REQUEST_CODE -> {
				when(resultCode) {
					Activity.RESULT_OK -> {
						presenter.onCameraImageSelected()
					}
					Activity.RESULT_CANCELED -> toast("Image capture cancelled")
					else -> w { "Unrecognised result:$resultCode" }
				}
			}
			else -> super.onActivityResult(requestCode, resultCode, data)
		}
	}

	override fun startConnectorService(image:CredentialsImage) {
		todo(image)
	}

	override fun showConnectorStartedMessage() {
		todo()
	}




	//@Inject lateinit var wifiManager: WifiManager
	//@Inject lateinit var wifiDataManager: WifiDataManager
//	fun onCameraButtonClicked() {
//		Timber.i("Camera button clicked")
//		/*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//		startActivityForResult(intent, CAMERA_REQUEST_CODE)*/
//		scanWithPermissionsCheck()
//	}
//
//	fun onGalleryButtonClicked() {
//		toast("Gallery button clicked")
//		showNetworks()
//	}
//
//	fun onQrButtonClicked() {
//		toast("Click QR!")
//		val svc = Completable.create({
//			try {
//				val bmp = FileUtils.getBitmapFromURL("http://elroid.com/wirelens/droidcon.jpg");
//				val credImg = CredentialsImage(bmp)
//				ConnectorService.start(this, credImg)
//			} catch (e: Exception) {
//				Timber.e(e, "Error scanning network image: %s", e.message)
//			}
//		})
//		svc.subscribeOn(Schedulers.io()).subscribe()
//	}
//
//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//		if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//
//			// Convert image data to bitmap
//			val bitmap = data.extras!!.get("data") as Bitmap
//
//			// Set the bitmap as the source of the ImageView
//			//(findViewById<View>(R.id.previewImage) as ImageView).setImageBitmap(picture)
//			ConnectorService.start(this, CredentialsImage(bitmap))
//		}
//	}
//
//	private fun scanWithPermissionsCheck() {
//
//		val dialogListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
//			.withContext(this)
//			.withTitle("Wifi & course location permission")
//			.withMessage("Both change wifi state and coarse location permissions are needed to scan for wifi")
//			.withButtonText(android.R.string.ok)
//			.withIcon(R.mipmap.ic_launcher)
//			.build()
//
//		val actionListener = object : BaseMultiplePermissionsListener() {
//			override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//				if (report!!.areAllPermissionsGranted()) {
//					scan()
//				}
//			}
//		}
//
//		Dexter.withActivity(this)
//			.withPermissions(
//				Manifest.permission.ACCESS_COARSE_LOCATION,
//				Manifest.permission.CHANGE_WIFI_STATE,
//				Manifest.permission.VIBRATE,
//				Manifest.permission.ACCESS_WIFI_STATE)
//			.withListener(CompositeMultiplePermissionsListener(dialogListener, actionListener))
//			.check()
//	}
//
//
//	private fun scan() {
//		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//		startActivityForResult(intent, CAMERA_REQUEST_CODE)
//
//	}
//
//	fun showNetworks() {
//		/*val configs = wifiManager?.getConfiguredNetworks()
//		Timber.i("networks: %s", configs)*/
//		Timber.i("widama:%s", wifiDataManager)
//		wifiDataManager.scan()
//			?.subscribeOn(Schedulers.io())
//			?.observeOn(AndroidSchedulers.mainThread())
//			?.subscribe({
//				Timber.d("got wifi result: %s", it)
//				//narf.append("\n"+String.format("'%s' (%s) at %sdb", wifiNetwork.getSsid(),
//				// wifiNetwork.getCapabilities(), wifiNetwork.getSignalLevel()));
//				for (i in it.indices) {
//					val wifiNetwork = it[i]
//					Timber.i(String.format("'%s' at %sdb", wifiNetwork.ssid,
//						wifiNetwork.signalLevel))
//				}
//			}, { Timber.w(it, "Error: %s", it.message) },
//				{ Timber.i("Completed") })
//
//		/*?.subscribe(object : Observer<List<WifiNetwork>> {
//			override fun onSubscribe(d: Disposable) {
//				Timber.d("subscribed to scan")
//			}
//
//			override fun onSuccess(wifiNetworks: List<WifiNetwork>) {
//				Timber.d("got wifi result: %s", wifiNetworks)
//				//narf.append("\n"+String.format("'%s' (%s) at %sdb", wifiNetwork.getSsid(),
//				// wifiNetwork.getCapabilities(), wifiNetwork.getSignalLevel()));
//				for (i in wifiNetworks.indices) {
//					val wifiNetwork = wifiNetworks[i]
//
//					Timber.i(String.format("'%s' at %sdb", wifiNetwork.ssid,
//						wifiNetwork.signalLevel))
//					*//*narf.append("\n" + String.format("'%s' at %sdb", wifiNetwork.ssid,
//							wifiNetwork.signalLevel))*//*
//					}
//				}
//
//				override fun onError(e: Throwable) {
//					Timber.w(e, "Scan error happened")
//				}
//			})*/
//	}

/*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)

    EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
        override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
            //Some error handling
        }

        override fun onImagesPicked(imagesFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
            //Handle the images
            //onPhotosReturned(imagesFiles)
        }
    })
}*/
/*override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
        R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
    }
}*/

	/*fun getImageUrlWithAuthority(context: Context, uri: Uri?): String? {
		Timber.d("getImageUrlWithAuthority(uri:%s)", uri)
		if(uri == null) return null
		var istream: InputStream? = null
		if (uri.getAuthority() != null) {
			try {
				istream = context.getContentResolver().openInputStream(uri)
				val bmp = BitmapFactory.decodeStream(istream)
				return writeToTempImageAndGetPathUri(context, bmp).toString()
			} catch (e: FileNotFoundException) {
				e.printStackTrace()
			} finally {
				try {
					istream!!.close()
				} catch (e: IOException) {
					e.printStackTrace()
				}
			}
		}
		return null
	}*/


	/*fun writeToTempImageAndGetPathUri(inContext: Context, inImage: Bitmap): Uri {
		Timber.d("writeToTempImageAndGetPathUri(inImage:%s)", inImage)

		// Assume thisActivity is the current activity
		val perm = Manifest.permission.WRITE_EXTERNAL_STORAGE
		val permissionCheck = ContextCompat.checkSelfPermission(this, perm)
		if(permissionCheck == PackageManager.PERMISSION_GRANTED) {

			val bytes = ByteArrayOutputStream()
			inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
			val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "tmp-img", null)
			return Uri.parse(path)
		}
		else{
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.READ_CONTACTS)) {

				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(this, arrayOf(perm),
						MY_PERMISSIONS_REQUEST_READ_CONTACTS)

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}

		}
	}

	override fun onRequestPermissionsResult(requestCode: Int,
											permissions: Array<String>, grantResults: IntArray) {
		when (requestCode) {
			MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return
			}
		}// other 'case' lines to check for other
		// permissions this app might request
	}*/
}
