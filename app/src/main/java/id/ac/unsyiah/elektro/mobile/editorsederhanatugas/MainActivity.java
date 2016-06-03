package id.ac.unsyiah.elektro.mobile.editorsederhanatugas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {
    private static String NAMA_BERKAS = "";
    private static final int PICKFILE_RESULT_CODE=1;

    String preferencesFirst = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTime();

    }

    public void firstTime(){
        SharedPreferences first = getSharedPreferences(preferencesFirst,0);
        String namaBerkas = first.getString("berkas","");
        if(first.getBoolean("firstTime",true)){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent,PICKFILE_RESULT_CODE);

            first.edit().putBoolean("firstTime", false).apply();
        }else{
            NAMA_BERKAS = namaBerkas;
            berkas();
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case PICKFILE_RESULT_CODE:
                if (resultCode==RESULT_OK){
                    NAMA_BERKAS = data.getData().getPath();
                    SharedPreferences first = getSharedPreferences(preferencesFirst,0);
                    SharedPreferences.Editor editor = first.edit();
                    editor.putString("berkas", NAMA_BERKAS);
                    editor.apply();
                    berkas();
                }
                break;
        }
    }

    public void berkas(){
        String isiBerkas = bacaBerkas();
        EditText txtIsi = (EditText) findViewById(R.id.editText);
        txtIsi.setText(isiBerkas);
        txtIsi.setSelection(isiBerkas.length());
    }

    /**
     * Tangani penekanan tombol simpan.
     */
    public void clickBtnSimpan(View view) {
        EditText txtIsi = (EditText) findViewById(R.id.editText);
        String isi = txtIsi.getText().toString();
        simpanBerkas(isi);
    }


    /**
     * Membaca semua isi suatu berkas text.
     *
     * @return Semua isi berkas.
     */
    private String bacaBerkas() {
        StringWriter stringWriter = new StringWriter();

        FileInputStream berkasStream = null;
        try {
            berkasStream = new FileInputStream(new File(NAMA_BERKAS));
            InputStreamReader berkasStreamReader = new InputStreamReader(berkasStream);
            BufferedReader berkasBuffered = new BufferedReader(berkasStreamReader);
            boolean barisPertama = true;

            String satuBaris = null;
            try {
                satuBaris = berkasBuffered.readLine();
                while (satuBaris != null) {
                    if (barisPertama == false)
                        stringWriter.write("\n");
                    else
                        barisPertama = false;
                    stringWriter.write(satuBaris);
                    satuBaris = berkasBuffered.readLine();
                }
            }
            catch (IOException salah) {
                salah.printStackTrace();
            }
            finally {
                berkasBuffered.close();
            }
        }
        catch (FileNotFoundException salah) {
            salah.printStackTrace();
        }
        catch (IOException salah) {
            salah.printStackTrace();
        return stringWriter.toString();
    }

    /**
     * Menulis ke suatu berkas text.
     *
     * @param isi Apa yang harus ditulis ke berkas.
     */
    private void simpanBerkas(String isi) {
        FileOutputStream berkasStream = null;
        try {
            berkasStream = new FileOutputStream (new File(NAMA_BERKAS));
            OutputStreamWriter berkasStreamWriter = new OutputStreamWriter(berkasStream);
            BufferedWriter berkasBuffered = new BufferedWriter(berkasStreamWriter);

            try {
                berkasBuffered.write(isi);
            }
            catch (IOException salah) {
                salah.printStackTrace();
            }
            finally {
                berkasBuffered.close();
            }
        }
        catch (FileNotFoundException salah) {
            salah.printStackTrace();
        }
        catch (IOException salah) {
            salah.printStackTrace();
        }
    }
}