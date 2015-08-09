package com.gordon.exp.test.as.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gordon.exp.test.as.R;
import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.test.as.service.LocalFileMgr;
import com.gordon.exp.test.as.service.UploadThread;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Gordon
 * @date 2015-08-09
 */
public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        String ip = Config.getFtpAddress("0.0.0.0");
        int port = Config.getFtpPort(21);
        String username = Config.getUsername();
        String pass = Config.getPasswd();
        ((EditText) findViewById(R.id.ftp_ip)).setText(ip);
        ((EditText) findViewById(R.id.ftp_port)).setText("" + port);
        ((EditText) findViewById(R.id.ftp_username)).setText(username);
        ((EditText) findViewById(R.id.ftp_passwd)).setText(pass);

        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radios_protocol);
        switch(Config.getProtocol()){
            case FTP:
                RadioButton radioButton = (RadioButton)rGroup.findViewById(R.id.radio_ftp);
                radioButton.setChecked(true);
                break;
            case SFTP:
                RadioButton radioButton2 = (RadioButton)rGroup.findViewById(R.id.radio_sftp);
                radioButton2.setChecked(true);
                break;
        }
    }


    /**
     * save configuration
     *
     * @param v
     */
    public void save(View v) {
        String ip = ((EditText) findViewById(R.id.ftp_ip)).getText().toString();
        String port = ((EditText) findViewById(R.id.ftp_port)).getText()
                .toString();
        String username = ((EditText) findViewById(R.id.ftp_username))
                .getText().toString();
        String pass = ((EditText) findViewById(R.id.ftp_passwd)).getText()
                .toString();
        Config.setHostAddress(ip);
        Config.setFtpAddress(ip);
        try {
            Config.setFtpPort(Integer.parseInt(port));
        } catch (Exception e) {
        }
        Config.setUsername(username);
        Config.setPasswd(pass);
        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radios_protocol);
        switch(rGroup.getCheckedRadioButtonId()){
            case R.id.radio_ftp:
                Config.setProtocol(Config.TransferProtocol.FTP);
                break;
            case R.id.radio_sftp:
                Config.setProtocol(Config.TransferProtocol.SFTP);
                break;
        }
        try {
            Config.save();
            Toast.makeText(getApplicationContext(), getString(R.string.info_config_saved), Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "IllegalArgumentException : " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "IllegalStateException : " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "FileNotFoundException : " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "IOException : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * upload data
     * @param v
     */
    public void upload(View v) {
        save(v);
        new UploadThread(this).start();
    }

    /**
     * delete local data
     *
     * @param v
     */
    public void delete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.info_delete_confirm))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.info_delete_positive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new LocalFileMgr().delete();
                                Toast.makeText(getApplication(), String.format(getString(R.string.info_deleted), Config.dir),
                                        Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton(getString(R.string.info_delete_negative), null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
