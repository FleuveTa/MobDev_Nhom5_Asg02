import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.convertMillisToDate(): String {
    // Create a calendar instance in the default time zone
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
        // Adjust for the time zone offset to get the correct local date
        val zoneOffset = get(Calendar.ZONE_OFFSET)
        val dstOffset = get(Calendar.DST_OFFSET)
        add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    }

    val today = Calendar.getInstance()

    // Format the calendar time in the specified format
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    val s = sdf.format(calendar.time)

    var res = sdf.format(today.time)

    if (res.equals(s)) {
        res = "Today"
    } else {
        res = s
    }

    return res
}