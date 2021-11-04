package com.app.nasasearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.nasasearch.R
import com.app.nasasearch.SearchedItems
import com.app.nasasearch.others.NasaAppConstants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)
        setupView()
    }

    private fun setupView() {

        setupToolbar()

        var item = intent.getSerializableExtra(NasaAppConstants.EXTRA_DATA) as SearchedItems
        Glide.with(this).load(item.links[0].href).into(iv_nasa_image)

        item.data[0].let {
            tv_title.text = it.title
            tv_description.text = it.description

            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            tv_date_created.text = formatter.format(parser.parse(it.dateCreated))

        }
    }

    private fun setupToolbar() {

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = getString(R.string.details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}