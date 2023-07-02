package com.team.bpm.data.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.team.bpm.data.model.response.NotificationListResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Notification

class NotificationPagingSource(
    private val mainApi: MainApi,
    private val pageSize: Int = 30 // TODO : Const로 관리
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        return try {
            val page = params.key ?: 0

            val response = mainApi.fetchNotificationList(page)
            val responseBody = response.body()

            LoadResult.Page(
                data = responseBody?.response?.toDataModel()?.alarmResponseList ?: emptyList(),
                prevKey = if (page > 0) page - 1 else null,
                nextKey = if ((responseBody?.response?.toDataModel()?.alarmCount
                        ?: 0) > page + responseBody?.response?.toDataModel()?.alarmResponseList.orEmpty().size
                ) {
                    page + pageSize
                } else {
                    null
                }
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}