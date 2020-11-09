package com.cancelik.insatagramclone.utils

import android.os.Environment
import androidx.fragment.app.Fragment
import com.cancelik.insatagramclone.profile.UploadFragment
import com.cancelik.insatagramclone.share.ShareNextFragment
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class FileOperations {
    companion object {
        fun incomingFolderFiles(folderName : String) : ArrayList<String>{
            var allFiles = ArrayList<String>()
            var file = File(folderName)
            //parametre olarak gönderdiğimiz tüm dosyaları almamız sağlanır
            var allFoldersFile = file.listFiles()
            if (allFoldersFile != null && allFoldersFile.size>0){
                //tarihlerine göre sondan başa listelemek
                if (allFoldersFile.size>1){
                    Arrays.sort(allFoldersFile, object : Comparator<File>{
                        override fun compare(o1: File?, o2: File?): Int {
                            if (o1!!.lastModified() > o2!!.lastModified()){
                                return -1
                            }
                            else{
                                return 1
                            }
                        }

                    })
                }
                for (i in 0..allFoldersFile.size-1){
                    if (allFoldersFile[i].isFile){
                        //sadece dosyalara bakıyoruz
                        //dosya yolu ve adını içeren kod
                        var learningFilePaths = allFoldersFile[i].absolutePath
                        //hangi türde geldiğine bakıyoruz
                        //noktanın geçtiği en son indexi almak istediğimi söylüyorum
                        var fileType :String? = learningFilePaths.substring(learningFilePaths.lastIndexOf("."))
                        println("Okunan dosyalarım ${learningFilePaths}")

                        if (fileType != null){
                            if (fileType.equals(".jpg") || fileType.equals(".jpeg") || fileType.equals(".png") || fileType.equals(".mp4") || fileType.equals(".3gp") || fileType.equals(".JPG")) {
                                allFiles.add(learningFilePaths)
                                println("Okunan dosyalarım ${learningFilePaths}")
                            }
                        }
                    }
                }
            }
            return allFiles
        }





    /*
        fun compressImageFile(shareNextFragment: Fragment, selectImagePath: String?) {
            runBlocking {
                    val job = launch(Dispatchers.Default) {
                        println("iş başladı")
                        var newFileFolder = File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/Test/")
                        var newFileFolderPaths = SiliCompressor.with(shareNextFragment.context)
                                .compress(selectImagePath, newFileFolder)
                        println(newFileFolder)
                        println(newFileFolderPaths)
                        (shareNextFragment as ShareNextFragment).(newFileFolderPaths)

                    }
                    job.invokeOnCompletion {
                       println("iş Bitti")
                    }
                   job.cancel()
                }


        }

     */






    }

}
