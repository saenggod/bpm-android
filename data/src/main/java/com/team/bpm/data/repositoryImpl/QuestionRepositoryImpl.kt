package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.QuestionResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : QuestionRepository {
    override suspend fun fetchQuestionDetail(questionId: Int): Flow<Question> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchQuestionDetail(questionId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}