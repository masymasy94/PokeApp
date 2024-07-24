package com.app.pokeapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SQLiteUtils {

    public static void executeScript(Context context, int resourceId, SQLiteDatabase db) throws IOException {

        InputStream    insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader  = new BufferedReader(new InputStreamReader(insertsStream));

        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();

            if (StringUtils.isNotEmpty(insertStmt))
                db.execSQL(insertStmt);
        }

        insertReader.close();
    }
}
