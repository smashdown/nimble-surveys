package com.nimble.surveys.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object TestFileUtils {

    @Throws(Exception::class)
    fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
//        while ((line = reader.readLine()) != null) {
//        }
        for (line in reader.lines()) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    @Throws(Exception::class)
    fun getStringFromFile(context: Context, filePath: String): String {
        val stream = context.getResources().getAssets().open(filePath)

        val ret = convertStreamToString(stream)
        //Make sure you close all streams.
        stream.close()
        return ret
    }
}