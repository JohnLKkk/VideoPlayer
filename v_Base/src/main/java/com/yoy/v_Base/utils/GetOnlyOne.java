package com.yoy.v_Base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.yoy.v_Base.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public final class GetOnlyOne {

    /**
     * 未知方式
     */
    public static final int UNKNOWN = -99;

    /**
     * IMEI码
     */
    public static final int IMEI = 0;

    /**
     * MAC地址
     */
    public static final int MAC = 1;

    /**
     * 设备SN码
     */
    public static final int SN = 2;

    /**
     * 自动生成的唯一码  6位随机数 + 当前毫秒时间戳
     */
    public static final int OTHER = 3;

    /**
     * 一恒科设备SN码
     */
    public static final int SN_YHK = 4;

    /**
     * GSM序列号
     */
    public static final int SN_GSM = 5;

    /**
     * 长虹序列号
     */
    public static final int SN_CH_1 = 6;

    public static int getDeviceAuthType() {
        switch (BuildConfig.AUTH_TYPE) {
            case 0:
                return GetOnlyOne.IMEI;
            case 1:
                return GetOnlyOne.MAC;
            case 2:
                return GetOnlyOne.SN;
            default:
                return GetOnlyOne.UNKNOWN;
        }
    }

    /**
     * 获取ID
     * <p>
     * 注意：如果存在可用的 cacheFile，则会忽略请求的认证类型，优先读取内容。
     * 如果需要获取指定的类型ID，则先调用 deleteCacheFile() 方法来清楚旧的缓存文件
     *
     * @param context  可用的上下文对象
     * @param authType 认证方式 GetOnlyOne.CodeType
     * @return 获取到的ID，如果获取失败返回 NULL。  {认证类型，对应的ID}
     * @see CodeType
     */
    @SuppressLint("SwitchIntDef")
    public @Nullable
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, READ_PHONE_STATE, INTERNET, WRITE_EXTERNAL_STORAGE})
    String[] getID(Context context, @CodeType int authType) throws SecurityException {
        return _getID(context.getApplicationContext(), authType);
    }

    /**
     * @param context  可用的上下文对象
     * @param authType 认证方式 GetOnlyOne.CodeType
     * @return 获取到的ID，如果获取失败返回 NULL。  {认证类型，对应的ID}
     */
    @SuppressLint("SwitchIntDef")
    private @Nullable
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, READ_PHONE_STATE, INTERNET, WRITE_EXTERNAL_STORAGE})
    String[] _getID(Context context, @CodeType int authType) throws SecurityException {
        String keyContent;
        String keyType;
        switch (authType) {
            case GetOnlyOne.IMEI:
                keyContent = getIMEI(context);
                keyType = "IMEI";
                break;
            case GetOnlyOne.MAC:
                keyContent = getMAC(context);
                keyType = "MAC";
                break;
            case GetOnlyOne.SN:
                keyContent = getSN();
                keyType = "SN";
                break;
            case GetOnlyOne.OTHER:
                keyContent = getOtherID();
                keyType = "OTHER";
                break;
            case GetOnlyOne.SN_YHK:
                keyContent = getSN_YHK();
                keyType = "SN_YHK";
                break;
            case GetOnlyOne.SN_GSM:
                keyContent = getSN_GSM(context);
                keyType = "SN_GSM";
                break;
            case GetOnlyOne.SN_CH_1:
                keyContent = getSN_CH_CUSTOM();
                keyType = "SN_CH_1";
                break;
            default:
                return null;
        }
        keyContent = getRightUniqueValue(keyContent);
        return new String[]{
                keyType, keyContent
        };
    }

    /**
     * @return IMEI码，获取不到返回NULL
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private @Nullable
    String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getDeviceId();
    }

    /**
     * @return MAC码，获取不到返回NULL
     */
    @SuppressLint("MissingPermission")
    private @Nullable
    String getMAC(Context context) {
        try {
            return DeviceUtils.getMacAddress(context);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * @return SN码，获取不到返回NULL
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private @Nullable
    String getSN() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Build.getSerial();
//        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//            return Build.SERIAL;
        } else {
            return Build.SERIAL;
        }
    }

    /**
     * @return 返回生成的ID
     */
    private @NonNull
    String getOtherID() {
        Random random = new Random(System.currentTimeMillis());
        return String.format(Locale.CHINESE, "GL%s%s", random.nextLong(), System.currentTimeMillis());
    }

    /**
     * @return 返回一恒科SN号
     */
    private @Nullable
    @SuppressLint("PrivateApi")
    String getSN_YHK() {
        return getCustomSN();
    }

    /**
     * @return 返回长虹自定义SN号
     */
    private @Nullable
    @SuppressLint("PrivateApi")
    String getSN_CH_CUSTOM() {
        try {
            String result = getCustomSN();
            if (!TextUtils.isEmpty(result)) {
                return result.substring(0, 16);
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    /**
     * @return 返回 GSM.Serial 序列号，获取不到返回NULL
     */
    private @Nullable
    String getSN_GSM(Context context) {
        return getSystemArg(context, "gsm.serial", null);
    }

    /**
     * @return 系统自定义 SN号。存在于 ro.serialnocustom。如果没有或无法获取，则返回NULL
     */
    private @Nullable
    String getCustomSN() {
        String customSerial;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            customSerial = (String) get.invoke(c, "ro.serialnocustom");
        } catch (Exception ignore) {
            return null;
        }
        return customSerial;
    }

    /**
     * 获取系统String字段
     *
     * @param key 键值
     * @param def 如果获取失败默认返回的值
     * @return 获取得到的值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SuppressLint("PrivateApi")
    private String getSystemArg(Context context, String key, String def) {
        String ret;
        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[2];
            params[0] = key;
            params[1] = def;
            ret = (String) get.invoke(SystemProperties, params);
        } catch (Exception ignore) {
            ret = def;
        }

        return ret;
    }

    public static String getRightUniqueValue(String str) {
        try {
            String regEx = "[^(0-9)(A-Za-z):]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            String s = m.replaceAll("").replace(" ", "").toLowerCase();
            if (s.equals("unknown") || s.equals("0123456789abcdef")) {
                return null;
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 认证类型
     */
    @IntDef({UNKNOWN, IMEI, MAC, SN, OTHER, SN_YHK, SN_GSM, SN_CH_1})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CodeType {
    }

    private static final class ShellUtils {

        private static final String LINE_SEP = System.getProperty("line.separator");

        /**
         * Execute the command.
         *
         * @param command  The command.
         * @param isRooted True to use root, false otherwise.
         * @return the single {@link CommandResult} instance
         */
        public static CommandResult execCmd(final String command, final boolean isRooted) {
            return execCmd(new String[]{command}, isRooted, true);
        }

        /**
         * Execute the command.
         *
         * @param commands        The commands.
         * @param isRooted        True to use root, false otherwise.
         * @param isNeedResultMsg True to return the message of result, false otherwise.
         * @return the single {@link CommandResult} instance
         */
        public static CommandResult execCmd(final String[] commands,
                                            final boolean isRooted,
                                            final boolean isNeedResultMsg) {
            int result = -1;
            if (commands == null || commands.length == 0) {
                return new CommandResult(result, "", "");
            }
            Process process = null;
            BufferedReader successResult = null;
            BufferedReader errorResult = null;
            StringBuilder successMsg = null;
            StringBuilder errorMsg = null;
            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec(isRooted ? "su" : "sh");
                os = new DataOutputStream(process.getOutputStream());
                for (String command : commands) {
                    if (command == null) continue;
                    os.write(command.getBytes());
                    os.writeBytes(LINE_SEP);
                    os.flush();
                }
                os.writeBytes("exit" + LINE_SEP);
                os.flush();
                result = process.waitFor();
                if (isNeedResultMsg) {
                    successMsg = new StringBuilder();
                    errorMsg = new StringBuilder();
                    successResult = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
                    );
                    errorResult = new BufferedReader(
                            new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)
                    );
                    String line;
                    if ((line = successResult.readLine()) != null) {
                        successMsg.append(line);
                        while ((line = successResult.readLine()) != null) {
                            successMsg.append(LINE_SEP).append(line);
                        }
                    }
                    if ((line = errorResult.readLine()) != null) {
                        errorMsg.append(line);
                        while ((line = errorResult.readLine()) != null) {
                            errorMsg.append(LINE_SEP).append(line);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (successResult != null) {
                        successResult.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (errorResult != null) {
                        errorResult.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (process != null) {
                    process.destroy();
                }
            }
            return new CommandResult(
                    result,
                    successMsg == null ? "" : successMsg.toString(),
                    errorMsg == null ? "" : errorMsg.toString()
            );
        }

        /**
         * The result of command.
         */
        public static class CommandResult {
            public int result;
            public String successMsg;
            public String errorMsg;

            public CommandResult(final int result, final String successMsg, final String errorMsg) {
                this.result = result;
                this.successMsg = successMsg;
                this.errorMsg = errorMsg;
            }

            @NotNull
            @Override
            public String toString() {
                return "result: " + result + "\n" +
                        "successMsg: " + successMsg + "\n" +
                        "errorMsg: " + errorMsg;
            }
        }
    }

    private static final class DeviceUtils {

        /**
         * Return the MAC address.
         * <p>Must hold
         * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
         * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
         *
         * @return the MAC address
         */
        @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
        public static String getMacAddress(Context context) {
            return getMacAddress(context, (String[]) null);
        }

        /**
         * Return the MAC address.
         * <p>Must hold
         * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
         * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
         *
         * @return the MAC address
         */
        @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
        private static String getMacAddress(Context context, final String... excepts) {
            String macAddress = getMacAddressByWifiInfo(context);
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByNetworkInterface();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByInetAddress();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            macAddress = getMacAddressByFile();
            if (isAddressNotInExcepts(macAddress, excepts)) {
                return macAddress;
            }
            return "";
        }

        private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
            if (excepts == null || excepts.length == 0) {
                return !"02:00:00:00:00:00".equals(address);
            }
            for (String filter : excepts) {
                if (address.equals(filter)) {
                    return false;
                }
            }
            return true;
        }

        @SuppressLint({"HardwareIds", "MissingPermission"})
        private static String getMacAddressByWifiInfo(Context context) {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo info = wifi.getConnectionInfo();
                    if (info != null) return info.getMacAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static String getMacAddressByNetworkInterface() {
            try {
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static String getMacAddressByInetAddress() {
            try {
                InetAddress inetAddress = getInetAddress();
                if (inetAddress != null) {
                    NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                    if (ni != null) {
                        byte[] macBytes = ni.getHardwareAddress();
                        if (macBytes != null && macBytes.length > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (byte b : macBytes) {
                                sb.append(String.format("%02x:", b));
                            }
                            return sb.substring(0, sb.length() - 1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "02:00:00:00:00:00";
        }

        private static InetAddress getInetAddress() {
            try {
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    // To prevent phone of xiaomi return "10.0.2.15"
                    if (!ni.isUp()) continue;
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress inetAddress = addresses.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String hostAddress = inetAddress.getHostAddress();
                            if (hostAddress.indexOf(':') < 0) return inetAddress;
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String getMacAddressByFile() {
            ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
            if (result.result == 0) {
                String name = result.successMsg;
                if (name != null) {
                    result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                    if (result.result == 0) {
                        String address = result.successMsg;
                        if (address != null && address.length() > 0) {
                            return address;
                        }
                    }
                }
            }
            return "02:00:00:00:00:00";
        }
    }
}
