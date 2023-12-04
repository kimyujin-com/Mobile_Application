package ddwu.com.mobile.sensortest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ddwu.com.mobile.sensortest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sensorManager : SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.btnAllSensor.setOnClickListener {
            val deviceSensors : List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

            for (sensor in deviceSensors) {
                mainBinding.tvResult.setText( mainBinding.tvResult.text.toString() + "\n"
                + sensor.toString()
                );
            }
        }

    }

}