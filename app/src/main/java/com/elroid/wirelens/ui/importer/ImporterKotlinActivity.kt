package com.elroid.wirelens.ui.importer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.elroid.wirelens.domain.DataManager
import com.elroid.wirelens.ui.base.BaseActivity
import com.elroid.wirelens.util.FileUtils
import timber.log.Timber

/**
 *
 * Class: com.elroid.wirelens.ui.importer.ImporterActivity
 * Project: WireLens
 * Created Date: 17/01/2018 18:19
 *
 * @author <a href="mailto:e@elroid.com">Elliot Long</a>
 *         Copyright (c) 2018 Elroid Ltd. All rights reserved.
 */
class ImporterKotlinActivity : BaseActivity() {

    companion object i {
        fun createIntent(ctx: Context): Intent {
            return Intent(ctx, ImporterKotlinActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the intent that started this activity
        val intent: Intent? = getIntent()
        Timber.d("got intent: %s", intent)
        if (intent != null) {
            val imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
            Timber.d("Got imageUri: %s", imageUri)
            if (imageUri != null) {
                val file = FileUtils.pickedExistingPicture(this, imageUri)
                Timber.d("Got file: %s", file)

                //todo import?

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
            else toast("no imageUri!");
        }
        else toast("no intent!");
    }
}