package com.mtjsoft.www.kotlinmvputils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndFileUtils {

    public static File saveBitmap(Bitmap bm, String path, String picName) {
        File file = null;
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(path, picName + ".JPEG");
            file = f;
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createSDDir(String path) throws IOException {
        File dir = new File(path);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir(path);
        }
        dir.delete();
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 通过图片的存储路径生成图片的base64
     * 并循环压缩到100K以下
     *
     * @param imgPath
     * @return
     */
    public static String imgToBase64(String imgPath) {
        String resultData = "";
        ByteArrayOutputStream out = null;
        Bitmap bitmap = null;
        int degree = 0;
        try {
            if (imgPath != null && imgPath.length() > 0) {
                //获得图片的宽和高，但并不把图片加载到内存当中
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgPath, options);
                options.inSampleSize = computeSampleSize(options, -1, (int) (0.5 * 1024 * 1024));
                //使用获取到的inSampleSize再次解析图片
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(imgPath, options);
                // 获取图片旋转角度
                degree = getBitmapDegree(imgPath);
            }
            if (bitmap != null) {
                // 角度大于0时，旋转纠正角度
                if (degree > 0) {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degree, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
                // 质量压缩从50%开始,每次都减少10,最小压缩到10%
                int options = 50;
                out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
                while (out.toByteArray().length / 1024 > 100 && options > 10) {// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    options -= 10;// 每次都减少10
                    out.reset(); // 重置 out清空
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);// 这里压缩options%，把压缩后的数据存放到out中
                }
                resultData = "data:image/jpeg;base64," + Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);//此处的NO_WRAP为：略去所有的换行符
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultData;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 计算压缩的比例
     *
     * @param options        解析图片所需的BitmapFactory.Options
     * @param minSideLength  调整后图片最小的宽或高值,一般赋值为 -1
     * @param maxNumOfPixels 调整后图片的内存占用量上限
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 计算原始大小
     *
     * @param options        解析图片所需的BitmapFactory.Options
     * @param minSideLength  调整后图片最小的宽或高值,一般赋值为 -1
     * @param maxNumOfPixels 调整后图片的内存占用量上限
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
