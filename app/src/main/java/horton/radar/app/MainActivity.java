package horton.radar.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    public Button btn;

    private Random RNG;

    public RadarWorld world;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        world = new RadarWorld();

        btn = findViewById(R.id.btn_addtgt);
        btn.setOnClickListener(this);
        btn = findViewById(R.id.btn_pulse);
        btn.setOnClickListener(this);
        findViewById(R.id.btn_cleartgts).setOnClickListener(this);

        RNG = new Random();

        SeekBar sb = ((SeekBar)findViewById(R.id.sbMaxRange));
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(sb.getMax());

        sb = ((SeekBar)findViewById(R.id.sbDutyCycle));
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(10);

        sb = ((SeekBar)findViewById(R.id.sbSamples));
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(50);

        CheckBox cb = findViewById(R.id.cbShowTgts);
        cb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_addtgt:
                world.addRandomObject();
                updateTgtList();
                break;

            case R.id.btn_pulse:
                try {
                    double[] pulse = world.digitizer.calculatePulseReturn(world);
                    updatePulsePlot(pulse);
                }
                catch (Exception e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG ).show();
                    System.out.println(e.getMessage());
                }
                break;

            case R.id.btn_cleartgts:
                world.objects.clear();
                updateTgtList();
                break;

            case R.id.cbShowTgts:
                boolean check = ((CheckBox)v).isChecked();
                if (check)
                    ((LinearLayout)findViewById(R.id.loTgtList)).setVisibility(View.VISIBLE);
                else
                    ((LinearLayout)findViewById(R.id.loTgtList)).setVisibility(View.GONE);
                break;
        }


    }

    private void updateTgtList()
    {
        ((TextView)findViewById(R.id.tvNumTargets)).setText(world.objects.size() + " targets");

        LinearLayout layout = findViewById(R.id.loTgtList);
        layout.removeAllViews();

        for (int i = 0; i < world.objects.size(); ++i)
        {
            RadarObject o = world.objects.get(i);
            TextView tv = new TextView(this);
            tv.setText("Target["+i+"]: Range: " + o.position.magnitude());
            tv.setPadding(5, 5, 5, 5);
            layout.addView(tv);
        }
    }

    public void updatePulsePlot(double[] pulse)
    {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < pulse.length; ++i)
        {
            entries.add(new Entry((float)i, (float)pulse[i]));
        }
        LineChart chart = findViewById(R.id.mpa_chart);
        chart.clear();
        chart.setData(new LineData(new LineDataSet(entries, " ")));
        ((LineDataSet)chart.getLineData().getDataSetByIndex(0)).setLineWidth(1.5f);
        chart.invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //int progress = seekBar.getProgress();
        double dp = (double)progress / seekBar.getMax();
        //RadarWorld w = (RadarWorld)seekBar.getTag();

        switch(seekBar.getId())
        {
            case R.id.sbMaxRange:
                ((TextView)findViewById(R.id.tvMaxRange)).setText("Max Range: " + (dp*100.0) + " km");
                world.digitizer.rangeLimit = dp * 100000.0;
                break;

            case R.id.sbDutyCycle:
                ((TextView)findViewById(R.id.tvDutyCycle)).setText("Duty Cycle: " + (dp*100.0) + "%");
                world.digitizer.dutyCycle = dp;
                break;

            case R.id.sbSamples:
                ((TextView)findViewById(R.id.tvSamples)).setText("Samples: " + progress);
                world.digitizer.numRangeGates = progress;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
