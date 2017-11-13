package com.yydcdut.sms

import android.os.Environment
import android.util.Log
import java.io.*

/**
 * Created by yuyidong on 2017/11/12.
 */
object Utils {
    val DIR = Environment.getDataDirectory()

    private val IGNORE_NUMBER = "ignore_number"
    private val IGNORE_TEXT = "ignore_text"

    private fun readStringFromFile(file: File?): String {
        if (file != null && file.exists() && file.isFile && file.length() > 0) {
            val fis = FileInputStream(file)
            val s = readStringFromInputStream(fis)
            closeStream(fis)
            return s
        }
        return ""
    }

    private fun readStringFromInputStream(inputStream: InputStream?): String {
        if (inputStream == null) return ""
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        while (true) {
            val len = inputStream.read(buffer)
            if (len != -1) bos.write(buffer, 0, len)
            else break
        }
        val s = String(bos.toByteArray())
        closeStream(bos)
        return s
    }

    private fun closeStream(closeable: Closeable?) {
        closeable?.close()
    }

    fun addIgnoreNumber(number: String) {

    }

    fun readIgnoreNumber(): MutableList<String> = readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_NUMBER)

    fun readIgnoreText(): MutableList<String> = readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_TEXT)

    private fun readIgnoreFile(path: String): MutableList<String> {
        Log.i("yuyidong", "readIgnoreFile-->" + path)
        val list: MutableList<String> = mutableListOf()
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
            return list
        }
        val content = readStringFromFile(file)
        val result = content.split(";")
        list += result
        return list
    }

}