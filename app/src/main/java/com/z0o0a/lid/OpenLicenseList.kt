package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.OpenLicenseListBinding

class OpenLicenseList : AppCompatActivity() {
    private lateinit var binding: OpenLicenseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OpenLicenseListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.txtList.text = """Copyright 2020 Philipp Jahoda

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""

        binding.btnBackUserpage.setOnClickListener {
            finish()
        }

    }
}