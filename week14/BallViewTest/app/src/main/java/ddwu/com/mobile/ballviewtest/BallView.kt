package ddwu.com.mobile.ballviewtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.Log
import android.util.MonthDisplayHelper
import android.view.View

class BallView : View {

    val TAG = "BallView"

    constructor(context: Context) : super(context) { initialize() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){ initialize() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initialize()}

    fun initialize() {
        paint.color = Color.RED
        paint.isAntiAlias = true
        isStart = true
        ballR = 100f

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magentometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    lateinit var sensorManager : SensorManager
    lateinit var accelerometer : Sensor
    lateinit var magentometer : Sensor

    val mAccmeterReading = FloatArray(3)
    val mMagnetometerReading = FloatArray(3)

    var pitch: Float = 0f
    var roll: Float = 0f



    val listener : SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccmeterReading, 0, mAccmeterReading.size)
            } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.size)
            }

            if (mAccmeterReading.size != 0 && mMagnetometerReading.size != 0) {
                val rotationMatrix = FloatArray(9)

                val isSuccess: Boolean = SensorManager.getRotationMatrix(
                    rotationMatrix, null, mAccmeterReading, mMagnetometerReading
                )

                if (isSuccess) {
                    var values = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, values)
                    for (i in values.indices) {
                        val degrees: Double = Math.toDegrees(values[i].toDouble())
                        values[i] = degrees.toFloat()
                    }
                    val azimuth: Float = values[0]
                    pitch = values[1]
                    roll = values[2]
                    Log.d(TAG, "azimuth: ${azimuth}, pitch: ${pitch}, roll: ${roll} ")

                    val distance = 5

                    when {
                        pitch >= 0 -> ballY -= distance
                        pitch < 0 -> ballY += distance
                    }
                    when {
                        roll >= 0 -> ballX += distance
                        roll < 0 -> ballX -= distance
                    }
                    //지우고 그리고 반복
                    invalidate()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    }

    fun startListening () {
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magentometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(listener)
    }

    val paint: Paint by lazy {
        Paint()
    }

    var ballX : Float = 0f
    var ballY : Float = 0f
    var ballR : Float = 0f

    var width : Int? = 0
    var height: Int? = 0

    var isStart : Boolean = true

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isStart) {
            width = canvas?.width
            height = canvas?.height
            ballX = width?.div(2)?.toFloat() ?: 100f
            ballY = height?.div(2)?.toFloat() ?: 100f
            isStart = false
        }

        canvas?.drawCircle(ballX, ballY, ballR, paint)
    }
}