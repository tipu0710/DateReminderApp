package com.systech.farha.datereminderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import com.systech.farha.datereminderapp.model.Person;
import com.systech.farha.datereminderapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "DRA_DB";

    //USER TABLE
    private static final String TABLE_USER = "user";

    private static final String USER_ID = "id";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_QUESTION_1 = "user_question_1";
    private static final String USER_QUESTION_2 = "user_question_2";
    private static final String USER_ANSWER_1 = "user_answer_1";
    private static final String USER_ANSWER_2 = "user_answer_2";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_ADDRESS = "user_address";
    private static final String USER_QUESTION_SKIPPED = "user_is_ques_skipped";
    private static final String USER_PROFILE_PIC = "user_profile_pic";

    private String CREATE_USER_TABLE
            = "CREATE TABLE " + TABLE_USER + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_NAME + " TEXT NOT NULL,"
            + USER_USERNAME + " TEXT NOT NULL,"
            + USER_EMAIL + " TEXT,"
            + USER_PASSWORD + " TEXT NOT NULL,"
            + USER_QUESTION_1 + " TEXT NOT NULL,"
            + USER_QUESTION_2 + " TEXT NOT NULL,"
            + USER_ANSWER_1 + " TEXT NOT NULL,"
            + USER_ANSWER_2 + " TEXT NOT NULL,"
            + USER_QUESTION_SKIPPED + " INTEGER DEFAULT 0,"
            + USER_PHONE + " TEXT NOT NULL,"
            + USER_ADDRESS + " TEXT,"
            + USER_PROFILE_PIC + " BLOB NOT NULL"
            + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //PERSON TABLE
    private static final String TABLE_PERSON = "person";

    private static final String PERSON_ID = "id";
    private static final String PERSON_NAME = "name";
    private static final String PERSON_PHONE_NO = "phone_no";
    private static final String PERSON_BORROW_DATE = "borrow_date";
    private static final String PERSON_FRIEND_DATE = "friend_date";
    private static final String PERSON_LOAN_DATE = "loan_date";
    private static final String PERSON_TIME_FRIEND = "time_friend";
    private static final String PERSON_TIME_BORROWER = "time_borrower";
    private static final String PERSON_TIME_LOAN = "time_loan";
    private static final String PERSON_AMOUNT_LOAN = "loan_amount";
    private static final String PERSON_AMOUNT_BORROW = "borrow_amount";
    private static final String PERSON_LOAN = "loan";
    private static final String PERSON_FRIEND= "friend";
    private static final String PERSON_BORROW= "borrow";
    private static final String PERSON_PROFILE= "profile";
    private static final String PERSON_USER_ID = "user_id";
    private static final String PERSON_LOAN_HAS_PAID = "loan_has_paid";
    private static final String PERSON_BORROW_HAS_PAID = "borrow_has_paid";


    private String CREATE_PERSON_TABLE
            = "CREATE TABLE " + TABLE_PERSON + "("
            + PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PERSON_NAME + " TEXT NOT NULL,"
            + PERSON_PHONE_NO + " TEXT NOT NULL,"
            + PERSON_BORROW_DATE + " DATETIME,"
            + PERSON_LOAN_DATE + " DATETIME,"
            + PERSON_FRIEND_DATE + " DATETIME,"
            + PERSON_TIME_FRIEND + " DATETIME,"
            + PERSON_TIME_BORROWER + " DATETIME,"
            + PERSON_TIME_LOAN + " DATETIME,"
            + PERSON_AMOUNT_LOAN + " DOUBLE,"
            + PERSON_AMOUNT_BORROW + " DOUBLE,"
            + PERSON_LOAN + " TEXT NOT NULL,"
            + PERSON_FRIEND + " TEXT NOT NULL,"
            + PERSON_BORROW + " TEXT NOT NULL,"
            + PERSON_USER_ID + " INTEGER NOT NULL,"
            + PERSON_LOAN_HAS_PAID + " BOOLEAN,"
            + PERSON_BORROW_HAS_PAID + " BOOLEAN,"
            + PERSON_PROFILE + " BLOB NOT NULL,"
            + "CONSTRAINT FK_USER FOREIGN KEY (" + PERSON_USER_ID + ")"
            + " REFERENCES " + TABLE_USER + "(" + USER_ID + ")"
            + ")";

    private String DROP_PERSON_TABLE = "DROP TABLE IF EXISTS " + TABLE_PERSON;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PERSON_TABLE);

        onCreate(db);
    }


    //USER CRUD OPERATION
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getName());
        values.put(USER_USERNAME, user.getUserName());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_QUESTION_1, user.getQuestion1());
        values.put(USER_ANSWER_1, user.getAnswer1());
        values.put(USER_QUESTION_2, user.getQuestion2());
        values.put(USER_ANSWER_2, user.getAnswer2());
        values.put(USER_QUESTION_SKIPPED, user.isQuestionSkipped());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_ADDRESS, user.getAddress());
        values.put(USER_PROFILE_PIC, user.getProfile());

        long b = db.insert(TABLE_USER, null, values);
        db.close();

        return b!=-1;
    }


    public List<User> getAllUser() {
        String[] columns = {
                USER_ID,
                USER_USERNAME,
                USER_EMAIL,
                USER_NAME,
                USER_PASSWORD,
                USER_PHONE,
                USER_ADDRESS
        };
        String sortOrder =
                USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_USER, columns, null, null, null, null, sortOrder);


        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(USER_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(USER_PASSWORD)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getName());
        values.put(USER_USERNAME, user.getUserName());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        values.put(USER_QUESTION_1, user.getQuestion1());
        values.put(USER_QUESTION_2, user.getQuestion2());
        values.put(USER_ANSWER_1, user.getAnswer1());
        values.put(USER_ANSWER_2, user.getAnswer2());
        values.put(USER_QUESTION_SKIPPED, user.isQuestionSkipped());
        values.put(USER_PHONE, user.getPhone());
        values.put(USER_ADDRESS, user.getAddress());
        values.put(USER_PROFILE_PIC, user.getProfile());

        int i = db.update(TABLE_USER, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return i==1;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUserEmail(String email) {

        String[] columns = {
                USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = USER_EMAIL + " = ?";

        String[] selectionArgs = {email};


        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public boolean checkUserName(String userName) {

        String[] columns = {
                USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = USER_USERNAME + " = ?";

        String[] selectionArgs = {userName};


        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

/*    public int getUserIdByEmail(String email) {

        int userId = 0;
        Cursor cursor = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT " + USER_ID + " FROM " + TABLE_USER
                + " WHERE " + USER_EMAIL + " = ? ";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{email});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                userId = cursor.getInt(cursor.getColumnIndex(USER_ID));
            }

            return userId;
        } finally {
            cursor.close();
            db.close();
        }
    }*/

    public int getUserIdByUserName(String userName) {

        int userId = 0;
        Cursor cursor = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT " + USER_ID + " FROM " + TABLE_USER
                + " WHERE " + USER_USERNAME + " = ? ";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{userName});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                userId = cursor.getInt(cursor.getColumnIndex(USER_ID));
            }

            return userId;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public User getUserById(int id) {
        String name = "name";
        String email = "email";
        String password = null;
        String userName = null;
        boolean isQuestionSkipped = false;
        String question1= null, answer1= null, question2= null, answer2 = null, phone = null, address = null;
        byte[] profile = null;

        String[] columns = {
                USER_EMAIL,
                USER_NAME,
                USER_USERNAME,
                USER_PASSWORD,
                USER_QUESTION_1,
                USER_ANSWER_1,
                USER_QUESTION_2,
                USER_ANSWER_2,
                USER_QUESTION_SKIPPED,
                USER_PHONE,
                USER_ADDRESS,
                USER_PROFILE_PIC
        };

        SQLiteDatabase db = this.getReadableDatabase();
        String selection = USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
            name = cursor.getString(cursor.getColumnIndex(USER_NAME));
            userName = cursor.getString(cursor.getColumnIndex(USER_USERNAME));
            password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
            question1 = cursor.getString(cursor.getColumnIndex(USER_QUESTION_1));
            answer1 = cursor.getString(cursor.getColumnIndex(USER_ANSWER_1));
            question2 = cursor.getString(cursor.getColumnIndex(USER_QUESTION_2));
            answer2 = cursor.getString(cursor.getColumnIndex(USER_ANSWER_2));
            isQuestionSkipped = (cursor.getInt(cursor.getColumnIndex(USER_QUESTION_SKIPPED))==1);
            phone = cursor.getString(cursor.getColumnIndex(USER_PHONE));
            address = cursor.getString(cursor.getColumnIndex(USER_ADDRESS));
            profile = cursor.getBlob(cursor.getColumnIndex(USER_PROFILE_PIC));
        }

        return new User(id, name, userName, email, password, question1, answer1, question2, answer2, phone, address, profile, isQuestionSkipped);
    }

    public boolean validateUser(String userName, String password) {

        String[] columns = {
                USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = USER_USERNAME + " = ?" + " AND " + USER_PASSWORD + " = ?";

        String[] selectionArgs = {userName, password};


        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        return cursorCount > 0;
    }


    //PERSON CRUD OPERATION
    public boolean addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_PHONE_NO, person.getPhoneNo());
        values.put(PERSON_BORROW_DATE, person.getBorrowDate());
        values.put(PERSON_LOAN_DATE, person.getLoanDate());
        values.put(PERSON_FRIEND_DATE, person.getFriendDate());
        values.put(PERSON_TIME_FRIEND, person.getTimeFriend());
        values.put(PERSON_TIME_BORROWER, person.getTimeBorrower());
        values.put(PERSON_TIME_LOAN, person.getTimeLoner());
        values.put(PERSON_AMOUNT_LOAN, person.getAmountLoan());
        values.put(PERSON_AMOUNT_BORROW, person.getAmountBorrow());
        values.put(PERSON_LOAN, person.getLoan());
        values.put(PERSON_FRIEND, person.getFriend());
        values.put(PERSON_BORROW, person.getBorrow());
        values.put(PERSON_USER_ID, person.getUserId());
        values.put(PERSON_LOAN_HAS_PAID, person.getLoanHasPaid());
        values.put(PERSON_BORROW_HAS_PAID, person.getBorrowHasPaid());
        values.put(PERSON_PROFILE, person.getProfile());

        long check = db.insert(TABLE_PERSON, null, values);
        db.close();

        return check!=-1;
    }

    public List<Person> getLoanerList(int userId, String type) {

        Cursor cursor = null;
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_PERSON
                + " WHERE " + PERSON_USER_ID + " = ? AND " + PERSON_LOAN + " = ?";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(userId), type});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                    person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                    person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                    person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                    person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                    person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                    person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                    person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                    person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                    person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                    person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                    person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                    person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                    person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                    person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                    person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                    person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);
                    person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);

                    personList.add(person);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return personList;
    }

    public List<Person> getBorrowList(int userId, String type) {

        Cursor cursor = null;
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_PERSON
                + " WHERE " + PERSON_USER_ID + " = ? AND " + PERSON_BORROW + " = ?";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(userId), type});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                    person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                    person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                    person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                    person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                    person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                    person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                    person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                    person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                    person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                    person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                    person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                    person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                    person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                    person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                    person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                    person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
                    person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);

                    personList.add(person);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return personList;
    }

    public List<Person> getFriendList(int userId, String type) {

        Cursor cursor = null;
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_PERSON
                + " WHERE " + PERSON_USER_ID + " = ? AND " + PERSON_FRIEND + " = ?";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(userId), type});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                    person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                    person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                    person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                    person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                    person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                    person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                    person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                    person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                    person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                    person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                    person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                    person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                    person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                    person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                    person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                    person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
                    person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);

                    personList.add(person);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        Log.v("CheckSize", personList.size()+"   "+userId);
        return personList;
    }

    public List<Person> getPersonList(int userId) {

        Cursor cursor = null;
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_PERSON
                + " WHERE " + PERSON_USER_ID + " = ?";

        try {
            cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(userId)});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Person person = new Person();
                    person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                    person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                    person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                    person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                    person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                    person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                    person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                    person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                    person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                    person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                    person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                    person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                    person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                    person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                    person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                    person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                    person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
                    person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);

                    personList.add(person);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return personList;
    }

    public boolean checkPerson(String name, String phoneNo) {

        String[] columns = {
                PERSON_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = PERSON_NAME + " = ? AND " + PERSON_PHONE_NO + "= ?";

        String[] selectionArgs = {name, phoneNo};


        Cursor cursor = db.query(TABLE_PERSON, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public boolean updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_PHONE_NO, person.getPhoneNo());
        values.put(PERSON_BORROW_DATE, person.getBorrowDate());
        values.put(PERSON_LOAN_DATE, person.getLoanDate());
        values.put(PERSON_FRIEND_DATE, person.getFriendDate());
        values.put(PERSON_TIME_FRIEND, person.getTimeFriend());
        values.put(PERSON_TIME_BORROWER, person.getTimeBorrower());
        values.put(PERSON_TIME_LOAN, person.getTimeLoner());
        values.put(PERSON_AMOUNT_LOAN, person.getAmountLoan());
        values.put(PERSON_AMOUNT_BORROW, person.getAmountBorrow());
        values.put(PERSON_LOAN, person.getLoan());
        values.put(PERSON_FRIEND, person.getFriend());
        values.put(PERSON_BORROW, person.getBorrow());
        values.put(PERSON_USER_ID, person.getUserId());
        values.put(PERSON_BORROW_HAS_PAID, person.getBorrowHasPaid());
        values.put(PERSON_LOAN_HAS_PAID, person.getLoanHasPaid());
        values.put(PERSON_PROFILE, person.getProfile());

        int i = db.update(TABLE_PERSON, values, PERSON_ID + " = ? AND "+PERSON_USER_ID +" = ?",
                new String[]{String.valueOf(person.getId()),String.valueOf(person.getUserId())});

        db.close();

        return i==1;
    }

    public void deletePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PERSON, PERSON_ID + " = ? AND "+PERSON_USER_ID + " = ?",
                new String[]{String.valueOf(person.getId()),String.valueOf(person.getUserId())});
        db.close();
    }

    public List<Person> getPersonByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);

        String[] sqlSelect = {
                PERSON_ID,
                PERSON_NAME,
                PERSON_PHONE_NO,
                PERSON_FRIEND_DATE,
                PERSON_LOAN_DATE,
                PERSON_BORROW_DATE,
                PERSON_TIME_FRIEND,
                PERSON_TIME_BORROWER,
                PERSON_TIME_LOAN,
                PERSON_AMOUNT_BORROW,
                PERSON_AMOUNT_LOAN,
                PERSON_FRIEND,
                PERSON_LOAN,
                PERSON_BORROW,
                PERSON_USER_ID,
                PERSON_PROFILE,
                PERSON_LOAN_HAS_PAID,
                PERSON_BORROW_HAS_PAID
        };
        Cursor cursor = qb.query(db, sqlSelect, "name LIKE ?", new String[]{"%"+name+"%"}, null,null,null);
        List<Person> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Person person = new Person();
                person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
                person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);
                result.add(person);
            }while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public List<Person> getFriendByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);

        String[] sqlSelect = {
                PERSON_ID,
                PERSON_NAME,
                PERSON_PHONE_NO,
                PERSON_FRIEND_DATE,
                PERSON_LOAN_DATE,
                PERSON_BORROW_DATE,
                PERSON_TIME_FRIEND,
                PERSON_TIME_BORROWER,
                PERSON_TIME_LOAN,
                PERSON_AMOUNT_BORROW,
                PERSON_AMOUNT_LOAN,
                PERSON_FRIEND,
                PERSON_LOAN,
                PERSON_BORROW,
                PERSON_USER_ID,
                PERSON_PROFILE,
                PERSON_LOAN_HAS_PAID,
                PERSON_BORROW_HAS_PAID
        };
        Cursor cursor = qb.query(db, sqlSelect, "name LIKE ? AND "+PERSON_FRIEND+"= ?", new String[]{"%"+name+"%","T"}, null,null,null);
        List<Person> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Person person = new Person();
                person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
                person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
                person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
                person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
                person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
                person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
                person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
                person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
                person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
                person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
                person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
                person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
                person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
                person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
                person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);
                result.add(person);
            }while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public List<String> getNames(){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);

        String[] sqlSelect = {PERSON_NAME};
        Cursor cursor = qb.query(db, sqlSelect, null, null, null,null,null);
        List<String> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                result.add(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
            }while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public Person getPersonById(int id){
        Person person = new Person();

        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);
        String[] sqlSelect = {
                PERSON_ID,
                PERSON_NAME,
                PERSON_PHONE_NO,
                PERSON_FRIEND_DATE,
                PERSON_LOAN_DATE,
                PERSON_BORROW_DATE,
                PERSON_TIME_FRIEND,
                PERSON_TIME_BORROWER,
                PERSON_TIME_LOAN,
                PERSON_AMOUNT_BORROW,
                PERSON_AMOUNT_LOAN,
                PERSON_FRIEND,
                PERSON_LOAN,
                PERSON_BORROW,
                PERSON_USER_ID,
                PERSON_LOAN_HAS_PAID,
                PERSON_BORROW_HAS_PAID,
                PERSON_PROFILE
        };

        Cursor cursor = qb.query(db, sqlSelect, PERSON_ID+" = ?", new String[]{String.valueOf(id)}, null,null,null);

        cursor.moveToFirst();
        person.setId(cursor.getInt(cursor.getColumnIndex(PERSON_ID)));
        person.setName(cursor.getString(cursor.getColumnIndex(PERSON_NAME)));
        person.setPhoneNo(cursor.getString(cursor.getColumnIndex(PERSON_PHONE_NO)));
        person.setBorrowDate(cursor.getString(cursor.getColumnIndex(PERSON_BORROW_DATE)));
        person.setLoanDate(cursor.getString(cursor.getColumnIndex(PERSON_LOAN_DATE)));
        person.setFriendDate(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND_DATE)));
        person.setTimeFriend(cursor.getString(cursor.getColumnIndex(PERSON_TIME_FRIEND)));
        person.setTimeBorrower(cursor.getString(cursor.getColumnIndex(PERSON_TIME_BORROWER)));
        person.setTimeLoner(cursor.getString(cursor.getColumnIndex(PERSON_TIME_LOAN)));
        person.setAmountBorrow(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
        person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
        person.setLoan(cursor.getString(cursor.getColumnIndex(PERSON_LOAN)));
        person.setFriend(cursor.getString(cursor.getColumnIndex(PERSON_FRIEND)));
        person.setBorrow(cursor.getString(cursor.getColumnIndex(PERSON_BORROW)));
        person.setUserId(cursor.getInt(cursor.getColumnIndex(PERSON_USER_ID)));
        person.setProfile(cursor.getBlob(cursor.getColumnIndex(PERSON_PROFILE)));
        person.setBorrowHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_BORROW_HAS_PAID)) > 0);
        person.setLoanHasPaid(cursor.getInt(cursor.getColumnIndex(PERSON_LOAN_HAS_PAID)) > 0);

        db.close();

        return person;
    }

    public double getTotalLoan(int userId){
        double amount=0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);
        String[] sqlSelect={PERSON_AMOUNT_LOAN};

        Cursor cursor = qb.query(db, sqlSelect, PERSON_LOAN+" = ? AND "+
                PERSON_AMOUNT_LOAN+">0 AND "+
                        PERSON_LOAN_HAS_PAID+" =0 AND "+ PERSON_USER_ID+ " = ?",
                new String[]{"T",String.valueOf(userId)},
                null,null,null);

        if (cursor.moveToFirst()){
            do {
                Person person = new Person();
                person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_LOAN)));
                amount = amount+person.getAmountLoan();
            }while (cursor.moveToNext());
        }
        db.close();
        return amount;
    }

    public double getTotalBorrow(int userId){
        double amount=0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_PERSON);
        String[] sqlSelect={
                PERSON_AMOUNT_BORROW
        };

        Cursor cursor = qb.query(db, sqlSelect, PERSON_BORROW+" = ? AND "+
                PERSON_AMOUNT_BORROW+">0 and "+
                PERSON_BORROW_HAS_PAID+"=0 and "+ PERSON_USER_ID+" = ?",
                new String[]{"T",String.valueOf(userId)},
                null,null,null);

        if (cursor.moveToFirst()){
            do {
                Person person = new Person();
                person.setAmountLoan(cursor.getDouble(cursor.getColumnIndex(PERSON_AMOUNT_BORROW)));
                amount = amount+person.getAmountLoan();
            }while (cursor.moveToNext());
        }
        db.close();
        return amount;
    }
}
