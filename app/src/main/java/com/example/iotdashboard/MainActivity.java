package com.example.iotdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{
    public SensorModel myModel;

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumi, txtLight, txtAi;
    LabeledSwitch btnLed, btnPump;
    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        myModel = new SensorModel();
        myModel.SensorModel();
        myModel.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtHumi= findViewById(R.id.txtHumidity);
        txtTemp= findViewById(R.id.txtTemperature);
        btnLed = findViewById(R.id.LightButt);
        btnPump = findViewById(R.id.AnotherButt);
        txtAi = findViewById(R.id.txtAI);
        txtLight=findViewById(R.id.txtLight);
        btnLed.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn== true){
                    sendDataMQTT("Kietlun9302/feeds/nutnhan1","1");
                }else{
                    sendDataMQTT("Kietlun9302/feeds/nutnhan1","0");
                }
            }
        });
        btnPump.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn2) {
                if(isOn2== true){
                    sendDataMQTT("Kietlun9302/feeds/nutnhan2","1");
                }else{
                    sendDataMQTT("Kietlun9302/feeds/nutnhan2","0");
                }
            }
        });
        startMQTT();
    }
    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper= new MQTTHelper(this);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        int[] Data= {0,0,0,0,0,0,0,0,0,0,0,0,};
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0,Data[0]),false,100);

        series.setColor(Color.rgb(0,80,100));
        series.setTitle("Temp Chart");
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setThickness(2);
        graph.addSeries(series);

        graph.setTitle("Temp Chart");
        graph.setTitleTextSize(50);
        graph.setTitleColor(Color.RED);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Temp");
        graph.getViewport().setScalable(true);

        graph.getViewport().setScrollable(true);

        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(9);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            Integer counter = 1;
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if (topic.contains("cambien2")) {
                    myModel.setValueAtIndex(0, Integer.parseInt(message.toString()));
                } else if(topic.contains("nutnhan1")){
                    if(message.toString().equals("1")){
                        btnLed.setOn(true);
                    } else{
                        btnLed.setOn(false);
                    };
                } else if(topic.contains("cambien1")){
                    myModel.setValueAtIndex(2, Integer.parseInt(message.toString()));
                } else if(topic.contains("cambien3")){
                    myModel.setValueAtIndex(1, Integer.parseInt(message.toString()));
                    series.appendData(new DataPoint(counter,Integer.parseInt(message.toString())),false,100);
                    counter++;
                }  else if(topic.contains("nutnhan2")){
                    if(message.toString().equals("1")){
                        btnPump.setOn(true);
                    } else{
                        btnPump.setOn(false);
                    };
                } else if(topic.contains("ai")) {
                    txtAi.setText(message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    @Override
    public void update(Observable o, Object arg) {
        txtHumi.setText(myModel.getValueAtIndex(0) + "%");
        txtTemp.setText(myModel.getValueAtIndex(1) + "°C");
        txtLight.setText(myModel.getValueAtIndex(2) + " LUX");
    }
}



