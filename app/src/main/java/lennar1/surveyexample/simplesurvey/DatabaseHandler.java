package lennar1.surveyexample.simplesurvey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Joyner on 4/15/15.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "dbaContact",
            TABLE_CONTACTS = "Contacts",
            KEY_ID = "idContact",
            KEY_FNAME = "txtFName",
            KEY_LNAME = "txtLName",
            KEY_EMAIL = "txtEmail",
            KEY_PHONE = "txtPhone",
            KEY_ZIP = "txtZip";


    /* Constuctor */

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT," +
                KEY_EMAIL + " TEXT," + KEY_PHONE + " TEXT," + KEY_ZIP + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    /* Creating a User */
    public void createContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, contact.gettxtFName());
        values.put(KEY_LNAME, contact.gettxtLName());
        values.put(KEY_EMAIL, contact.gettxtEmail());
        values.put(KEY_PHONE, contact.gettxtPhone());
        values.put(KEY_ZIP, contact.gettxtZip());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    /* Get Contact (Read Contact) */
    public Contact getContact(int idContact) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_FNAME, KEY_LNAME, KEY_EMAIL, KEY_PHONE, KEY_ZIP },
                KEY_ID + "=?", new String[]{String.valueOf(idContact)}, null, null, null, null);

        if(cursor !=null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        db.close();
        cursor.close();
        return contact;
    }

    /* Delete Contact */
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[] { String.valueOf(contact.getidContact()) });
        db.close();
    }

    /* Get Contact Count */
    public int getContactCount() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;

    }

    /* Update Contact */
    public int updateContact (Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, contact.gettxtFName());
        values.put(KEY_LNAME, contact.gettxtLName());
        values.put(KEY_EMAIL, contact.gettxtEmail());
        values.put(KEY_PHONE, contact.gettxtPhone());
        values.put(KEY_ZIP, contact.gettxtZip());

        int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[] {String.valueOf(contact.getidContact()) });
            db.close();
        return rowsAffected;

    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                    contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }


}
