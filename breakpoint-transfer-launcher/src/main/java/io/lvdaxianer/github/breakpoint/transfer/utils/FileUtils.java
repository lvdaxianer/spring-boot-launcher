package io.lvdaxianer.github.breakpoint.transfer.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * 文件操作工具类
 * 提供文件创建、删除、合并等常用操作
 * 核心方法已委托给 Apache Commons IO 和 JDK NIO
 *
 * @author lvdaxianer
 */
public class FileUtils {

    /**
     * 创建目录事件
     * 支持创建多级目录，如果父目录不存在会自动创建
     *
     * @param dirPath 目录名称
     * @author lihh
     */
    public static void mkDir(String dirPath) {
        if (dirPath == null || dirPath.isBlank()) {
            return;
        }
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            throw new RuntimeException("创建目录失败: " + dirPath, e);
        }
    }

    /**
     * 获取文件名称 不包括后缀
     *
     * @param filename 文件名称
     * @return 文件名称 不包括后缀
     * @author lihh
     */
    public static String getNameExcludeExt(String filename) {
        if (filename == null) {
            return null;
        }
        return FilenameUtils.getBaseName(filename);
    }

    /**
     * 删除存在的目录 以及文件
     * 如果删除失败会记录日志并继续执行
     *
     * @param fileOrDirPath 文件或是目的地址
     * @return 删除成功返回 true，失败返回 false
     * @author lihh
     */
    public static boolean deleteIfExists(String fileOrDirPath) {
        if (fileOrDirPath == null || fileOrDirPath.isBlank()) {
            return false;
        }
        // Apache Commons IO 的 deleteQuietly 会忽略异常，返回 boolean
        return org.apache.commons.io.FileUtils.deleteQuietly(new File(fileOrDirPath));
    }

    /**
     * 删除目录列表，只删除列表，不包括目录本身
     *
     * @param dirPath 目录地址
     * @author lihh
     */
    public static void deleteDirectoryListing(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            try {
                org.apache.commons.io.FileUtils.cleanDirectory(dir);
            } catch (IOException e) {
                throw new RuntimeException("清理目录失败: " + dirPath, e);
            }
        }
    }

    /**
     * 多个 path 拼接
     *
     * @param paths 多个目录
     * @return 返回拼接的 url
     * @author lihh
     */
    public static String joinPath(String... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }
        // 使用 Path.of 进行跨平台路径拼接
        return Path.of("").resolve(Path.of("", paths)).toString();
    }

    /**
     * 读取文件列表
     * 对分片文件按照索引号进行排序，支持格式：filename-index
     *
     * @param dirPath 目录地址
     * @return 返回排序后的文件数组，如果目录不存在或为空返回 null
     * @author lihh
     */
    public static File[] readDirectoryListing(String dirPath) {
        File file = new File(dirPath);

        if (!file.isDirectory() || !file.exists()) {
            return null;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        // 为了防止文件顺序乱了 这里进行强制排序
        java.util.Arrays.sort(files, (o1, o2) -> {
            try {
                String[] p1Arr = o1.getName().split("-"), p2Arr = o2.getName().split("-");
                if (p1Arr.length < 2 || p2Arr.length < 2) {
                    // 如果格式不符合预期，按文件名排序
                    return o1.getName().compareTo(o2.getName());
                }
                int lastP1 = Integer.parseInt(p1Arr[1]);
                int lastP2 = Integer.parseInt(p2Arr[1]);
                return lastP1 - lastP2;
            } catch (NumberFormatException e) {
                // 解析失败时按文件名排序
                return o1.getName().compareTo(o2.getName());
            }
        });

        return files;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 返回 boolean 状态
     * @author lihh
     */
    public static boolean isFileExist(String filePath) {
        return Files.isRegularFile(Paths.get(filePath));
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节），文件不存在返回 -1
     * @author lvdaxianer
     */
    public static long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 获取目录大小
     * 递归计算目录下所有文件的总大小
     *
     * @param dirPath 目录路径
     * @return 目录总大小（字节），目录不存在返回 -1
     * @author lvdaxianer
     */
    public static long getDirectorySize(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return -1;
        }
        return org.apache.commons.io.FileUtils.sizeOf(dir);
    }

    /**
     * 写文件的事件
     * 如果文件已存在会自动覆盖
     *
     * @param outputFilePath 输出的文件地址
     * @param inputStream   输入流
     * @author lvdaxianer
     */
    public static void writeFile(String outputFilePath, InputStream inputStream) throws IOException {
        Files.copy(inputStream, Paths.get(outputFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 使用零拷贝 splice 方式合并多个文件
     * 通过 FileChannel.transferTo 实现高效数据传输，无需经过用户态缓冲区
     *
     * @param files          文件数组，文件需按顺序排列
     * @param outputFilePath 合并后的文件路径
     * @return 合并成功返回 true，失败返回 false
     * @throws IOException IO异常
     * @author lvdaxianer
     */
    public static boolean mergeFile(File[] files, String outputFilePath) throws IOException {
        try (FileChannel outputChannel = FileChannel.open(Paths.get(outputFilePath),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (File sourceFile : files) {
                if (!sourceFile.exists()) {
                    return false;
                }
                try (FileChannel inputChannel = FileChannel.open(sourceFile.toPath(), StandardOpenOption.READ)) {
                    long transferred = 0;
                    long fileSize = inputChannel.size();
                    while (transferred < fileSize) {
                        transferred += inputChannel.transferTo(transferred, fileSize - transferred, outputChannel);
                    }
                }
            }
        }
        return true;
    }
}
