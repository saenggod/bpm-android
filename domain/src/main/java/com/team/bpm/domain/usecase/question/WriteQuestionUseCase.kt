package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.Question
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteQuestionUseCase @Inject constructor(private val questionRepository: QuestionRepository) {
    suspend operator fun invoke(
        title: String,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<Question> {
        return questionRepository.sendQuestion(title, content, imageByteArrays)
    }
}