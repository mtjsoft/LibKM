package com.mtjsoft.www.kotlinmvputils.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 常用工具类
 *
 * @author mtj
 */
public class TurnsUtils {

    /**
     * String转换成int
     *
     * @param str      需要转换的数据源
     * @param defValue 默认值
     */
    public static int getInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * String转换成long
     *
     * @param str      需要转换的数据源
     * @param defValue 默认值
     */
    public static long getLong(String str, int defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * String转换成float
     *
     * @param str      需要转换的数据源
     * @param defValue 默认值
     */
    public static float getFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * String转换成double
     *
     * @param str      需要转换的数据源
     * @param defValue 默认值
     */
    public static double getDouble(String str, double defValue) {
        try {
            return Double.parseDouble(keepTwoDecimalPlaces(str));
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 获取年份
     *
     * @return
     */
    public static int getSysYear() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR);
    }

    /**
     * 把一个Date对象转换成相应格式的字符串
     *
     * @param date      时间
     * @param outFormat 输出的格式
     * @return 返回转换的字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertToString(Date date, String outFormat) {
        SimpleDateFormat format = new SimpleDateFormat(outFormat);
        return format.format(date);
    }

    /**
     * 将 字符串格式 转成 Date
     *
     * @param dateTimeStr
     * @param formatStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDate(String dateTimeStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 获取下个月的日期
     *
     * @return
     */
    public static Date nextMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }


    /**
     * 返回Uid
     *
     * @return
     */
    public static String getUid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取data是本年第几周
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        //这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        // 设置每周的第一天为星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 每周从周一开始
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // 设置每周最少为7天
        cal.setMinimalDaysInFirstWeek(7);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getFirstOfMonth(String resultDateFormat) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        return convertToString(c.getTime(), resultDateFormat);
    }

    /**
     * 获取当前月最后一天
     *
     * @return
     */
    public static String getLastOfMonth(String resultDateFormat) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return convertToString(ca.getTime(), resultDateFormat);
    }

    /**
     * 当前周的第一天
     *
     * @param resultDateFormat
     * @return
     */
    public static String getFirstOfWeek(String resultDateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        return convertToString(cal.getTime(), resultDateFormat);
    }

    /**
     * 当前周的最后一天
     *
     * @param resultDateFormat
     * @return
     * @throws ParseException
     */
    public static String getLastOfWeek(String resultDateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        return convertToString(cal.getTime(), resultDateFormat);
    }

    /**
     * 转换成倒计时,时分秒样式
     *
     * @param mss 毫秒值
     * @return
     */
    public static String getTimeString(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days > 0) {
            return days + "天 " + hours + ":" + minutes + ":" + seconds;
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String keepTwoDecimalPlaces(Double value) {
        if (value == 0) {
            return "0.00";
        }
        return keepTwoDecimalPlaces(value.toString());
    }

    public static String keepTwoDecimalPlaces(String value) {
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format((Double.valueOf(value)));
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * 最多保留两位小数，且只保留小数的有效位，即为末尾是0的不保留
     *
     * @param value
     * @return
     */
    public static String keepTwoDecimalPlacesOrNoKeep(Double value) {
        if (value == 0) {
            return "0";
        }
        return keepTwoDecimalPlacesOrNoKeep(value.toString());
    }

    public static String keepTwoDecimalPlacesOrNoKeep(String value) {
        // 递归剔除无效的小数位
        String s = keepTwoDecimalPlaces(value);
        // 包含小数点
        if (s.contains(".")) {
            // 找到小数点的位置
            int pointIndex = s.lastIndexOf(".");
            return deteleDecimalPlaces(pointIndex, s);
        } else {
            return s;
        }
    }

    /**
     * 递归剔除无效位
     * 如果小数的最后一位是0或者最后一位是小数点，就剔除最后的0和小数点
     *
     * @param value
     * @return
     */
    private static String deteleDecimalPlaces(int pointIndex, String value) {
        if (value.length() > 0) {
            int lastIndex = value.length() - 1;
            int last0 = value.lastIndexOf("0");
            // 如果小数的最后一位是0,就剔除最后的0
            if (last0 == lastIndex && last0 > pointIndex) {
                return deteleDecimalPlaces(pointIndex, value.substring(0, lastIndex));
            }
            // 如果最后一位是小数点,就剔除最后的小数点
            if (pointIndex == lastIndex) {
                return value.substring(0, lastIndex);
            }
        }
        return value;
    }

    /**
     * @param et          输入框
     * @param decimal_num 限制小数点后面的位数
     */
    public static void decimalNumber(final EditText et, final int decimal_num) {
        InputFilter lengthFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                if (dest.length() == 0 && source.equals(".")) {
                    return "0.";
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    if (dotValue.length() == decimal_num
                            && dstart > splitArray[0].length()) {
                        return "";
                    }
                }
                return null;
            }
        };
        et.setFilters(new InputFilter[]{lengthFilter});
    }

    /**
     * 判断c是否在a b范围
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

    public static boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

    public static boolean isInRange(float a, float b, float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }


    /**
     * 判断手机安装的地图APP
     *
     * @return
     */
    public static List<String> isPackageInstalledMap() {
        List<String> listMap = new ArrayList<>();
        String baiduMap = "com.baidu.BaiduMap";
        String gaodeMap = "com.autonavi.minimap";
        String tengxunMap = "com.tencent.map";
        if (new File("/data/data/" + baiduMap).exists()) {
            listMap.add("百度地图");
        }
        if (new File("/data/data/" + gaodeMap).exists()) {
            listMap.add("高德地图");
        }
        if (new File("/data/data/" + tengxunMap).exists()) {
            listMap.add("腾讯地图");
        }
        return listMap;
    }

    /**
     * Bitmap以文件File形式保存在本地
     *
     * @param bm
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
}
