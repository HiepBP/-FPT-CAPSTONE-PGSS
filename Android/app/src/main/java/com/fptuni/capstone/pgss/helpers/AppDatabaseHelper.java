package com.fptuni.capstone.pgss.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.ParkingLot;
import com.fptuni.capstone.pgss.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TrungTNM on 3/7/2017.
 */

public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static AppDatabaseHelper instance;

    // Database Info
    private static final String DATABASE_NAME = "PGSS.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_TRANSACTION = "transactions";

    // Transaction Table Columns
    private static final String KEY_TRANSACTION_ID = "id";
    private static final String KEY_TRANSACTION_USERNAME = "username";
    private static final String KEY_TRANSACTION_DATE = "date";
    private static final String KEY_TRANSACTION_STATUS = "status";
    private static final String KEY_TRANSACTION_CAR_PARK_ID = "carParkId";
    private static final String KEY_TRANSACTION_LOT_ID = "lotId";
    private static final String KEY_TRANSACTION_LOT_NAME = "lotName";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";

    public static synchronized AppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION +
                " (" +
                KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY, " +
                KEY_TRANSACTION_USERNAME + " TEXT, " +
                KEY_TRANSACTION_CAR_PARK_ID + " INTEGER, " +
                KEY_TRANSACTION_LOT_ID + " INTEGER, " +
                KEY_TRANSACTION_LOT_NAME + " TEXT, " +
                KEY_TRANSACTION_DATE + " INTEGER, " +
                KEY_TRANSACTION_STATUS + " TEXT, " +
                KEY_TRANSACTION_AMOUNT + " REAL" +
                ")";

        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
            onCreate(db);
        }
    }

    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRANSACTION_USERNAME, transaction.getUsername());
            values.put(KEY_TRANSACTION_CAR_PARK_ID, transaction.getCarParkId());
            values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(KEY_TRANSACTION_DATE, transaction.getDate().getTime());
            values.put(KEY_TRANSACTION_STATUS, transaction.getStatus());

            db.insertOrThrow(TABLE_TRANSACTION, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    public void addOrUpdateTransaction(Transaction transaction) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRANSACTION_ID, transaction.getId());
            values.put(KEY_TRANSACTION_USERNAME, transaction.getUsername());
            values.put(KEY_TRANSACTION_CAR_PARK_ID, transaction.getCarParkId());
            values.put(KEY_TRANSACTION_LOT_ID, transaction.getLotId());
            values.put(KEY_TRANSACTION_LOT_NAME, transaction.getLot().getName());
            values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(KEY_TRANSACTION_DATE, transaction.getDate().getTime());
            values.put(KEY_TRANSACTION_STATUS, transaction.getStatus());

            int rows = db.update(TABLE_TRANSACTION, values,
                    KEY_TRANSACTION_ID + "= ?", new String[]{String.valueOf(transaction.getId())});
            if (rows == 0) {
                db.insertOrThrow(TABLE_TRANSACTION, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public List<Transaction> getAllTransactions(Account account) {
        List<Transaction> transactions = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_TRANSACTION, KEY_TRANSACTION_USERNAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{account.getUsername()});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction();
                    transaction.setId(cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ID)));
                    transaction.setCarParkId(cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_CAR_PARK_ID)));
                    transaction.setUsername(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_USERNAME)));
                    transaction.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_STATUS)));
                    transaction
                            .setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_TRANSACTION_AMOUNT)));
                    transaction
                            .setDate(new Date(cursor
                                    .getLong(cursor.getColumnIndex(KEY_TRANSACTION_DATE))));
                    transaction.setLotId(cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_LOT_ID)));
                    ParkingLot lot = new ParkingLot();
                    lot.setName(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_LOT_NAME)));
                    transaction.setLot(lot);

                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return transactions;
    }
}
