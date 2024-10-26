package com.app.mvvmdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.mvvmdemo.databinding.ActivitySettingBinding
import com.app.mvvmdemo.helper.MyHelpPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.setting_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyHelpPreference.init(this)

        binding.switch1.isChecked = MyHelpPreference.isAdult

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->

            MyHelpPreference.isAdult = isChecked
        }

        binding.toolSetting.setNavigationOnClickListener {
            finish()
        }

        binding.mCardHistory.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                setTitle("Delete")
                setMessage("Do you really want to delete search history ?")
                setPositiveButton("YES") { _, _ ->
                    Snackbar.make(binding.root.rootView,"History Delete Successfully.",Snackbar.LENGTH_SHORT).show()
                }
                setNegativeButton("NO") { p1, _ ->
                    p1.dismiss()
                }
            }.show()
        }

    }
}