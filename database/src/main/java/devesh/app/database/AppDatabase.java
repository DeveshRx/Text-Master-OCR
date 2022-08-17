package devesh.app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScanFile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}