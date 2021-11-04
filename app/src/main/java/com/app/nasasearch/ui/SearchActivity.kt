package com.app.nasasearch.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nasasearch.R
import com.app.nasasearch.api.NasaRepository
import com.app.nasasearch.others.NasaAppConstants
import com.app.nasasearch.others.NasaAppConstants.QUERY_PAGE_SIZE
import com.app.nasasearch.others.NasaAppConstants.SEARCH_TIME_DELAY
import com.app.nasasearch.ui.viewmodels.SearchViewModel
import com.app.nasasearch.ui.viewmodels.SearchViewModelProviderFactory
import com.app.newstoday.api.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.toolbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    lateinit var searchViewModel: SearchViewModel
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var searchViewModelProviderFactory: SearchViewModelProviderFactory

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)

        setupViewModel()

        setupTextChangedListener()

        setupRecyclerView()

        setupView()
    }

    /**
     * set onClickListener and observe changes to Live data object
     */
    private fun setupView() {
        searchAdapter.setOnItemClickListener {

            var intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(NasaAppConstants.EXTRA_DATA, it)
            startActivity(intent)
        }

        searchViewModel.searchedData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { searchResponse ->
                        searchAdapter.differ.submitList(searchResponse.collection.items.toList())
                        val totalPages =
                            searchResponse.collection.metadata.totalHits / QUERY_PAGE_SIZE + 2
                        isLastPage = searchViewModel.searchPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("SearchActivity", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupViewModel() {
        searchViewModel = ViewModelProvider(this, searchViewModelProviderFactory).get(
            SearchViewModel::class.java
        )
    }

    /**
     * set up listener for search query
     */
    private fun setupTextChangedListener() {
        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        searchViewModel.searchQuery(editable.toString())
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    /**
     * on scroll, fetch articles of next page
     */
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                searchViewModel.searchQuery(etSearch.text.toString())
                isScrolling = false
            } else {
                rvSearch.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    /**
     * Set up recycler view for fetched data
     */
    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            addOnScrollListener(this@SearchActivity.scrollListener)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home)
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}