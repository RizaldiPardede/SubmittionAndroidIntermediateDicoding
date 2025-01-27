package com.example.submissionpertamaai.Paging


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionpertamaai.Api.ApiService
import com.example.submissionpertamaai.ListStoryItem
import com.example.submissionpertamaai.MainActivity


class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1

    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getListStoriesPage("Bearer ${MainActivity.TOKEN}", position,params.loadSize).listStory
            Log.d("Paging Source", "jumlah data adalah ${responseData}")

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}