package com.rmicro.launcher.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tim on 16-12-14.
 * 文件管理
 */
public class FileUtil {

    private final static String TAG = "FileUtil";
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
    //外部存储
    public static String T_FLASH_PATH = "/storage/sdcard1";
    //内部存储
    public static String EMMC_FLASH_PATH = "/storage/sdcard0";
    //外部存储路径
    public String T_FLASH_ROOT;

    public static File getTFlashCardDirFile(String parentDirName, String dirName) {
        File dirFile = new File(getStorageDir(parentDirName), dirName);
        if (!dirFile.exists()) {
            boolean mkFlag = dirFile.mkdirs();
            LogUtil.d(TAG,"getTFlashCardDirFile创建文件夹 " + mkFlag);
        }
        return dirFile;
    }

    public static File getStorageDir(String dirString) {
        File dir = new File(T_FLASH_PATH, dirString);
        if (!dir.exists()) {
            boolean mkFlag = dir.mkdirs();
            LogUtil.d(TAG,"getStorageDir创建文件夹 " + mkFlag);
        }
        return dir;
    }

    //获取TF卡路径
    public static String gettFlashPath(){
        return T_FLASH_PATH;
    }

    //判断TF卡是否存在//
    public static boolean isTFlashCardExists() {
        return testNewTfFile(T_FLASH_PATH);
    }

    public static boolean testNewTfFile(String filePath) {
        File testFile = new File(filePath, UUID.randomUUID().toString());
        boolean returnFlag = false;
        if (!testFile.exists()) {
            try {
                if (testFile.createNewFile()) {
                    returnFlag = true;
                    testFile.delete();
                }
            } catch (IOException e) {
                returnFlag = false;
            }
        } else {
            testFile.delete();
            returnFlag = true;
        }
        return returnFlag;
    }

    public static List<String> getDirFileNameList(String dir, String startString) {
        File dirFile = new File(dir);
        List<String> fileNameList = new ArrayList<String>();
        if (dirFile.isDirectory()) {
            File[] fileArray = dirFile.listFiles();
            for (File file : fileArray) {
                if (file.getName().startsWith(startString) && !file.getName().equals("")) {
                    fileNameList.add(file.getName());
                }
            }
        }
        return fileNameList;
    }

    public static String fileByte2Mb(double size) {
        double mbSize = size / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(mbSize);
    }

    public static String fileByte2Kb(double size) {
        double mbSize = size / 1024;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(mbSize);
    }


    public static String getFileSizeKb(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists()) {
            return fileByte2Kb(file.length());
        } else
            return "0";
    }

    public static String getFileSizeMb(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists()) {
            return fileByte2Mb(file.length());
        } else
            return "0";
    }

    /*
    * ×××××××内部存储与外部存储容量大小计算方法×××××××××××
    * */
    //方法1：
    //获取TF卡总容量大小 MB单位
    public static float getTFlashCardSpaceMbFloat() {
        return Float.valueOf(getTFlashCardSpaceMb());
    }
    public static double getTFlashCardSpace() {
        if (isTFlashCardExists()) {
            File tfCard = new File(T_FLASH_PATH);
            return tfCard.getTotalSpace();
        } else {
            return 0;
        }
    }

    public static String getTFlashCardSpaceMb() {
        return fileByte2Mb(getTFlashCardSpace());
    }

    /**
     * 获取TF卡剩余空间 MB单位
     */
    public static float getTFlashCardFreeSpaceMbFloat() {
        return Float.valueOf(fileByte2Mb(getTFlashCardFreeSpace()));
    }

    public static double getTFlashCardFreeSpace() {
        File tfCard = new File(T_FLASH_PATH);
        return tfCard.getFreeSpace();
    }

    //方法2：GB数，MB数需要x1024
    /**
     * 内置EMMC总存储空间GB数
     */
    public static String getInteralFlashTotalMemorySize() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = Environment.getExternalStorageDirectory().getPath();// 获取数据目录
        StatFs stat = getStatFs(path);
        float len = getSDAllSize(stat);
        String re_time = null;
        if(len != 0){
            re_time = df.format((double)len);
        }
        return re_time;

    }

    /**
     *
     * @return 内置EMMC剩余存储空间GB数--返回String//
     */
    public static String getEmmcFlashFreeMemorySizeGBString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = Environment.getExternalStorageDirectory().getPath();// 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInGB(stat);
        String re_time = null;
        if (len != 0) {
            re_time = df.format((double) len);
        }
        return re_time;

    }
    /**
     *
     * @return 内置EMMC剩余存储空间GB数
     */
    public static float getEmmcFlashFreeMemorySizeGB() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = Environment.getExternalStorageDirectory().getPath();// 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInGB(stat);
        return len;
//        String re_time = null;
//        if (len != 0) {
//            re_time = df.format((double) len);
//        }
//        return re_time;

    }

    /**
     *
     * @return 外置TF卡剩余存储空间GB数
     */
    public static float getTFlashFreeMemorySizeGB() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = T_FLASH_PATH; // 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInGB(stat);
        return len;
//        String re_time = null;
//        if(len != 0){
//            re_time = df.format((double)len);
//        }
//        return re_time;

    }

    /**
     *
     * @return 外置TF卡剩余存储空间GB数 返回String
     */
    public static String getTFlashFreeMemorySizeGBString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = T_FLASH_PATH; // 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInGB(stat);
        String re_time = null;
        if(len != 0){
            re_time = df.format((double)len);
        }
        return re_time;
    }

    /**
     *
     * @return 内置EMMC剩余存储空间GB数
     */
    public static float getEmmcFlashFreeMemorySizeMB() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = Environment.getExternalStorageDirectory().getPath();// 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInMB(stat);
        return len;
//        String re_time = null;
//        if (len != 0) {
//            re_time = df.format((double) len);
//        }
//        return re_time;
    }

    /**
     *
     * @return 外置TF卡剩余存储空间MB数
     */
    public static float getTFlashFreeMemorySizeMB() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = T_FLASH_PATH; // 获取数据目录
        StatFs stat = getStatFs(path);
        float len = calculateSizeInMB(stat);
        LogUtil.d(TAG,"getTFlashFreeMemorySizeMB : " + len);
        return len;
//        String re_time = null;
//        if(len != 0){
//            re_time = df.format((double)len);
//        }
//        return re_time;

    }

    /**
     *
     * @return 外置SDCard总存储空间GB数
     */
    public static String getTFlashTotalMemorySize() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String path = T_FLASH_PATH; // 获取数据目录
        StatFs stat = getStatFs(path);
        float len = getSDAllSize(stat);
        String re_time = null;
        if(len != 0){
            re_time = df.format((double)len);
        }
        return re_time;

    }

    /**
     * 获取sd的大小
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return allBlocks * blockSize; // 单位Byte
    }

    /**
     * 获取sd卡剩余容量
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return freeBlocks * blockSize; // 单位Byte
        // return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    /**
     *
     * @param path
     *            文件路径
     * @return 文件路径的StatFs对象
     * @throws Exception
     *             路径为空或非法异常抛出
     */
    public static StatFs getStatFs(String path) {
        try {
            return new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param stat
     *            文件StatFs对象
     * @return 剩余存储空间的GB数
     *
     */
    public static float calculateSizeInGB(StatFs stat) {
        if (stat != null)
            return stat.getAvailableBlocks()
                    * (stat.getBlockSize() / (1024f * 1024f * 1024f));//GB
        return 0.0f;
    }

    /**
     *
     * @param stat
     *            文件StatFs对象
     * @return 剩余存储空间的GB数
     *
     */
    public static float calculateSizeInMB(StatFs stat) {
        if (stat != null)
            return stat.getAvailableBlocks()
                    * (stat.getBlockSize() / (1024f * 1024f));//MB
        return 0.0f;
    }
    /**
     * 剩余存储空间的byte数
     * @param stat
     * @return
     */
    public static long calculateSizeInByte(StatFs stat) {
        if (stat != null){
            // 获取单个数据块的大小(Byte)
            long blockSize = stat.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = stat.getAvailableBlocks();
            return blockSize * freeBlocks ;
        }
        return 0;
    }

    /**
     * 存储空间的 总GB数
     */
    public static float getSDAllSize(StatFs stat) {
        if (stat != null)
            return stat.getBlockCount() * (stat.getBlockSize() / (1024f * 1024f * 1024f));//GB
        return 0.0f;
    }
    /**
     * 存储空间的总byte数
     */
    public static long getSDAllByteSize(StatFs stat) {
        if (stat != null){
            // 获取单个数据块的大小(Byte)
            long blockSize = stat.getBlockSize();
            // 获取所有数据块数
            long allBlocks = stat.getBlockCount();
            return blockSize * allBlocks;
        }
        return 0;
    }

    /*
    * ×××××××××××××××文件管理接口 //删除拷贝/ ××××××××××××××××
    * */
    //删除文件
    private static void delectAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    delectAllFiles(f);
                } else {
                    if (f.exists()) { // 判断是否存在
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    public static void clearLostDirFolder() {
        if (isTFlashCardExists()) {
            File root = new File(T_FLASH_PATH, "LOST.DIR");
            if (root != null && root.exists()) {
                delectAllFiles(root);
            }
        }
    }

    public static boolean copyFileAndCreateDir(InputStream assetFile, String path, String name) {
        createDirAtDir(path);
        File file = new File(path, name);
        return copyFileToSd(assetFile, file);
    }

    /**
     * 复制文件
     *
     * @param sdFile :目标文件
     * @params assetFile :被复制文件的输入流
     */
    public static boolean copyFileToSd(InputStream assetFile, File sdFile) {
        boolean flags = false;
        try {
            FileOutputStream fos = new FileOutputStream(sdFile);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = assetFile.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            flags = true;
            fos.close();
            assetFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flags;
    }

    public static boolean createDirAtDir(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            LogUtil.d(TAG,"创建文件夹 " + dirPath + "：{} " + directory.mkdirs());
        }
        if (directory.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean createDirAtDir(String dir, String dirPath) {
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + dir + File.separator + dirPath;
        File directory = new File(path);
        if (!directory.exists()) {
            LogUtil.d(TAG,"创建文件夹：{}" + directory.mkdirs());
        }
        if (directory.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String createSdcardDir(String dir) {
        File file = new File(Environment.getExternalStorageDirectory(), dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File createFileInSDCard(String fileName, String dir, int type)
            throws IOException {

        File file = new File(Environment.getExternalStorageDirectory() + dir + File.separator + fileName);

        if(!file.exists()) {
            LogUtil.d(TAG,"创建新的文本文件。");
            file.createNewFile();
            //RmUtils.sendMediaUpdateMessgae(file.toString());//
        }

        return file;
    }


    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (file.exists() && file.canRead()) {
                String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
                File tmp = new File(tmpPath);
                Log.e("FileManagerUtil 删除文件 :", filePath + "");
                if (file.renameTo(tmp)) {
                    return tmp.delete();
                } else {
                    return file.delete();
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean detectFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 返回内部存储路径
     */
    public static String getEmmcPath() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 判断内部存储是否存在
     */
    public static boolean isEmmcCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return true;
        } else {
            return false;
        }
    }
}