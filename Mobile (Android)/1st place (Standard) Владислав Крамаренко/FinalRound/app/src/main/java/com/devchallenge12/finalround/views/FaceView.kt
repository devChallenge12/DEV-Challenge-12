package com.devchallenge12.finalround.views

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.devchallenge12.finalround.Constants
import com.devchallenge12.finalround.utils.Utils
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.Landmark
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FaceView(context: Context, attrs: AttributeSet) : View(context, attrs){

    companion object {
        var debug = true
        var effectX = -1
        var effectY = -1
        var scaleFactor = 1f
    }

     var effect: Bitmap? = null

    private var mBitmap: Bitmap? = null
    private var mFaces: SparseArray<Face>? = null
    private val TAG = this::class.java.simpleName
    /**
     * Sets the bitmap background and the associated face detections.
     */
    internal fun setContent(bitmap: Bitmap, faces: SparseArray<Face>) {
        mBitmap = bitmap
        mFaces = faces

        invalidate()
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mBitmap != null && mFaces != null) {
            val scale = drawBitmap(canvas)
            drawFaceAnnotations(canvas, scale)
        }

    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private fun drawBitmap(canvas: Canvas): Double {
        val viewWidth = canvas.width.toDouble()
        val viewHeight = canvas.height.toDouble()
        val imageWidth = mBitmap!!.width.toDouble()
        val imageHeight = mBitmap!!.height.toDouble()
        val scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight)

        val destBounds = Rect(0, 0, (imageWidth * scale).toInt(), (imageHeight * scale).toInt())
        canvas.drawBitmap(mBitmap!!, null, destBounds, null)
        return scale
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     *
     *
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private fun drawFaceAnnotations(canvas: Canvas, scale: Double) {
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f


        for (i in 0 until mFaces!!.size()) {
            val face = mFaces!!.valueAt(i)

            var bottomMouth: Landmark? = null
            var leftMouth: Landmark? = null
            var rightMouth: Landmark? = null
            var rightEye: Landmark? = null
            var leftEye: Landmark? = null
            var baseNose: Landmark? = null

            for (landmark in face.landmarks) {
                when(landmark.type){
                    Landmark.NOSE_BASE -> baseNose = landmark
                    Landmark.LEFT_EYE -> leftEye = landmark
                    Landmark.RIGHT_EYE -> rightEye = landmark
                    Landmark.LEFT_MOUTH -> leftMouth = landmark
                    Landmark.RIGHT_MOUTH -> rightMouth = landmark
                    Landmark.BOTTOM_MOUTH -> bottomMouth = landmark
                }

                if(debug) {
                    val cx = (landmark.position.x * scale).toInt()
                    val cy = (landmark.position.y * scale).toInt()
                    canvas.drawCircle(cx.toFloat(), cy.toFloat(), 10f, paint)
                }
            }

            if(effectX == -1 && effectY == -1) {
                when (Utils.mode) {
                    Constants.MUSTACHE_MODE -> {
                        val b = BitmapFactory.decodeResource(context.resources, Utils.imagesRes)
                        val width = ((leftMouth!!.position.x * scale - rightMouth!!.position.x * scale) * 1.5).toInt()
                        effect = Bitmap.createScaledBitmap(b, width, (b.height * (width.toFloat() / b.width.toFloat())).toInt(), false)
                        mustacheMode(canvas, effect!!, baseNose!!, scale, paint)
                    }

                    Constants.GLASSES_MODE -> {
                        val b = BitmapFactory.decodeResource(context.resources, Utils.imagesRes)
                        val width = (((leftEye!!.position.x * scale - rightEye!!.position.x * scale) * 1.5) + (leftEye.position.x * scale - rightEye.position.x * scale) / 2).toInt()
                        effect = Bitmap.createScaledBitmap(b, width, (b.height * (width.toFloat() / b.width.toFloat())).toInt(), false)
                        glassesMode(canvas, effect!!, rightEye, baseNose!!, scale, paint)
                    }

                    Constants.LIPS_MODE -> {
                        val b = BitmapFactory.decodeResource(context.resources, Utils.imagesRes)
                        val width = (leftMouth!!.position.x * scale - rightMouth!!.position.x * scale).toInt()
                        effect = Bitmap.createScaledBitmap(b, width, (b.height * (width.toFloat() / b.width.toFloat())).toInt(), false)
                        lipsMode(canvas, effect!!, bottomMouth!!, scale, paint)
                    }
                }
            } else canvas.drawBitmap(effect, (effectX).toFloat(), (effectY).toFloat(), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(effect != null) {
            effectX = event!!.x.toInt()
            effectY = event.y.toInt()
            effectX -= effect!!.width / 2
            effectY -= effect!!.height / 2
            invalidate()
        }

        Log.d(TAG, effectX.toString() + " " + effectY + " " + scaleFactor)
        return super.onTouchEvent(event)
    }

    fun save(){
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)

        val file = File(Environment.getExternalStorageDirectory().toString()  + File.separator + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()) + ".jpg")

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun lipsMode(canvas: Canvas, lips: Bitmap, bottomMouth: Landmark, scale: Double, paint: Paint) {
        canvas.drawBitmap(lips, (bottomMouth.position.x*scale - lips.width/2).toFloat(), (bottomMouth.position.y * scale - lips.height).toFloat(), paint)
    }

    private fun glassesMode(canvas: Canvas, glasses: Bitmap, rightEye: Landmark, baseNose: Landmark, scale: Double, paint: Paint) {
        canvas.drawBitmap(glasses, (baseNose.position.x*scale - glasses.width/2).toFloat(), (rightEye.position.y * scale - glasses.height/2).toFloat(), paint)
    }

    private fun mustacheMode(canvas: Canvas, mustache: Bitmap, noseBaseLandmark: Landmark, scale: Double, paint: Paint){
        canvas.drawBitmap(mustache, (noseBaseLandmark.position.x*scale - mustache.width/2).toFloat(), (noseBaseLandmark.position.y * scale).toFloat(), paint)
    }
}