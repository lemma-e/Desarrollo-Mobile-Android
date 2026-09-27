package ar.edu.unicen.seminario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.unicen.seminario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clickMeButton.setOnClickListener {
            binding.helloWorldTextView.text =
                getString(R.string.complete_test)
        }
    }
}