package com.example.vijaya.androidhardware

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import java.io.IOException

class AudioRecordingActivity : AppCompatActivity() {
    private var mRecordButton: RecordButton? = null
    private var mRecorder: MediaRecorder? = null
    private var mPlayButton: PlayButton? = null
    private var mPlayer: MediaPlayer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun onPlay(start: Boolean) {
        if (start) {
            startPlaying()
        } else {
            stopPlaying()
        }
    }

    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer!!.setDataSource(mFileName)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }
    }

    private fun stopPlaying() {
        mPlayer!!.release()
        mPlayer = null
    }

    private fun startRecording() {
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder!!.setOutputFile(mFileName)
        mRecorder!!.prepare()
        // ICP Task3: Write the code to recording the Audio
        mRecorder!!.start()
    }

    private fun stopRecording() {
        mRecorder!!.stop()
        mRecorder!!.release()
        mRecorder = null
    }

    internal inner class RecordButton(ctx: Context?) : AppCompatButton(ctx) {
        var mStartRecording = true
        var clicker = OnClickListener {
            onRecord(mStartRecording)
            text = if (mStartRecording) {
                "Stop recording"
            } else {
                "Start recording"
            }
            mStartRecording = !mStartRecording
        }

        init {
            text = "Start recording"
            setOnClickListener(clicker)
        }
    }

    internal inner class PlayButton(ctx: Context?) : AppCompatButton(ctx) {
        var mStartPlaying = true
        var clicker = OnClickListener {
            onPlay(mStartPlaying)
            text = if (mStartPlaying) {
                "Stop playing"
            } else {
                "Start playing"
            }
            mStartPlaying = !mStartPlaying
        }

        init {
            text = "Start playing"
            setOnClickListener(clicker)
        }
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Record to the external cache directory for visibility
        mFileName = externalCacheDir.absolutePath
        mFileName += "/audiorecordtest.3gp"
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        val ll = LinearLayout(this)
        mRecordButton = RecordButton(this)
        ll.addView(mRecordButton,
                LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0F))
        mPlayButton = PlayButton(this)
        ll.addView(mPlayButton,
                LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0F))
        setContentView(ll)
    }

    public override fun onStop() {
        super.onStop()
        if (mRecorder != null) {
            mRecorder!!.release()
            mRecorder = null
        }
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
        }
    }

    companion object {
        private const val LOG_TAG = "AudioRecordTest"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private var mFileName: String? = null
    }
}