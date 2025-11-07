package com.example.peekaboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 8);
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
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS lembretes(" +
                "lembrete_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "pet_id INTEGER, " +
                "tipo TEXT NOT NULL, " +
                "descricao TEXT, " +
                "data TEXT NOT NULL, " +
                "hora TEXT NOT NULL, " +
                "ativo INTEGER DEFAULT 1, " + // 1 = Ativo, 0 = Concluído
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE)");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS users");
    db.execSQL("DROP TABLE IF EXISTS pets");
    db.execSQL("DROP TABLE IF EXISTS lembretes");
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
            contentValues.put("password", newHashedPassword);
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

    public boolean addLembrete(int userId, Integer petId, String tipo, String descricao, String data, String hora) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", userId);
        contentValues.put("tipo", tipo);
        contentValues.put("descricao", descricao);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("ativo", 1); // Inicialmente ativo

        if (petId != null && petId != -1) {
            contentValues.put("pet_id", petId);
        }

        long result = MyDatabase.insert("lembretes", null, contentValues);

        MyDatabase.close();
        return result != -1;
    }

    public List<PetModel> getAllPetsForUser(int userId) {
        List<PetModel> petList = new ArrayList<>();
        SQLiteDatabase MyDatabase = this.getReadableDatabase();

        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM pets WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int idIndex = cursor.getColumnIndexOrThrow("pet_id");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int especieIndex = cursor.getColumnIndexOrThrow("especie");
                int dataNascIndex = cursor.getColumnIndexOrThrow("datanasc");
                int descricaoIndex = cursor.getColumnIndexOrThrow("descricao");
                int userIdIndex = cursor.getColumnIndexOrThrow("user_id");

                do {
                    PetModel pet = new PetModel(
                            cursor.getInt(idIndex),
                            cursor.getString(nomeIndex),
                            cursor.getString(especieIndex),
                            cursor.getString(dataNascIndex),
                            cursor.getString(descricaoIndex),
                            cursor.getInt(userIdIndex)
                    );
                    petList.add(pet);
                } while (cursor.moveToNext());
            } catch (Exception e) {

            } finally {
                cursor.close();
            }
        }
        MyDatabase.close();
        return petList;
    }

    public List<LembreteModel> getAllLembretesForUser(int userId) {
        List<LembreteModel> lembreteList = new ArrayList<>();

        String TABLE_LEMBRETES = "lembretes";
        String TABLE_PETS = "pets";

        String KEY_ID_LEMBRETE = "lembrete_id";
        String KEY_PET_ID = "pet_id";
        String KEY_USER_ID = "user_id";
        String KEY_TIPO = "tipo";
        String KEY_DESCRICAO = "descricao";
        String KEY_DATA = "data";
        String KEY_HORA = "hora";
        String KEY_ATIVO = "ativo";
        String KEY_NOME_PET = "nome";

        // SQL para fazer um JOIN entre lembretes e pets para pegar o nome do pet
        String SELECT_QUERY =
                "SELECT L.*, P.nome AS pet_nome " +
                        "FROM " + TABLE_LEMBRETES + " L " +
                        "LEFT JOIN " + TABLE_PETS + " P ON L." + KEY_PET_ID + " = P.pet_id " +
                        "WHERE L." + KEY_USER_ID + " = ? " +
                        "ORDER BY L.data ASC, L.hora ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(KEY_ID_LEMBRETE);
                int petIdIndex = cursor.getColumnIndex(KEY_PET_ID);
                int tipoIndex = cursor.getColumnIndex(KEY_TIPO);
                int descricaoIndex = cursor.getColumnIndex(KEY_DESCRICAO);
                int dataIndex = cursor.getColumnIndex(KEY_DATA);
                int horaIndex = cursor.getColumnIndex(KEY_HORA);
                int ativoIndex = cursor.getColumnIndex(KEY_ATIVO);
                int petNomeIndex = cursor.getColumnIndex("pet_nome");

                if (idIndex != -1) {

                    LembreteModel lembrete = new LembreteModel(
                            cursor.getInt(idIndex),
                            userId,
                            cursor.getInt(petIdIndex),
                            cursor.getString(tipoIndex),
                            cursor.getString(descricaoIndex),
                            cursor.getString(dataIndex),
                            cursor.getString(horaIndex),
                            cursor.getInt(ativoIndex)
                    );

                    // Pega o nome do pet
                    String petNome = cursor.getString(petNomeIndex);
                    if (lembrete.getPetId() == -1 || petNome == null) {
                        lembrete.setPetNome("Lembrete Geral");
                    } else {
                        lembrete.setPetNome(petNome);
                    }

                    lembreteList.add(lembrete);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lembreteList;
    }
}
