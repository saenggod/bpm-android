package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestionDetail(questionId: Int): Flow<ResponseState<Question>>
}