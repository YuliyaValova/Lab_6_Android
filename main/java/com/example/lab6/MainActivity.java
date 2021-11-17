package com.example.lab6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.*;


public class MainActivity extends AppCompatActivity {

    private final static String TYPE = ".txt";
    private final static String SURNAME = "Surname: ";
    private final static String GROUP = "Group: ";
    private final static String FACULTY = "Faculty: ";
    private static final int REQUEST_CODE_PERMISSION_WRITE = 1;
    private static final int REQUEST_CODE_PERMISSION_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private File getExternalPath(String fileName) {
        //получаем доступ к папке приложения во внешнем хранилище и устанавливаем объект файла:
        return new File(getExternalFilesDir(null), fileName);
    }

    // сохранение файла
    public void saveText(View view) {

        getPermissionsForWrite();

            EditText surname = findViewById(R.id.Surname);
            EditText group = findViewById(R.id.Group);
            EditText faculty = findViewById(R.id.Faculty);
            EditText file = findViewById(R.id.FileName);

            StringBuilder str = new StringBuilder();

            str.append(SURNAME).append(surname.getText().toString()).append("\n");

            str.append(GROUP).append(group.getText().toString()).append("\n");

            str.append(FACULTY).append(faculty.getText().toString()).append("\n");

            String fileName = file.getText().toString() + TYPE;
            String text = new String(str);

            try (FileOutputStream fos = new FileOutputStream(getExternalPath(fileName))) {
                fos.write(text.getBytes());
                Toast.makeText(this, "File is saved.", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }

    private void getPermissionsForWrite() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE);
        };

    };

    private void getPermissionsForRead() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ);
        };
    };


    public void openText(View view) {
        getPermissionsForRead();
        TextView textView = findViewById(R.id.fileOutput);
        EditText file = findViewById(R.id.fRead);
        String fileName = file.getText().toString() + TYPE;

        File fileForOutput = getExternalPath(fileName);
        if (!fileForOutput.exists()) {
            Toast.makeText(this, "File with such name doesn't exists.", Toast.LENGTH_SHORT).show();
            return;
        }
        try (FileInputStream fin = new FileInputStream(fileForOutput)) {
            byte[] info = new byte[fin.available()];
            fin.read(info);
            String infoText = new String(info);
            textView.setText(infoText);
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}