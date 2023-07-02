package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.CommentRequest
import com.team.bpm.data.model.request.ReportRequest
import com.team.bpm.data.model.response.CommentListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.CommentResponse.Companion.toDataModel
import com.team.bpm.data.model.response.QuestionListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.QuestionResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.model.QuestionList
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : QuestionRepository {
    override suspend fun fetchQuestionList(
        limit: Int,
        offset: Int,
        slug: String?
    ): Flow<QuestionList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchQuestionList(limit, offset, slug)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendQuestion(
        title: String,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<Question> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestion(
                    title,
                    content,
                    imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "files",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    }
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun deleteQuestion(questionId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteQuestion(questionId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchQuestionDetail(questionId: Int): Flow<Question> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchQuestionDetail(questionId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendQuestionReport(
        questionId: Int,
        reason: String
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestionReport(questionId, ReportRequest(reason))
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun sendQuestionLike(questionId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestionLike(questionId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteQuestionLike(questionId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteQuestionLike(questionId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun sendQuestionComment(
        questionId: Int,
        parentId: Int?,
        comment: String
    ): Flow<Comment> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestionComment(questionId, CommentRequest(parentId, comment))
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun deleteQuestionComment(
        questionId: Int,
        commentId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteQuestionComment(questionId, commentId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchQuestionCommentList(questionId: Int): Flow<CommentList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchQuestionCommentList(questionId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendQuestionCommentReport(
        questionId: Int,
        commentId: Int,
        reason: String
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestionCommentReport(questionId, commentId, ReportRequest(reason))
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun sendQuestionCommentLike(
        questionId: Int,
        commentId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendQuestionCommentLike(questionId, commentId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteQuestionCommentLike(
        questionId: Int,
        commentId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteQuestionCommentLike(questionId, commentId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchMyQuestionList(
        page: Int,
        size: Int,
    ): Flow<QuestionList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchMyQuestionList(page, size)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}