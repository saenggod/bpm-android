package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestionDetail(questionId: Int): Flow<Question>
}