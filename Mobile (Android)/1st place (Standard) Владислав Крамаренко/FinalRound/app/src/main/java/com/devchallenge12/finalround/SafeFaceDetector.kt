package com.devchallenge12.finalround



import android.graphics.ImageFormat
import android.util.Log
import android.util.SparseArray

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face

import java.nio.ByteBuffer
import java.util.Arrays

/**
 * Created at the base of https://github.com/googlesamples/android-vision/blob/master/visionSamples/photo-demo/app/src/main/java/com/google/android/gms/samples/vision/face/patch/SafeFaceDetector.java
 */
class SafeFaceDetector(private val mDelegate: Detector<Face>) : Detector<Face>() {

    override fun release() {
        mDelegate.release()
    }

    /**
     * Determines whether the supplied image may cause a problem with the underlying face detector.
     * If it does, padding is added to the image in order to avoid the issue.
     */
    override fun detect(frame: Frame): SparseArray<Face> {
        var frame = frame
        val kMinDimension = 147
        val kDimensionLower = 640
        val width = frame.metadata.width
        val height = frame.metadata.height

        if (height > 2 * kDimensionLower) {
            // The image will be scaled down before detection is run.  Check to make sure that this
            // won't result in the width going below the minimum
            val multiple = height.toDouble() / kDimensionLower.toDouble()
            val lowerWidth = Math.floor(width.toDouble() / multiple)
            if (lowerWidth < kMinDimension) {
                // The width would have gone below the minimum when downsampling, so apply padding
                // to the right to keep the width large enough.
                val newWidth = Math.ceil(kMinDimension * multiple).toInt()
                frame = padFrameRight(frame, newWidth)
            }
        } else if (width > 2 * kDimensionLower) {
            // The image will be scaled down before detection is run.  Check to make sure that this
            // won't result in the height going below the minimum
            val multiple = width.toDouble() / kDimensionLower.toDouble()
            val lowerHeight = Math.floor(height.toDouble() / multiple)
            if (lowerHeight < kMinDimension) {
                val newHeight = Math.ceil(kMinDimension * multiple).toInt()
                frame = padFrameBottom(frame, newHeight)
            }
        } else if (width < kMinDimension) {
            frame = padFrameRight(frame, kMinDimension)
        }

        return mDelegate.detect(frame)
    }

    override fun isOperational(): Boolean {
        return mDelegate.isOperational
    }

    override fun setFocus(id: Int): Boolean {
        return mDelegate.setFocus(id)
    }

    /**
     * Creates a new frame based on the original frame, with additional width on the right to
     * increase the size to avoid the bug in the underlying face detector.
     */
    private fun padFrameRight(originalFrame: Frame, newWidth: Int): Frame {
        val metadata = originalFrame.metadata
        val width = metadata.width
        val height = metadata.height

        Log.i(TAG, "Padded image from: " + width + "x" + height + " to " + newWidth + "x" + height)

        val origBuffer = originalFrame.grayscaleImageData
        val origOffset = origBuffer.arrayOffset()
        val origBytes = origBuffer.array()

        // This can be changed to just .allocate in the future, when Frame supports non-direct
        // byte buffers.
        val paddedBuffer = ByteBuffer.allocateDirect(newWidth * height)
        val paddedOffset = paddedBuffer.arrayOffset()
        val paddedBytes = paddedBuffer.array()
        Arrays.fill(paddedBytes, 0.toByte())

        for (y in 0 until height) {
            val origStride = origOffset + y * width
            val paddedStride = paddedOffset + y * newWidth
            System.arraycopy(origBytes, origStride, paddedBytes, paddedStride, width)
        }

        return Frame.Builder()
                .setImageData(paddedBuffer, newWidth, height, ImageFormat.NV21)
                .setId(metadata.id)
                .setRotation(metadata.rotation)
                .setTimestampMillis(metadata.timestampMillis)
                .build()
    }

    /**
     * Creates a new frame based on the original frame, with additional height on the bottom to
     * increase the size to avoid the bug in the underlying face detector.
     */
    private fun padFrameBottom(originalFrame: Frame, newHeight: Int): Frame {
        val metadata = originalFrame.metadata
        val width = metadata.width
        val height = metadata.height

        Log.i(TAG, "Padded image from: " + width + "x" + height + " to " + width + "x" + newHeight)

        val origBuffer = originalFrame.grayscaleImageData
        val origOffset = origBuffer.arrayOffset()
        val origBytes = origBuffer.array()

        // This can be changed to just .allocate in the future, when Frame supports non-direct
        // byte buffers.
        val paddedBuffer = ByteBuffer.allocateDirect(width * newHeight)
        val paddedOffset = paddedBuffer.arrayOffset()
        val paddedBytes = paddedBuffer.array()
        Arrays.fill(paddedBytes, 0.toByte())

        // Copy the image content from the original, without bothering to fill in the padded bottom
        // part.
        for (y in 0 until height) {
            val origStride = origOffset + y * width
            val paddedStride = paddedOffset + y * width
            System.arraycopy(origBytes, origStride, paddedBytes, paddedStride, width)
        }

        return Frame.Builder()
                .setImageData(paddedBuffer, width, newHeight, ImageFormat.NV21)
                .setId(metadata.id)
                .setRotation(metadata.rotation)
                .setTimestampMillis(metadata.timestampMillis)
                .build()
    }

    companion object {
        private val TAG = "SafeFaceDetector"
    }
}