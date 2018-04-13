package com.alextroy.udacitydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper dbHelper;
    private Button insertDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InventoryDbHelper(this);
        insertDataBtn = findViewById(R.id.insert_data);

        insertDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                displayDatabaseInfo();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                null, null, null, null, null);

        TextView displayView = findViewById(R.id.text_view);

        try {
            displayView.setText("The inventory table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(
                    InventoryContract.InventoryEntry._ID + " - " +
                            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
                            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE + " - " +
                            InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME + " - " +
                            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER +
                            "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int price = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantity = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int suplierName = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int suplierPhoneNumber = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(price);
                int currentQuantity = cursor.getInt(quantity);
                String currentSuplierName = cursor.getString(suplierName);
                int currentSuplierPhoneNumber = cursor.getInt(suplierPhoneNumber);

                displayView.append("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSuplierName + " - " +
                        currentSuplierPhoneNumber);
            }
        } finally {
            cursor.close();
        }
    }

    private void insertData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "Alex");
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, 20);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, 3);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, "Troy");
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, 89000000);
        long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
    }
}
