package devesh.app.database;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class DatabaseTool {

    AppDatabase db;
    UserDao userDao;

    public DatabaseTool(Context context){
         db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "ocrdb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
         userDao = db.userDao();

    }

    public List<ScanFile> getAll(){
        return userDao.getAll();
    }

    public void Add(ScanFile scanFile){
        userDao.insert(scanFile);
    }
    public void clearHistory(){
        userDao.nukeTable();
    }



}
