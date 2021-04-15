package com.example.vijaya.androidhardware

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

class StorageActivity : AppCompatActivity() {
    var txt_content: EditText? = null
    var contenttoDisplay: EditText? = null
    var FILENAME = "MyAppStorage.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        txt_content = findViewById<View>(R.id.id_txt_mycontent) as EditText
        contenttoDisplay = findViewById<View>(R.id.id_txt_display) as EditText
    }

    @Throws(IOException::class)
    fun saveTofile(v: View?) {

        // ICP Task4: Write the code to save the text
        var text = ""
        val file= File(this.filesDir,"Maneesha")
        if (!file.exists()){
            file.mkdir()
        } else {
            val path= "${this.filesDir}/Maneesha/$FILENAME"
            val filePath = File(path)
            if(filePath.exists()) {
                text = filePath.bufferedReader().use {
                    it.readText()
                }
            }
        }
        try {
            val textFile=File(file,FILENAME)
            val writer=FileWriter(textFile)
            writer.append(text.plus(" ").plus(txt_content!!.text.toString()))
            writer.flush()
            writer.close()
            Toast.makeText(this,"Saved successfully",Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){

        }
    }

    @Throws(IOException::class)
    fun retrieveFromFile(v: View?) {

        // ICP Task4: Write the code to display the above saved text
        val path= "${this.filesDir}/Maneesha/$FILENAME"
        val file = File(path)
        if(file.exists()){
            val text : String = file.bufferedReader().use {
                it.readText()
            }
            txt_content!!.setText("")
            contenttoDisplay!!.setText(text)
        }




    }
}