package com.app.pokeapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Challenger;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.utils.PokemonTypesUtils;
import com.app.pokeapp.utils.SQLiteUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChallengerSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "challengerDb";
    private static final String TABLE_CHALLENGER = "challenger";

    private Context context;
    public ChallengerSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGER);
        this.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {createTable(db);}

    private void createTable(SQLiteDatabase db) {
        try {
            SQLiteUtils.executeScript(context, R.raw.challengers_create_table, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insert(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            SQLiteUtils.executeScript(context, R.raw.challenger_insert_1_gen, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Challenger> getAllChallengers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHALLENGER,
                                 new String[]{"name", "pokemon", "base_power", "first_bonus", "second_bonus", "third_bonus"},
                                 null, null,
                                 null, null, null, null);
        List<Challenger> all = new ArrayList<>();
        while(cursor.moveToNext()){
            all.add(getChallengerFromDbCursor(cursor));
        }
        cursor.close();
        return all;
    }

    public Challenger getChallengerByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHALLENGER,
                new String[]{"name", "pokemon", "base_power", "first_bonus", "second_bonus", "third_bonus"},
                " name = ?", new String[]{ name },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        return getChallengerFromDbCursor(cursor);
    }

    private Challenger getChallengerFromDbCursor(Cursor cursor) {

        Challenger cha = new Challenger();
        cha.name = cursor.getString(0);
        cha.pokemon = cursor.getString(1);
        cha.basePower = Integer.parseInt(cursor.getString(2));
        cha.firstBonus = Integer.parseInt(cursor.getString(3));
        cha.secondBonus =Integer.parseInt(cursor.getString(4));
        cha.thirdBonus = Integer.parseInt(cursor.getString(5));
        return cha;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGER);
        this.onCreate(db);
    }
}
