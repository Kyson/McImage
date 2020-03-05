package com.smallsoho.mcplugin.image.utils

import com.smallsoho.mcplugin.image.Config
import java.io.File

class CompressUtil {

    companion object {
        private const val TAG = "Compress"
        fun compressImg(imgFile: File,mcImageConfig: Config) {
            if (!ImageUtil.isImage(imgFile)) {
                return
            }
            val oldSize = imgFile.length()
            val newSize: Long
            if (ImageUtil.isJPG(imgFile)) {
                val tempFilePath: String = "${imgFile.path.substring(0, imgFile.path.lastIndexOf("."))}_temp" +
                        imgFile.path.substring(imgFile.path.lastIndexOf("."))
                Tools.cmd("guetzli", "--quality ${mcImageConfig.guetzliQuality} ${imgFile.path} $tempFilePath")
                val tempFile = File(tempFilePath)
                newSize = tempFile.length()
                LogUtil.log("newSize = $newSize")
                if (newSize < oldSize) {
                    val imgFileName: String = imgFile.path
                    if (imgFile.exists()) {
                        imgFile.delete()
                    }
                    tempFile.renameTo(File(imgFileName))
                } else {
                    if (tempFile.exists()) {
                        tempFile.delete()
                    }
                }
            } else {
                Tools.cmd("pngquant", "--skip-if-larger --quality=${mcImageConfig.pngquantQuality} --speed 1 --nofs --strip --force --output ${imgFile.path} -- ${imgFile.path}")
                newSize = File(imgFile.path).length()
            }

            LogUtil.log(TAG, imgFile.path, oldSize.toString(), newSize.toString())
        }
    }

}