package com.nickmonks.Util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nickmonks.data.TaskDao;
import com.nickmonks.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class}) // with this annotation we simply pass the methods and room will know how to manage it.
public abstract class TaskRoomDatabase extends RoomDatabase {

    public static final int NUMBER_OF_THREADS = 4;
    private static volatile TaskRoomDatabase INSTANCE;
    public static final String DATABASE_NAME = "todoister_database";
    public static final ExecutorService datbaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    datbaseWriterExecutor.execute(() -> {
                        // invoke DAO, and write
                        TaskDao taskDao = INSTANCE.taskDao();
                        taskDao.deleteAll(); // clean slate!

                        // writing to our table

                    });
                }
            };


    public static TaskRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class){
                if (INSTANCE == null ){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract TaskDao taskDao();
}
