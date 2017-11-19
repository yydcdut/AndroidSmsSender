package com.yydcdut.sms

import android.content.Context
import android.text.TextUtils
import java.io.*
import java.util.regex.Pattern

/**
 * Created by yuyidong on 2017/11/12.
 */
object Utils {
    private val DIR = App.instance().cacheDir

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

    fun readIgnoreNumber(lock: Any): MutableList<String> {
        synchronized(lock) {
            return readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_NUMBER)
        }
    }

    fun saveIgnoreNumber(list: MutableList<String>, lock: Any) {
        synchronized(lock) {
            return saveIgnoreFile(DIR.absolutePath + File.separator + IGNORE_NUMBER, format(list))
        }
    }

    fun readIgnoreText(lock: Any): MutableList<String> {
        synchronized(lock) {
            return readIgnoreFile(DIR.absolutePath + File.separator + IGNORE_TEXT)
        }
    }

    fun saveIgnoreText(list: MutableList<String>, lock: Any) {
        synchronized(lock) {
            return saveIgnoreFile(DIR.absolutePath + File.separator + IGNORE_TEXT, format(list))
        }
    }

    private fun format(list: MutableList<String>): String {
        val set: HashSet<String> = HashSet(list)
        val sb = StringBuilder()
        set.filterNot { TextUtils.isEmpty(it) }.forEach { sb.append(it).append(";") }
        return sb.toString()
    }

    private fun readIgnoreFile(path: String): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
            return list
        }
        val content = readStringFromFile(file)
        val result = content.split(";")
        result.filterNotTo(list) { TextUtils.isEmpty(it) }
        return list
    }

    private fun saveIgnoreFile(path: String, content: String) {
        val file = File(path)
        if (file.exists()) {
            file.writeText(content)
        }
    }

    fun savePhone(phone: String) =
            App.instance().getSharedPreferences("sms", Context.MODE_PRIVATE).edit().putString("phone", phone).commit()

    fun getPhone(): String =
            App.instance().getSharedPreferences("sms", Context.MODE_PRIVATE).getString("phone", "")

    fun isPhone(str: String): Boolean {
        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$"
        return Pattern.compile(regExp).matcher(str).matches()
    }
}