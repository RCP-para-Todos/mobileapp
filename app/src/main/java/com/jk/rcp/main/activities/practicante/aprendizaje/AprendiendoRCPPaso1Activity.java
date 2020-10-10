package com.jk.rcp.main.activities.practicante.aprendizaje;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jk.rcp.R;

public class AprendiendoRCPPaso1Activity extends AppCompatActivity {
    private static final String TAG = "AprendiendoRCPPaso_1_Activity";
    private Button btnEntornoSeguro;
    private CheckBox cbNoCables;
    private CheckBox cbNoPersonasHostiles;
    private CheckBox cbNoSituacionViolenta;
    private CheckBox cbNoSignosAnimales;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprender_rcp_paso1);
        // Configuro la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Boton para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnEntornoSeguro = findViewById(R.id.btnEntornoSeguro);
        btnEntornoSeguro.setEnabled(false);
        btnEntornoSeguro.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnEntornoSeguro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AprendiendoRCPPaso1Activity.this, AprendiendoRCPPaso2Activity.class);
                startActivity(intent);
            }
        });

        cbNoCables = findViewById(R.id.cbNoCables);
        cbNoPersonasHostiles = findViewById(R.id.cbNoPersonasHostiles);
        cbNoSituacionViolenta = findViewById(R.id.cbNoSituacionViolenta);
        cbNoSignosAnimales = findViewById(R.id.cbNoSignosAnimales);

        CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbNoCables.isChecked() && cbNoPersonasHostiles.isChecked() && cbNoSignosAnimales.isChecked() && cbNoSituacionViolenta.isChecked()) {
                    btnEntornoSeguro.setEnabled(true);
                    btnEntornoSeguro.getBackground().setColorFilter(null);
                } else {
                    btnEntornoSeguro.setEnabled(false);
                    btnEntornoSeguro.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        };

        cbNoCables.setOnCheckedChangeListener(onCheckedChangedListener);
        cbNoPersonasHostiles.setOnCheckedChangeListener(onCheckedChangedListener);
        cbNoSituacionViolenta.setOnCheckedChangeListener(onCheckedChangedListener);
        cbNoSignosAnimales.setOnCheckedChangeListener(onCheckedChangedListener);
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
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
