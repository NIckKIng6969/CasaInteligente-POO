// Solucion completa del codelab "Usa clases y objetos en Kotlin"
// Incluye el desafio de la seccion 11 (SmartDevice / SmartHome).

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// ---------------------------------------------------------------------------
// Delegado de propiedad: controla que el valor este dentro de un rango.
// ---------------------------------------------------------------------------
class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    private var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int = fieldData

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

// ---------------------------------------------------------------------------
// Superclase
// ---------------------------------------------------------------------------
open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "online"
    open val deviceType = "unknown"

    // Constructor secundario que traduce un codigo Int a un estado de texto.
    constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when (statusCode) {
            0 -> "offline"
            1 -> "online"
            else -> "unknown"
        }
    }

    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }

    // Desafio: imprime la informacion del dispositivo.
    fun printDeviceInfo() {
        println("Device name: $name, category: $category, type: $deviceType")
    }
}

// ---------------------------------------------------------------------------
// Smart TV
// ---------------------------------------------------------------------------
class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"

    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }

    // Desafio: bajar el volumen solo si la TV esta encendida.
    fun decreaseVolume() {
        if (deviceStatus == "on") {
            speakerVolume--
            println("Speaker volume decreased to $speakerVolume.")
        } else {
            println("Smart TV is off. Turn it on to change the volume.")
        }
    }

    fun nextChannel() {
        if (deviceStatus == "on") {
            channelNumber++
            println("Channel number increased to $channelNumber.")
        } else {
            println("Smart TV is off. Turn it on to change the channel.")
        }
    }

    // Desafio: ir al canal anterior solo si la TV esta encendida.
    fun previousChannel() {
        if (deviceStatus == "on") {
            channelNumber--
            println("Channel number decreased to $channelNumber.")
        } else {
            println("Smart TV is off. Turn it on to change the channel.")
        }
    }

    override fun turnOn() {
        super.turnOn()
        println("$name is turned on. Speaker volume is set to $speakerVolume and channel number is set to $channelNumber.")
    }

    override fun turnOff() {
        super.turnOff()
        println("$name turned off")
    }
}

// ---------------------------------------------------------------------------
// Lampara inteligente
// ---------------------------------------------------------------------------
class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart Light"

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }

    // Desafio: bajar el brillo solo si la lampara esta encendida.
    fun decreaseBrightness() {
        if (deviceStatus == "on") {
            brightnessLevel--
            println("Brightness decreased to $brightnessLevel.")
        } else {
            println("Smart Light is off. Turn it on to change the brightness.")
        }
    }

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("$name turned off")
    }
}

// ---------------------------------------------------------------------------
// SmartHome: relacion HAS-A con los dispositivos
// ---------------------------------------------------------------------------
class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        if (smartTvDevice.deviceStatus != "on") {
            deviceTurnOnCount++
            smartTvDevice.turnOn()
        } else {
            println("Smart TV is already on.")
        }
    }

    fun turnOffTv() {
        if (smartTvDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartTvDevice.turnOff()
        } else {
            println("Smart TV is already off.")
        }
    }

    fun turnOnLight() {
        if (smartLightDevice.deviceStatus != "on") {
            deviceTurnOnCount++
            smartLightDevice.turnOn()
        } else {
            println("Smart Light is already on.")
        }
    }

    fun turnOffLight() {
        if (smartLightDevice.deviceStatus == "on") {
            deviceTurnOnCount--
            smartLightDevice.turnOff()
        } else {
            println("Smart Light is already off.")
        }
    }

    fun increaseTvVolume() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.increaseSpeakerVolume()
        } else {
            println("Smart TV is off. Turn it on first.")
        }
    }

    // ----- Desafio: metodos nuevos en SmartHome -----

    fun decreaseTvVolume() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.decreaseVolume()
        } else {
            println("Smart TV is off. Turn it on first.")
        }
    }

    fun changeTvChannelToNext() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.nextChannel()
        } else {
            println("Smart TV is off. Turn it on first.")
        }
    }

    fun changeTvChannelToPrevious() {
        if (smartTvDevice.deviceStatus == "on") {
            smartTvDevice.previousChannel()
        } else {
            println("Smart TV is off. Turn it on first.")
        }
    }

    fun printSmartTvInfo() {
        smartTvDevice.printDeviceInfo()
    }

    fun printSmartLightInfo() {
        smartLightDevice.printDeviceInfo()
    }

    fun increaseLightBrightness() {
        if (smartLightDevice.deviceStatus == "on") {
            smartLightDevice.increaseBrightness()
        } else {
            println("Smart Light is off. Turn it on first.")
        }
    }

    fun decreaseLightBrightness() {
        if (smartLightDevice.deviceStatus == "on") {
            smartLightDevice.decreaseBrightness()
        } else {
            println("Smart Light is off. Turn it on first.")
        }
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
}

// ---------------------------------------------------------------------------
// Programa principal
// ---------------------------------------------------------------------------
fun main() {
    val smartTvDevice = SmartTvDevice(deviceName = "Android TV", deviceCategory = "Entertainment")
    val smartLightDevice = SmartLightDevice(deviceName = "Google Nest", deviceCategory = "Utility")

    val smartHome = SmartHome(
        smartTvDevice = smartTvDevice,
        smartLightDevice = smartLightDevice
    )

    smartHome.turnOnTv()
    smartHome.turnOnLight()
    println("Device turn on count: ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.printSmartTvInfo()
    smartHome.increaseTvVolume()
    smartHome.increaseTvVolume()
    smartHome.decreaseTvVolume()
    smartHome.changeTvChannelToNext()
    smartHome.changeTvChannelToNext()
    smartHome.changeTvChannelToPrevious()
    println()

    smartHome.printSmartLightInfo()
    smartHome.increaseLightBrightness()
    smartHome.increaseLightBrightness()
    smartHome.decreaseLightBrightness()
    println()

    smartHome.turnOffAllDevices()
    println("Device turn on count: ${smartHome.deviceTurnOnCount}")
}
