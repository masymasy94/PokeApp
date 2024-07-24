package com.app.pokeapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.utils.PokemonTypesUtils;
import com.app.pokeapp.utils.SQLiteUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PokemonSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pokeDb";
    private static final String TABLE_POKEMON = "pokemon";

    private Context context;

    public PokemonSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createTable(SQLiteDatabase db){
        try {
            SQLiteUtils.executeScript(context, R.raw.pokemon_create_table, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertFirstGen(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            SQLiteUtils.executeScript(context, R.raw.pokemon_insert_1_gen, db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEMON);
        this.onCreate(db);
    }

    public void dropPokemonTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEMON);
        this.onCreate(db);
    }



//    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    List<String> booleanTrueValues = Arrays.asList("1", "S");

    public List<Pokemon> getAllPokemonFromDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POKEMON,
                new String[]{"id", "name", "strenght", "type", "second_type", "is_evolved", "is_two_times_evolved", "move"},
                null, null,
                null, null, null, null);

        List<Pokemon> all = new ArrayList<>();
        while(cursor.moveToNext()){
            all.add(getPokemonFromDbCursor(cursor));
        }

        cursor.close();
        return all;
    }

    private Pokemon getPokemonFromDbCursor(Cursor cursor) {
        Pokemon pkm = new Pokemon();
        pkm.id = Integer.parseInt(cursor.getString(0));
        pkm.name = cursor.getString(1);
        pkm.strenght = cursor.getInt(2);
        pkm.types.add(0, PokemonTypesUtils.getValueOrNull(cursor.getString(3)));
        pkm.isEvolved = booleanTrueValues.contains(StringUtils.trimToEmpty(cursor.getString(5)));
        pkm.isEvolvedTwoTimes = booleanTrueValues.contains(StringUtils.trimToEmpty(cursor.getString(6)));
        pkm.move = cursor.getString(7);
        String secondType = cursor.getString(4);
        if (StringUtils.isNotEmpty(secondType)){
            pkm.types.add(1, PokemonTypesUtils.getValueOrNull(secondType));
        }
        return pkm;
    }


    public Pokemon getPokemonByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POKEMON,
                new String[]{"id", "name", "strenght", "type", "second_type", "is_evolved", "is_two_times_evolved", "move"},
                " name = ?", new String[]{ name },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        return getPokemonFromDbCursor(cursor);
    }
}