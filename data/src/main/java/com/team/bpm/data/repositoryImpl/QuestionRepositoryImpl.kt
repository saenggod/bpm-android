package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.QuestionResponse
import com.team.bpm.data.model.response.QuestionResponse.Companion.toDataModel
import com.team.bpm.data.network.BpmResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.ResponseStateV2
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : QuestionRepository {
    override suspend fun fetchQuestionDetail(questionId: Int): Flow<ResponseStateV2<Question>> {
        return flow {
            BpmResponseHandlerV2().handle<QuestionResponse>(key = "questionBoardResponse") {
                mainApi.fetchQuestionDetail(questionId)
            }.onEach { result ->
                result.data?.let {
                    emit(ResponseStateV2.Success(result.data.toDataModel()))
                }

                result.errors?.let {
                    emit(ResponseStateV2.Error(result.errors.code))
                } // TODO : Throw exception by error code
            }.collect()
        }
    }
}