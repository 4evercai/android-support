package com.i4evercai.android.support.utils

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.content.ContextCompat
import java.util.*


/**
 *
 * @Description: 跟App相关的辅助类
 * @author Fitz
 * @email FitzPro@qq.com
 * @date 2017/7/8 11:14
 * @version V1.0
 */
object AppUtils {
    lateinit var appContext: Context
    var isDebug = true

    /**
     * 获取应用程序名称
     *
     *@param[context] 上下文
     */
    @JvmStatic
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.getResources().getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 检测程序是否安装
     *
     * @param context
     * @param packageName
     *
     * @return
     */
    private fun isInstalled(context: Context, packageName: String): Boolean {
        val manager = context.getPackageManager()
        //获取所有已安装程序的包信息
        val installedPackages = manager.getInstalledPackages(0)
        if (installedPackages != null) {
            for (info in installedPackages) {
                if (info.packageName == packageName)
                    return true
            }
        }
        return false
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    @JvmStatic
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            return packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    fun getVersionCode(context: Context): Int {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            return packageInfo.versionCode

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 1
    }

    /**
     * 判断是否为主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {

        val packageName = context.getPackageName();
        val processName = getProcessName(context);
        return packageName.equals(processName)
    }

    /**
     * 获取当前进程名
     */
    @JvmStatic
    fun getProcessName(context: Context): String {

        var processName: String = ""
        val am: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        while (true) {
            for (info in am.runningAppProcesses) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }

            }
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            try {
                Thread.sleep(100L)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }


        }
        return processName
    }

    @JvmStatic
    private fun getPhoneImei(context: Context): String {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE)
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return (tm as TelephonyManager).getImei()
                } else {
                    return (tm as TelephonyManager).getDeviceId();
                }

            }

        }
        return ""

    }

    /**
     * 获取IMEI
     */
    @JvmStatic
    fun getImei(context: Context): String {
        var imei = ""
        try {
            imei = getPhoneImei(context)
        } catch (e: Exception) {

        }
        if (TextUtils.isEmpty(imei)) {
            imei = PreferenceUtils.getString(context, "sp_app_uuid", "key_mac_uuid", "")
            if (TextUtils.isEmpty(imei)) {
                imei = UUID.randomUUID().toString().replace("-", "")
                PreferenceUtils.putString(context, "sp_app_uuid", "key_mac_uuid", imei)
            }


        }
        return imei
    }

    @JvmStatic
    fun restartApp(context: Context) {
        val intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }


}