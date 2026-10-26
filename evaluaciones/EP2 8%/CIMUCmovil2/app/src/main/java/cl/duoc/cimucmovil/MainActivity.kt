package cl.duoc.cimucmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.cimucmovil.ui.CimApp
import cl.duoc.cimucmovil.ui.theme.CIMUCmovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CIMUCmovilTheme {
                CimApp()
            }
        }
    }
}
