package com.gordon.exp.test.as.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gordon.exp.test.as.R;
import com.gordon.exp.test.as.domain.Config;
import com.gordon.exp.test.as.domain.Location;
import com.gordon.exp.test.as.service.LocationRecorder;
import com.gordon.exp.test.as.service.WifiSignalProcessor;
import com.gordon.exp.util.Utils;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    /** WIFI Signal Manager */
    private WifiSignalProcessor wifiProcessor = null ;

    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(wifiProcessor == null)
            wifiProcessor = new WifiSignalProcessor((WifiManager) getSystemService(Context.WIFI_SERVICE)) ;

        /** Switch that controls WLAN */
        ToggleButton tgbtn = ((ToggleButton)findViewById(R.id.toggleButton_capture)) ;
        tgbtn.setOnCheckedChangeListener(new WLANToggleListener(wifiProcessor)) ;
        tgbtn.setBackgroundColor(tgbtn.isChecked() ? Color.rgb(0xFF, 0, 0) : Color.rgb(0x76, 0xEE, 0)) ;
        tgbtn.setChecked(wifiProcessor.isWifiOn());

        ((EditText)findViewById(R.id.frequency)).setText(""+Config.getFrequency()) ;
//        ((EditText)findViewById(R.id.duration)).setText(""+Config.getDuration()) ;
        ((EditText)findViewById(R.id.total_train_length)).setText("" + Config.getTotalTrainLength()) ;
        ((EditText)findViewById(R.id.total_test_length)).setText("" + Config.getTotalTestLength()) ;
        ((EditText)findViewById(R.id.editText_x)).setText("-2") ;
        ((EditText)findViewById(R.id.editText_y)).setText("-2") ;
        ((EditText)findViewById(R.id.data_prefix)).setText(Config.getDatasetPrefix());

        // Button to record wifi signal
        ((Button)this.findViewById(R.id.button_collect)).setOnClickListener(new CollectButtonClickListener(this)) ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_upload) {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * set up the WLAN Switch available or not
     * @param enabled
     */
    private void setWIFIToggleEnabled(boolean enabled){
        ToggleButton tgbtn = ((ToggleButton)findViewById(R.id.toggleButton_capture)) ;
        tgbtn.setEnabled(enabled);
    }


    private void resetComponents(){
        Button btn = (Button)this.findViewById(R.id.button_collect) ;
        btn.setText(R.string.btn_collect);
        btn.setEnabled(true);
        ((Button)this.findViewById(R.id.btn_xmm)).setEnabled(true);
        ((Button)this.findViewById(R.id.btn_xpp)).setEnabled(true);
        ((Button)this.findViewById(R.id.btn_ymm)).setEnabled(true);
        ((Button)this.findViewById(R.id.btn_ypp)).setEnabled(true);
        ((EditText)this.findViewById(R.id.editText_x)).setEnabled(true);
        ((EditText)this.findViewById(R.id.editText_y)).setEnabled(true);
        ((EditText)this.findViewById(R.id.total_train_length)).setEnabled(true);
        ((EditText)this.findViewById(R.id.total_test_length)).setEnabled(true);
        ((EditText)this.findViewById(R.id.frequency)).setEnabled(true);
        ((EditText)this.findViewById(R.id.data_prefix)).setEnabled(true);
    }


    private void disableComponents(){
        Button btn = (Button)this.findViewById(R.id.button_collect) ;
        btn.setEnabled(false);
        ((Button)this.findViewById(R.id.btn_xmm)).setEnabled(false);
        ((Button)this.findViewById(R.id.btn_xpp)).setEnabled(false);
        ((Button)this.findViewById(R.id.btn_ymm)).setEnabled(false);
        ((Button)this.findViewById(R.id.btn_ypp)).setEnabled(false);
        ((EditText)this.findViewById(R.id.editText_x)).setEnabled(false);
        ((EditText)this.findViewById(R.id.editText_y)).setEnabled(false);
        ((EditText)this.findViewById(R.id.total_train_length)).setEnabled(false);
        ((EditText)this.findViewById(R.id.total_test_length)).setEnabled(false);
        ((EditText)this.findViewById(R.id.frequency)).setEnabled(false);
        ((EditText)this.findViewById(R.id.data_prefix)).setEnabled(false);

    }


    private void setCollectBtnText(String text){
        Button btn = (Button)this.findViewById(R.id.button_collect) ;
        btn.setText(text);
    }


    /**
     * Some methods that would be invoked while collecting
     * @author Gordon
     * @date 2015-8-7
     *
     */
    public class CollectHandler{

        public void resetViewComponents(){
            resetComponents();
        }

        public void disableViewComponents(){
            disableComponents();
        }

        public void setCollectButtonText(String text){
            setCollectBtnText(text);
        }

        public void setWIFIToggleEnable(boolean enabled){
            setWIFIToggleEnabled(enabled);
        }
    }

    /**
     * WLAN Switch listener
     * @author Gordon
     *
     */
    private final class WLANToggleListener implements CompoundButton.OnCheckedChangeListener{

        private WifiSignalProcessor wpo;

        public WLANToggleListener(WifiSignalProcessor wifiProcessor){
            this.wpo = wifiProcessor;
            if(!wpo.isWifiOn()){
                disableComponents();
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if(wpo == null)
                wpo = new WifiSignalProcessor((WifiManager) getSystemService(Context.WIFI_SERVICE)) ;
            System.out.println(isChecked);
            if(isChecked){
                ((ToggleButton)findViewById(R.id.toggleButton_capture)).setBackgroundColor(Color.rgb(0xFF, 0, 0)) ;
                wpo.startScan() ;
                resetComponents();
            }else{
                ((ToggleButton)findViewById(R.id.toggleButton_capture)).setBackgroundColor(Color.rgb(0x76, 0xEE, 0)) ;
                wpo.closeWifi() ;
                disableComponents();
            }
        }
    }

    /**
     * Listener for collect button
     * @author Gordon
     *
     */
    private final class CollectButtonClickListener implements View.OnClickListener{

        private Activity activity;

        public CollectButtonClickListener(Activity activity){
            this.activity = activity;
        }



        @Override
        public void onClick(View v) {
            String xString = ((EditText)findViewById(R.id.editText_x)).getText().toString() ;
            String yString = ((EditText)findViewById(R.id.editText_y)).getText().toString() ;
            String frequencyString = ((EditText)findViewById(R.id.frequency)).getText().toString() ;
            String totalTrainLengthString = ((EditText)findViewById(R.id.total_train_length)).getText().toString() ;
            String totalTestLengthString = ((EditText)findViewById(R.id.total_test_length)).getText().toString() ;
            String datasetPrefix = ((EditText)findViewById(R.id.data_prefix)).getText().toString() ;
            if(Utils.isEmpty(totalTrainLengthString)){
                totalTrainLengthString = "10";
                ((EditText)findViewById(R.id.total_train_length)).setText(totalTrainLengthString);
            }
            if(Utils.isEmpty(totalTestLengthString)){
                totalTestLengthString = "10";
                ((EditText)findViewById(R.id.total_test_length)).setText(totalTestLengthString);
            }
            if(Utils.isEmpty(datasetPrefix)){
                datasetPrefix = Config.datasetPrefix;
                ((EditText)findViewById(R.id.data_prefix)).setText(Config.getDatasetPrefix());
            }else{
                Config.setDatasetPrefix(datasetPrefix);
            }
            try{
                int freq = Integer.parseInt(frequencyString);
                Config.setFrequency(freq);
            }catch(Exception e){}
            try{
                int totalTrainLength = Integer.parseInt(totalTrainLengthString);
                int totalTestLength = Integer.parseInt(totalTestLengthString);
                if(totalTrainLength <= 0){
                    totalTestLengthString = "10";
                    totalTrainLength = 10;
                    ((EditText)findViewById(R.id.total_test_length)).setText(totalTestLengthString);
                }
                if(totalTrainLength <= 0){
                    totalTrainLengthString = "10";
                    totalTrainLength = 10;
                    ((EditText)findViewById(R.id.total_train_length)).setText(totalTrainLengthString);
                }
                Config.setTotalTestLength(totalTestLength);
                Config.setTotalTrainLength(totalTrainLength);
                Config.setTotalLength(totalTestLength + totalTrainLength);
            } catch (Exception e){}
            LocationRecorder recorder = new LocationRecorder(activity, wifiProcessor,
                    new Location(Integer.parseInt(xString), Integer.parseInt(yString)),
                    new CollectHandler(), new DecimalFormat("0000").format(count++));
            disableComponents();
            recorder.collect();
            Toast.makeText(activity, getString(R.string.info_start_collecting),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * action to be executed when x++ button is pressed
     * @param v
     */
    public void click_xpp(View v){
        addCoordinate(R.id.editText_x, 1);
    }

    /**
     * action to be executed when x-- button is pressed
     * @param v
     */
    public void click_xmm(View v){
        addCoordinate(R.id.editText_x, -1);
    }

    /**
     * action to be executed when y++ button is pressed
     * @param v
     */
    public void click_ypp(View v){
        addCoordinate(R.id.editText_y, 1);
    }

    /**
     * action to be executed when y-- button is pressed
     * @param v
     */
    public void click_ymm(View v){
        addCoordinate(R.id.editText_y, -1);
    }

    private void addCoordinate(int id, int adder){
        EditText et = ((EditText)this.findViewById(id));
        try {
            int value = Integer.parseInt(et.getText().toString());
            value += adder;
            et.setText(""+value);
        } catch (Exception e){
            Toast.makeText(this.getApplicationContext(), this.getString(R.string.err_coor_not_num), Toast.LENGTH_SHORT).show();
        }
    }

}
