package com.donews.network.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.donews.network.room.bean.DownloadInfo;
import com.donews.network.room.dao.DownloadInfoDao;

/**
 * 网络模块使用的数据库,现用于保存下载文件信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/8 17:06
 */
@Database(entities = {DownloadInfo.class}, version = 1, exportSchema = true)
public abstract class NetworkDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "network_database";
    private static volatile NetworkDatabase INSTANCE;

    public static NetworkDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized(NetworkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NetworkDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @return 返回DownloadInfo 表操作接口
     */
    public abstract DownloadInfoDao getDownloadInfoDao();

}
