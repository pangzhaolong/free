package com.donews.network.room.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.donews.network.room.bean.DownloadInfo;

import java.util.List;

/**
 * DownloadInfo Dao接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/8 17:19
 */
@Dao
public interface DownloadInfoDao {

    @Insert(onConflict = REPLACE)
    Long insertDownloadInfo(DownloadInfo downloadInfo);

    @Delete
    void deleteDownloadInfo(DownloadInfo downloadInfo);

    @Update
    void uploadDownloadInfo(DownloadInfo downloadInfo);

    @Query("SELECT *  FROM download_info WHERE url = :url AND save_path = :savePath AND file_suffix = :fileSuffix " +
            "LIMIT 1")
    DownloadInfo queryDownloadInfo(String url, String savePath, String fileSuffix);

    @Query("SELECT * FROM download_info")
    List<DownloadInfo> getAllDownloadInfo();
}
