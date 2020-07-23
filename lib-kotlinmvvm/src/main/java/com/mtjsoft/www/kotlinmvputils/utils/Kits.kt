package com.mtjsoft.www.kotlinmvputils.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wanglei on 2016/11/28.
 */

class Kits {

    object Package {
        /**
         * 获取版本号
         *
         * @param context
         * @return
         */
        fun getVersionCode(context: Context): Int {
            val pManager = context.packageManager
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = pManager.getPackageInfo(context.packageName, 0)

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return packageInfo!!.versionCode
        }

        /**
         * 获取当前版本
         *
         * @param context
         * @return
         */
        fun getVersionName(context: Context): String {
            val pManager = context.packageManager
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = pManager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return packageInfo!!.versionName
        }

        /**
         * 安装App
         *
         * @param context
         * @param filePath
         * @return
         */
        fun installNormal(context: Context, filePath: String): Boolean {
            val i = Intent(Intent.ACTION_VIEW)
            val file = java.io.File(filePath)
            if (file == null || !file.exists() || !file.isFile || file.length() <= 0) {
                return false
            }

            i.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.android.package-archive")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
            return true
        }

        /**
         * 卸载App
         *
         * @param context
         * @param packageName
         * @return
         */
        fun uninstallNormal(context: Context, packageName: String?): Boolean {
            if (packageName == null || packageName.length == 0) {
                return false
            }

            val i = Intent(Intent.ACTION_DELETE, Uri.parse(StringBuilder().append("package:")
                    .append(packageName).toString()))
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
            return true
        }

        /**
         * 判断是否是系统App
         *
         * @param context
         * @param packageName 包名
         * @return
         */
        fun isSystemApplication(context: Context?, packageName: String?): Boolean {
            if (context == null) {
                return false
            }
            val packageManager = context.packageManager
            if (packageManager == null || packageName == null || packageName.length == 0) {
                return false
            }

            try {
                val app = packageManager.getApplicationInfo(packageName, 0)
                return app != null && app.flags and ApplicationInfo.FLAG_SYSTEM > 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return false
        }

        /**
         * 获取Meta-Data
         *
         * @param context
         * @param key
         * @return
         */
        fun getAppMetaData(context: Context?, key: String): String? {
            if (context == null || TextUtils.isEmpty(key)) {
                return null
            }
            var resultData: String? = null
            try {
                val packageManager = context.packageManager
                if (packageManager != null) {
                    val applicationInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                    if (applicationInfo != null) {
                        if (applicationInfo.metaData != null) {
                            resultData = applicationInfo.metaData.getString(key)
                        }
                    }

                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return resultData
        }

        /**
         * 判断当前应用是否运行在后台
         *
         * @param context
         * @return
         */
        fun isApplicationInBackground(context: Context): Boolean {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val taskList = am.getRunningTasks(1)
            if (taskList != null && !taskList.isEmpty()) {
                val topActivity = taskList[0].topActivity
                if (topActivity != null && topActivity.packageName != context.packageName) {
                    return true
                }
            }
            return false
        }
    }


    object Dimens {
        fun dpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }

        fun pxToDp(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        fun dpToPxInt(context: Context, dp: Float): Int {
            return (dpToPx(context, dp) + 0.5f).toInt()
        }

        fun pxToDpCeilInt(context: Context, px: Float): Int {
            return (pxToDp(context, px) + 0.5f).toInt()
        }
    }


    object Random {
        val NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val NUMBERS = "0123456789"
        val LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"

        fun getRandomNumbersAndLetters(length: Int): String? {
            return getRandom(NUMBERS_AND_LETTERS, length)
        }

        fun getRandomNumbers(length: Int): String? {
            return getRandom(NUMBERS, length)
        }

        fun getRandomLetters(length: Int): String? {
            return getRandom(LETTERS, length)
        }

        fun getRandomCapitalLetters(length: Int): String? {
            return getRandom(CAPITAL_LETTERS, length)
        }

        fun getRandomLowerCaseLetters(length: Int): String? {
            return getRandom(LOWER_CASE_LETTERS, length)
        }

        fun getRandom(source: String, length: Int): String? {
            return if (TextUtils.isEmpty(source)) null else getRandom(source.toCharArray(), length)
        }

        fun getRandom(sourceChar: CharArray?, length: Int): String? {
            if (sourceChar == null || sourceChar.size == 0 || length < 0) {
                return null
            }

            val str = StringBuilder(length)
            val random = java.util.Random()
            for (i in 0 until length) {
                str.append(sourceChar[random.nextInt(sourceChar.size)])
            }
            return str.toString()
        }

        fun getRandom(max: Int): Int {
            return getRandom(0, max)
        }

        fun getRandom(min: Int, max: Int): Int {
            if (min > max) {
                return 0
            }
            return if (min == max) {
                min
            } else min + java.util.Random().nextInt(max - min)
        }
    }

    object File {
        val FILE_EXTENSION_SEPARATOR = "."

        /**
         * read file
         *
         * @param filePath
         * @param charsetName The name of a supported [&lt;/code&gt;charset&lt;code&gt;][java.nio.charset.Charset]
         * @return if file not exist, return null, else return content of file
         * @throws RuntimeException if an error occurs while operator BufferedReader
         */
        fun readFile(filePath: String, charsetName: String): StringBuilder? {
            val file = java.io.File(filePath)
            val fileContent = StringBuilder("")
            if (file == null || !file.isFile) {
                return null
            }

            var reader: BufferedReader? = null
            try {
                val `is` = InputStreamReader(FileInputStream(file), charsetName)
                reader = BufferedReader(`is`)
                var line: String? = null
                while (reader.readLine() != null) {
                    line = reader.readLine()
                    if (fileContent.toString() != "") {
                        fileContent.append("\r\n")
                    }
                    fileContent.append(line)
                }
                return fileContent
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                IO.close(reader)
            }
        }

        /**
         * write file
         *
         * @param filePath
         * @param content
         * @param append   is append, if true, write to the end of file, else clear content of file and write into it
         * @return return false if content is empty, true otherwise
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        @JvmOverloads
        fun writeFile(filePath: String, content: String, append: Boolean = false): Boolean {
            if (TextUtils.isEmpty(content)) {
                return false
            }

            var fileWriter: FileWriter? = null
            try {
                makeDirs(filePath)
                fileWriter = FileWriter(filePath, append)
                fileWriter.write(content)
                return true
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                IO.close(fileWriter)
            }
        }

        /**
         * write file
         *
         * @param filePath
         * @param contentList
         * @param append      is append, if true, write to the end of file, else clear content of file and write into it
         * @return return false if contentList is empty, true otherwise
         * @throws RuntimeException if an error occurs while operator FileWriter
         */
        @JvmOverloads
        fun writeFile(filePath: String, contentList: List<String>?, append: Boolean = false): Boolean {
            if (contentList == null || contentList.isEmpty()) {
                return false
            }

            var fileWriter: FileWriter? = null
            try {
                makeDirs(filePath)
                fileWriter = FileWriter(filePath, append)
                var i = 0
                for (line in contentList) {
                    if (i++ > 0) {
                        fileWriter.write("\r\n")
                    }
                    fileWriter.write(line)
                }
                return true
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                IO.close(fileWriter)
            }
        }

        /**
         * write file
         *
         * @param stream the input stream
         * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
         * @return return true
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        @JvmOverloads
        fun writeFile(filePath: String?, stream: InputStream, append: Boolean = false): Boolean {
            return writeFile(if (filePath != null) java.io.File(filePath) else null, stream, append)
        }

        /**
         * write file
         *
         * @param file   the file to be opened for writing.
         * @param stream the input stream
         * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
         * @return return true
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        @JvmOverloads
        fun writeFile(file: java.io.File?, stream: InputStream, append: Boolean = false): Boolean {
            var o: OutputStream? = null
            try {
                makeDirs(file!!.absolutePath)
                o = FileOutputStream(file, append)
                val data = ByteArray(1024)
                var length = -1
                while (stream.read(data) != -1) {
                    length = stream.read(data)
                    o.write(data, 0, length)
                }
                o.flush()
                return true
            } catch (e: FileNotFoundException) {
                throw RuntimeException("FileNotFoundException occurred. ", e)
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            } finally {
                IO.close(o)
                IO.close(stream)
            }
        }

        /**
         * move file
         *
         * @param sourceFilePath
         * @param destFilePath
         */
        fun moveFile(sourceFilePath: String, destFilePath: String) {
            if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
                throw RuntimeException("Both sourceFilePath and destFilePath cannot be null.")
            }
            moveFile(java.io.File(sourceFilePath), java.io.File(destFilePath))
        }

        /**
         * move file
         *
         * @param srcFile
         * @param destFile
         */
        fun moveFile(srcFile: java.io.File, destFile: java.io.File) {
            val rename = srcFile.renameTo(destFile)
            if (!rename) {
                copyFile(srcFile.absolutePath, destFile.absolutePath)
                deleteFile(srcFile.absolutePath)
            }
        }

        /**
         * copy file
         *
         * @param sourceFilePath
         * @param destFilePath
         * @return
         * @throws RuntimeException if an error occurs while operator FileOutputStream
         */
        fun copyFile(sourceFilePath: String, destFilePath: String): Boolean {
            var inputStream: InputStream? = null
            try {
                inputStream = FileInputStream(sourceFilePath)
            } catch (e: FileNotFoundException) {
                throw RuntimeException("FileNotFoundException occurred. ", e)
            }

            return writeFile(destFilePath, inputStream)
        }

        /**
         * get file name from path, not include suffix
         *
         *
         * <pre>
         * getFileNameWithoutExtension(null)               =   null
         * getFileNameWithoutExtension("")                 =   ""
         * getFileNameWithoutExtension("   ")              =   "   "
         * getFileNameWithoutExtension("abc")              =   "abc"
         * getFileNameWithoutExtension("a.mp3")            =   "a"
         * getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
         * getFileNameWithoutExtension("c:\\")              =   ""
         * getFileNameWithoutExtension("c:\\a")             =   "a"
         * getFileNameWithoutExtension("c:\\a.b")           =   "a"
         * getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
         * getFileNameWithoutExtension("/home/admin")      =   "admin"
         * getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
        </pre> *
         *
         * @param filePath
         * @return file name from path, not include suffix
         * @see
         */
        fun getFileNameWithoutExtension(filePath: String): String? {
            if (TextUtils.isEmpty(filePath)) {
                return filePath
            }

            val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
            val filePosi = filePath.lastIndexOf(java.io.File.separator)
            if (filePosi == -1) {
                return if (extenPosi == -1) filePath else filePath.substring(0, extenPosi)
            }
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1)
            }
            return if (filePosi < extenPosi) filePath.substring(filePosi + 1, extenPosi) else filePath.substring(filePosi + 1)
        }

        /**
         * get file name from path, include suffix
         *
         *
         * <pre>
         * getFileName(null)               =   null
         * getFileName("")                 =   ""
         * getFileName("   ")              =   "   "
         * getFileName("a.mp3")            =   "a.mp3"
         * getFileName("a.b.rmvb")         =   "a.b.rmvb"
         * getFileName("abc")              =   "abc"
         * getFileName("c:\\")              =   ""
         * getFileName("c:\\a")             =   "a"
         * getFileName("c:\\a.b")           =   "a.b"
         * getFileName("c:a.txt\\a")        =   "a"
         * getFileName("/home/admin")      =   "admin"
         * getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
        </pre> *
         *
         * @param filePath
         * @return file name from path, include suffix
         */
        fun getFileName(filePath: String): String? {
            if (TextUtils.isEmpty(filePath)) {
                return filePath
            }

            val filePosi = filePath.lastIndexOf(java.io.File.separator)
            return if (filePosi == -1) filePath else filePath.substring(filePosi + 1)
        }

        /**
         * get folder name from path
         *
         *
         * <pre>
         * getFolderName(null)               =   null
         * getFolderName("")                 =   ""
         * getFolderName("   ")              =   ""
         * getFolderName("a.mp3")            =   ""
         * getFolderName("a.b.rmvb")         =   ""
         * getFolderName("abc")              =   ""
         * getFolderName("c:\\")              =   "c:"
         * getFolderName("c:\\a")             =   "c:"
         * getFolderName("c:\\a.b")           =   "c:"
         * getFolderName("c:a.txt\\a")        =   "c:a.txt"
         * getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
         * getFolderName("/home/admin")      =   "/home"
         * getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
        </pre> *
         *
         * @param filePath
         * @return
         */
        fun getFolderName(filePath: String): String? {

            if (TextUtils.isEmpty(filePath)) {
                return filePath
            }

            val filePosi = filePath.lastIndexOf(java.io.File.separator)
            return if (filePosi == -1) "" else filePath.substring(0, filePosi)
        }

        /**
         * get suffix of file from path
         *
         *
         * <pre>
         * getFileExtension(null)               =   ""
         * getFileExtension("")                 =   ""
         * getFileExtension("   ")              =   "   "
         * getFileExtension("a.mp3")            =   "mp3"
         * getFileExtension("a.b.rmvb")         =   "rmvb"
         * getFileExtension("abc")              =   ""
         * getFileExtension("c:\\")              =   ""
         * getFileExtension("c:\\a")             =   ""
         * getFileExtension("c:\\a.b")           =   "b"
         * getFileExtension("c:a.txt\\a")        =   ""
         * getFileExtension("/home/admin")      =   ""
         * getFileExtension("/home/admin/a.txt/b")  =   ""
         * getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
        </pre> *
         *
         * @param filePath
         * @return
         */
        fun getFileExtension(filePath: String): String? {
            if (TextUtils.isEmpty(filePath)) {
                return filePath
            }

            val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
            val filePosi = filePath.lastIndexOf(java.io.File.separator)
            if (extenPosi == -1) {
                return ""
            }
            return if (filePosi >= extenPosi) "" else filePath.substring(extenPosi + 1)
        }

        /**
         * Creates the directory named by the trailing filename of this file, including the complete directory path required
         * to create this directory. <br></br>
         * <br></br>
         *
         * **Attentions:**
         *  * makeDirs("C:\\Users\\Trinea") can only create users folder
         *  * makeFolder("C:\\Users\\Trinea\\") can create Trinea folder
         *
         *
         * @param filePath
         * @return true if the necessary directories have been created or the target directory already exists, false one of
         * the directories can not be created.
         *
         *  * if [File.getFolderName] return null, return false
         *  * if target directory already exists, return true
         *
         */
        fun makeDirs(filePath: String): Boolean {
            val folderName = getFolderName(filePath)
            if (TextUtils.isEmpty(folderName)) {
                return false
            }

            val folder = java.io.File(folderName!!)
            return folder.exists() && folder.isDirectory || folder.mkdirs()
        }

        /**
         * @param filePath
         * @return
         * @see .makeDirs
         */
        fun makeFolders(filePath: String): Boolean {
            return makeDirs(filePath)
        }

        /**
         * Indicates if this file represents a file on the underlying file system.
         *
         * @param filePath
         * @return
         */
        fun isFileExist(filePath: String): Boolean {
            if (TextUtils.isEmpty(filePath)) {
                return false
            }

            val file = java.io.File(filePath)
            return file.exists() && file.isFile
        }

        /**
         * Indicates if this file represents a directory on the underlying file system.
         *
         * @param directoryPath
         * @return
         */
        fun isFolderExist(directoryPath: String): Boolean {
            if (TextUtils.isEmpty(directoryPath)) {
                return false
            }

            val dire = java.io.File(directoryPath)
            return dire.exists() && dire.isDirectory
        }

        /**
         * delete file or directory
         *
         *  * if path is null or empty, return true
         *  * if path not exist, return true
         *  * if path exist, delete recursion. return true
         *
         *
         * @param path
         * @return
         */
        fun deleteFile(path: String): Boolean {
            if (TextUtils.isEmpty(path)) {
                return true
            }

            val file = java.io.File(path)
            if (!file.exists()) {
                return true
            }
            if (file.isFile) {
                return file.delete()
            }
            if (!file.isDirectory) {
                return false
            }
            for (f in file.listFiles()) {
                if (f.isFile) {
                    f.delete()
                } else if (f.isDirectory) {
                    deleteFile(f.absolutePath)
                }
            }
            return file.delete()
        }

        /**
         * get file size
         *
         *  * if path is null or empty, return -1
         *  * if path exist and it is a file, return file size, else return -1
         *
         *
         * @param path
         * @return returns the length of this file in bytes. returns -1 if the file does not exist.
         */
        fun getFileSize(path: String): Long {
            if (TextUtils.isEmpty(path)) {
                return -1
            }

            val file = java.io.File(path)
            return if (file.exists() && file.isFile) file.length() else -1
        }

    }
    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link .writeFile
     */
    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link .writeFile
     */

    object IO {
        /**
         * 关闭流
         *
         * @param closeable
         */
        fun close(closeable: Closeable?) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    throw RuntimeException("IOException occurred. ", e)
                }

            }
        }
    }

    object Date {
        private val m = SimpleDateFormat("MM", Locale.getDefault())
        private val d = SimpleDateFormat("dd", Locale.getDefault())
        private val md = SimpleDateFormat("MM-dd", Locale.getDefault())
        private val ymd = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val ymdDot = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        private val ymdhms = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        private val ymdhmss = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        private val ymdhm = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        private val hm = SimpleDateFormat("HH:mm", Locale.getDefault())
        private val mdhm = SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
        private val mdhmLink = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

        /**
         * 年月日[2015-07-28]
         *
         * @param timeInMills
         * @return
         */
        fun getYmd(timeInMills: Long): String {
            return ymd.format(java.util.Date(timeInMills))
        }

        /**
         * 年月日[2015.07.28]
         *
         * @param timeInMills
         * @return
         */
        fun getYmdDot(timeInMills: Long): String {
            return ymdDot.format(java.util.Date(timeInMills))
        }

        fun getYmdhms(timeInMills: Long): String {
            return ymdhms.format(java.util.Date(timeInMills))
        }

        fun getYmdhmsS(timeInMills: Long): String {
            return ymdhmss.format(java.util.Date(timeInMills))
        }

        fun getYmdhm(timeInMills: Long): String {
            return ymdhm.format(java.util.Date(timeInMills))
        }

        fun getHm(timeInMills: Long): String {
            return hm.format(java.util.Date(timeInMills))
        }

        fun getMd(timeInMills: Long): String {
            return md.format(java.util.Date(timeInMills))
        }

        fun getMdhm(timeInMills: Long): String {
            return mdhm.format(java.util.Date(timeInMills))
        }

        fun getMdhmLink(timeInMills: Long): String {
            return mdhmLink.format(java.util.Date(timeInMills))
        }

        fun getM(timeInMills: Long): String {
            return m.format(java.util.Date(timeInMills))
        }

        fun getD(timeInMills: Long): String {
            return d.format(java.util.Date(timeInMills))
        }

        /**
         * 是否是今天
         *
         * @param timeInMills
         * @return
         */
        fun isToday(timeInMills: Long): Boolean {
            val dest = getYmd(timeInMills)
            val now = getYmd(Calendar.getInstance().timeInMillis)
            return dest == now
        }

        /**
         * 是否是同一天
         *
         * @param aMills
         * @param bMills
         * @return
         */
        fun isSameDay(aMills: Long, bMills: Long): Boolean {
            val aDay = getYmd(aMills)
            val bDay = getYmd(bMills)
            return aDay == bDay
        }

        /**
         * 获取年份
         *
         * @param mills
         * @return
         */
        fun getYear(mills: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mills
            return calendar.get(Calendar.YEAR)
        }

        /**
         * 获取月份
         *
         * @param mills
         * @return
         */
        fun getMonth(mills: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mills
            return calendar.get(Calendar.MONTH) + 1
        }


        /**
         * 获取月份的天数
         *
         * @param mills
         * @return
         */
        fun getDaysInMonth(mills: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mills

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            when (month) {
                Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> return 31
                Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> return 30
                Calendar.FEBRUARY -> return if (year % 4 == 0) 29 else 28
                else -> throw IllegalArgumentException("Invalid Month")
            }
        }


        /**
         * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
         *
         * @param mills
         * @return
         */
        fun getWeek(mills: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mills

            return calendar.get(Calendar.DAY_OF_WEEK) - 1
        }

        /**
         * 获取当月第一天的时间（毫秒值）
         *
         * @param mills
         * @return
         */
        fun getFirstOfMonth(mills: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = mills
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            return calendar.timeInMillis
        }

    }


    object Empty {
        fun check(obj: Any?): Boolean {
            return obj == null
        }

        fun check(list: List<*>?): Boolean {
            return list == null || list.isEmpty()
        }

        fun check(array: Array<Any>?): Boolean {
            return array == null || array.size == 0
        }

        fun check(str: String?): Boolean {
            return str == null || "" == str
        }

        fun check(map: Map<*, *>?): Boolean {
            return map == null || map.isEmpty()
        }

        fun check(set: Set<*>?): Boolean {
            return set == null || set.isEmpty()
        }
    }


}
