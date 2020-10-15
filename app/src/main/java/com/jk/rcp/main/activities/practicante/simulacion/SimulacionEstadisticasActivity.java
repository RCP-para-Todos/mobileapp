package com.jk.rcp.main.activities.practicante.simulacion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.jk.rcp.R;
import com.jk.rcp.main.activities.practicante.HomeActivityPracticante;
import com.jk.rcp.main.data.model.instant.Instant;

import java.util.ArrayList;
import java.util.List;

public class SimulacionEstadisticasActivity extends AppCompatActivity {
    private static final String TAG = "SimulacionEstadisticasActivity";
    private Button btnAtras;
    private List<Instant> instantes;
    private PieChart chart;
    private TextView ciclosInsuflaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulacion_paso4);

        ciclosInsuflaciones = findViewById(R.id.tvCiclos);

        btnAtras = findViewById(R.id.btnBackHome);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SimulacionEstadisticasActivity.this, HomeActivityPracticante.class);
                startActivity(intent);
            }
        });

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(0.0f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawHoleEnabled(false);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);


        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);
        if (getIntent().getExtras() != null && getIntent().getSerializableExtra("instantes") != null) {
            this.instantes = (List<Instant>) getIntent().getSerializableExtra("instantes");
            float n = 0, i = 0, c = 0, e = 0;
            int mediosegundos = 0;
            for (Instant instante : instantes
            ) {
                if (instante.getCompresion().equals("Nula")) {
                    n = n + 1;
                } else if (instante.getCompresion().equals("Insuficiente")) {
                    i = i + 1;
                } else if (instante.getCompresion().equals("Correcta")) {
                    c = c + 1;
                    mediosegundos += 1;
                } else if (instante.getCompresion().equals("Excesiva")) {
                    e = e + 1;
                }
            }

            ciclosInsuflaciones.setText("Ciclo de insuflaciones: " + Math.floor(mediosegundos / 2));

            setData(n, i, c, e);
        }
    }

    private void setData(float n, float i, float c, float e) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(n, "Nulas"));
        entries.add(new PieEntry(i, "Insuficientes"));
        entries.add(new PieEntry(c, "Correctas"));
        entries.add(new PieEntry(e, "Excesivas"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Finalizando activity");
        finish();
        return true;
    }
}
