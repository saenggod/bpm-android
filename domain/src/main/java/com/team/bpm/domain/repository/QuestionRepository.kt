package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.ResponseStateV2
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestionDetail(questionId: Int): Flow<ResponseStateV2<Question>>
}