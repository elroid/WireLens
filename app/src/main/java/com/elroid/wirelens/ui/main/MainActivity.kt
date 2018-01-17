package com.elroid.wirelens.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.elroid.wirelens.util.FileUtils
import com.elroid.wirelens.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
            //EasyImage.openGallery(this, 0)
            val photoPickerIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            photoPickerIntent.type = "image/*"
            val INTENT_REQUEST_CODE_SELECT_PHOTO = 0
            startActivityForResult(photoPickerIntent, INTENT_REQUEST_CODE_SELECT_PHOTO)
        }

        // Get the intent that started this activity
        val intent: Intent? = getIntent()
        Timber.d("got intent: %s", intent)
        if (intent != null) {
            val imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
            Timber.d("Got imageUri: %s", imageUri)
            if (imageUri != null) {
                val file = FileUtils.pickedExistingPicture(this, imageUri)
                Timber.d("Got file: %s", file)
                /*val imageLoader = ImageLoader.getInstance() // Get singleton instance

                // Load image, decode it to Bitmap and return Bitmap to callback
                imageLoader.loadImage(imageUri.toString(), object : SimpleImageLoadingListener() {
                    override fun onLoadingComplete(imageUri: String, view: View?, loadedImage: Bitmap) {
                        // Do whatever you want with Bitmap
                        Timber.i("Got bitmap: %s", loadedImage)
                    }

                    override fun onLoadingStarted(imageUri: String?, view: View?) {
                        Timber.d("Loading started: %s", imageUri)
                        super.onLoadingStarted(imageUri, view)
                    }

                    override fun onLoadingCancelled(imageUri: String?, view: View?) {
                        Timber.d("Loading cancelled: %s", imageUri)
                        super.onLoadingCancelled(imageUri, view)
                    }

                    override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                        Timber.d("Loading failed: %s", failReason)
                        super.onLoadingFailed(imageUri, view, failReason)
                    }
                })*/
            }
            /*val data: Uri? = intent.getData()
            val clipData: ClipData? = intent.clipData
            //val item = clip?.getItemAt(0)

            // Tries to get the item's contents as a URI pointing to a note
            //val uri = item?.uri
            //Timber.d("Got uri: %s", uri)

            val files = ArrayList<File>()

            val imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
            Timber.d("Got imageUri: %s", imageUri)

            if (clipData != null) {
                for (i in 0 until clipData.getItemCount()) {
                    val uri = clipData.getItemAt(i).getUri()
                    val file = FileUtils.pickedExistingPicture(this, uri)
                    files.add(file)
                }
            } else if(data != null){
                val file = FileUtils.pickedExistingPicture(this, data)
                files.add(file)
            }

            Timber.d("Got file paths: %s", files)*/

            /*val filePath = getImageUrlWithAuthority(this, data)
            Timber.d("Got file path: %s", filePath)*/

            // Figure out what to do based on the intent type
            /*if (intent.type?.indexOf("image/") != -1) {
                // Handle intents with image data ...
                Timber.d("got image from intent: %s", data)
                val filePath = getImageUrlWithAuthority(this, data)
                Timber.d("Got file path: %s", filePath)
            } else *//*if (intent.getType()?.equals("text/plain"))*//* {
                // Handle intents with text ...
                Timber.d("got text from intent: %s", data)
            }*/
        }
    }


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
