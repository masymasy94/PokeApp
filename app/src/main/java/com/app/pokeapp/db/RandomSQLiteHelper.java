package com.app.pokeapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Random;
import com.app.pokeapp.utils.SQLiteUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RandomSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "randomDb";
    private static final String TABLE_RANDOM = "random";

    private Context context;
    public RandomSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANDOM);
        this.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {createTable(db);}

    private void createTable(SQLiteDatabase db) {
        try {
            SQLiteUtils.executeScript(context, R.raw.random_create_table, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertRandoms(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            SQLiteUtils.executeScript(context, R.raw.randoms_insert_1_gen, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Random> getAllRandoms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RANDOM,
                                 new String[]{"name", "pokemon", "base_power", "map_color", "first_bonus", "second_bonus", "third_bonus"},
                                 null, null,
                                 null, null, null, null);
        List<Random> all = new ArrayList<>();
        while(cursor.moveToNext()){
            all.add(getRandomFromDbCursor(cursor));
        }
        cursor.close();
        return all;
    }

    public Random getRandomByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RANDOM,
                new String[]{"name", "pokemon", "base_power", "map_color",  "first_bonus", "second_bonus", "third_bonus"},
                " name = ?", new String[]{ name },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        return getRandomFromDbCursor(cursor);
    }

    private Random getRandomFromDbCursor(Cursor cursor) {

        Random rdm = new Random();
        rdm.name = cursor.getString(0);
        rdm.pokemon = cursor.getString(1);
        rdm.basePower = Integer.parseInt(cursor.getString(2));
        rdm.mapColor = cursor.getString(3);
        rdm.firstBonus = Integer.parseInt(cursor.getString(4));
        rdm.secondBonus =Integer.parseInt(cursor.getString(5));
        rdm.thirdBonus = Integer.parseInt(cursor.getString(6));
        return rdm;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANDOM);
        this.onCreate(db);
    }
}
