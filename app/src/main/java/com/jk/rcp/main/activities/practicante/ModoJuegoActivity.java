package com.jk.rcp.main.activities.practicante;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.juego.JuegoPaso1Activity;

public class ModoJuegoActivity extends AppCompatActivity {
    private static final String TAG = "ModoJuegoActivity";
    private Button btnJugar;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 200;
    private Spinner spinner;
    private int tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_juego);

        btnJugar = (Button) findViewById(R.id.btnJugar);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ModoJuegoActivity.this, JuegoPaso1Activity.class);
                intent.putExtra("tiempo", tiempo);
                startActivity(intent);
            }
        });

        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        btnJugar.setEnabled(true);
                        tiempo = 1 * 60 * 1000;
                        break;
                    case 2:
                        btnJugar.setEnabled(true);
                        tiempo = 3 * 60 * 1000;
                        break;
                    case 3:
                        btnJugar.setEnabled(true);
                        tiempo = 5 * 60 * 1000;
                        break;
                    default:
                        btnJugar.setEnabled(false);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        beginBLE();
    }

    public void beginBLE() {
        // BLE
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
}
