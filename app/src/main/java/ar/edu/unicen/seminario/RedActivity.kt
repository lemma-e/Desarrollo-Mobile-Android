package ar.edu.unicen.seminario

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.unicen.seminario.databinding.ActivityRedBinding

class RedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationButton.setOnClickListener {

            val intent = Intent(this, BlueActivity::class.java)

            startActivity(intent)
        }
    }
}