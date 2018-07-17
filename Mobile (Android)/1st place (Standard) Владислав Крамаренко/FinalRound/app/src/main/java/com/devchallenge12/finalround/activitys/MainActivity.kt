package com.devchallenge12.finalround.activitys

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.vision.face.FaceDetector
import com.google.android.gms.vision.Frame
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ScaleGestureDetector
import com.devchallenge12.finalround.Constants
import com.devchallenge12.finalround.callbacks.OnEffectSelectedCallback
import com.devchallenge12.finalround.R
import com.devchallenge12.finalround.SafeFaceDetector
import com.devchallenge12.finalround.adapter.EffectsAdapter
import com.devchallenge12.finalround.views.FaceView
import android.view.MotionEvent
import android.widget.CompoundButton


class MainActivity : AppCompatActivity(), OnEffectSelectedCallback {
    override fun onSelected() {
        face_view.invalidate()
        FaceView.effectX = -1
        FaceView.effectY = -1
        FaceView.scaleFactor = 1f
        face_view.effect?.recycle()
    }
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun userInterfaceInit(){
        val effects = ArrayList<Int>()
        effects.add(R.drawable.glasses1)
        effects.add(R.drawable.glasses2)
        effects.add(R.drawable.glasses3)
        effects.add(R.drawable.glasses4)
        effects.add(R.drawable.glasses5)
        effects.add(R.drawable.glasses6)
        effects.add(R.drawable.glasses7)
        effects.add(R.drawable.glasses8)
        effects.add(R.drawable.glasses9)
        effects.add(R.drawable.glasses10)
        effects.add(R.drawable.glasses11)
        effects.add(R.drawable.mustache1)
        effects.add(R.drawable.mustache2)
        effects.add(R.drawable.mustache3)
        effects.add(R.drawable.mustache4)
        effects.add(R.drawable.mustache6)
        effects.add(R.drawable.lips1)
        effects.add(R.drawable.lips2)
        effects.add(R.drawable.lips3)

        recyclerView.adapter = EffectsAdapter(effects, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        camera.setOnClickListener {
            startCameraIntent()
        }

        gallery.setOnClickListener {
            getImageFromGalery()
        }


        save.setOnClickListener{
            face_view.save()
        }

        debug.setOnCheckedChangeListener { _, isChecked ->
            FaceView.debug = isChecked
            face_view.invalidate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            userInterfaceInit()
        }
    }

    private fun startCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Constants.REQUEST_TAKE_PHOTO)
    }

    private fun getImageFromGalery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra("return-data", true)
        startActivityForResult(intent, Constants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode == Constants.REQUEST_TAKE_PHOTO || requestCode == Constants.PICK_IMAGE) && data != null){
            if(requestCode == Constants.PICK_IMAGE) {
                try {
                    val imageUri = data.data
                    val imageStream = contentResolver.openInputStream(imageUri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    faceViewInit(selectedImage)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else{
                faceViewInit(data.extras.get("data") as Bitmap)
            }
        }
    }

    private fun faceViewInit(bitmap: Bitmap){
        val detector = FaceDetector.Builder(applicationContext)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()

        val safeDetector = SafeFaceDetector(detector)
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val faces = safeDetector.detect(frame)

        face_view.setContent(bitmap, faces)

        safeDetector.release()
    }

    private fun checkPermissions(vararg permissions: String) = ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(ev)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            if(face_view.effect != null) {
                if(detector.scaleFactor > 0 && detector.scaleFactor < 2) {
                    FaceView.scaleFactor = detector.scaleFactor
                    val bitmap = face_view.effect

                    val ratio = (face_view.effect!!.width/face_view.effect!!.height)

                    Log.d(TAG, ratio.toString())
                    face_view.effect = Bitmap.createScaledBitmap(face_view.effect, (face_view.effect!!.width * FaceView.scaleFactor).toInt(), (face_view.effect!!.width/ratio * FaceView.scaleFactor).toInt(), false)
                    face_view.invalidate()
                    bitmap?.recycle()
                }
            }
            return true
        }
    }


}
