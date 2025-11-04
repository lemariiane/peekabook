package com.example.peekaboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.time.LocalDate;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS pets(" +
                "pet_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "especie TEXT, " +
                "datanasc TEXT, " +
                "descricao TEXT, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))");
    }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS users");
    db.execSQL("DROP TABLE IF EXISTS pets");
    onCreate(db);
}

    public Boolean insertData(String nome, String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("email", email);
        contentValues.put("password", password);

        long result = MyDatabase.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean insertPetData(String nome, String especie, String datanasc, String descricao, int user_id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", nome);
        contentValues.put("especie", especie);
        contentValues.put("datanasc", datanasc);
        contentValues.put("descricao", descricao);
        contentValues.put("user_id", user_id);

        long result = MyDatabase.insert("pets", null, contentValues);
        return result != -1;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public int getUserId(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT id FROM users WHERE email = ?", new String[]{email});
        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT nome, email FROM users WHERE email = ?", new String[]{email});
        return cursor;
    }

    public Cursor getOwnerPets(int user_id) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM pets WHERE user_id = ?", new String[]{String.valueOf(user_id)});
        return cursor;
    }

    public Boolean deletePet(int pet_id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();

        // O método delete() é o mais seguro para exclusões
        int result = MyDatabase.delete("pets", "pet_id = ?", new String[]{String.valueOf(pet_id)});

        return result > 0; // Retorna true se mais de zero linhas foram excluídas
    }

    public Boolean updatePetData(int petId, String nome, String especie, String descricao, String dataNasc) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", nome);
        contentValues.put("especie", especie);
        contentValues.put("descricao", descricao);
        contentValues.put("datanasc", dataNasc);

        // O whereClause e whereArgs especificam qual linha (pet) deve ser atualizada
        int result = MyDatabase.update("pets", contentValues, "pet_id = ?", new String[]{String.valueOf(petId)});

        // Retorna true se pelo menos uma linha foi afetada
        return result > 0;
    }
    public Cursor getPetById(int pet_id) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();

        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM pets WHERE pet_id = ?", new String[]{String.valueOf(pet_id)});
        return cursor;
    }

    public Cursor getUserDataById(int userId) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();

        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)});
        return cursor;
    }

    public Boolean updateUserData(int userId, String nome, String newHashedPassword) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", nome);

        //Somente atualiza a senha se uma nova senha for fornecida
        if (newHashedPassword != null && !newHashedPassword.isEmpty()) {
            contentValues.put("senha", newHashedPassword);
        }

        int result = MyDatabase.update("users", contentValues, "id = ?", new String[]{String.valueOf(userId)});

        return result > 0;
    }

    public Cursor getNextPetBirthday(int userId) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();

        Cursor cursor = MyDatabase.rawQuery("SELECT nome, datanasc FROM pets WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        return cursor;
    }
}
