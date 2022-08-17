package devesh.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface UserDao {
    @Query("SELECT * FROM scanfile ORDER BY time desc")
    List<ScanFile> getAll();

/*
    @Query("SELECT * FROM ScanFile WHERE uid IN (:ScanFileIds)")
    List<ScanFile> loadAllByIds(int[] ScanFileIds);

    @Query("SELECT * FROM ScanFile WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    ScanFile findByName(String first, String last);
*/

    @Insert
    void insertAll(ScanFile... ScanFiles);

    @Insert
    void insert(ScanFile ScanFiles);

    @Delete
    void delete(ScanFile ScanFile);

    @Query("DELETE FROM scanfile")
    void nukeTable();
}