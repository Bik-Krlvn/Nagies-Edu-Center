package com.cheise_proj.presentation.viewmodel.files

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cheise_proj.domain.repository.FilesRepository
import com.cheise_proj.domain.usecase.files.DeleteAssignmentTask
import com.cheise_proj.domain.usecase.files.GetAssignmentTask
import com.cheise_proj.presentation.extensions.asAssignmentEntityList
import com.cheise_proj.presentation.model.vo.STATUS
import com.cheise_proj.presentation.utils.IServerPath
import com.cheise_proj.presentation.utils.TestFilesGenerator
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class AssignmentViewModelTest {


    companion object {
        private const val IDENTIFIER: String = "1"
        private const val ERROR_MESSAGE = "An error occurred"
        private const val IS_SUCCESS = true
        private const val IS_FAILURE = false
    }

    private lateinit var assignmentViewModel: AssignmentViewModel
    private lateinit var getAssignmentTask: GetAssignmentTask
    private lateinit var deleteAssignmentTask: DeleteAssignmentTask

    @Mock
    lateinit var filesRepository: FilesRepository

    @Mock
    lateinit var path: IServerPath

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getAssignmentTask =
            GetAssignmentTask(filesRepository, Schedulers.trampoline(), Schedulers.trampoline())
        deleteAssignmentTask =
            DeleteAssignmentTask(filesRepository, Schedulers.trampoline(), Schedulers.trampoline())

        assignmentViewModel = AssignmentViewModel(
            getAssignmentTask,
            path,
            deleteAssignmentTask
        )
    }

    @Test
    fun `Delete assignment success`() {
        val actual = IS_SUCCESS
        val url = "test url"
        Mockito.`when`(filesRepository.deleteAssignment(IDENTIFIER, url))
            .thenReturn(Observable.just(actual))
        val deleteLive = assignmentViewModel.deleteAssignment(IDENTIFIER, url)
        deleteLive.observeForever { }
        assertTrue(
            deleteLive.value?.status == STATUS.SUCCESS && deleteLive.value?.data == actual
        )
        Mockito.verify(filesRepository, times(1)).deleteAssignment(IDENTIFIER, url)
    }

    @Test
    fun `Delete assignment with errors`() {
        val actual = ERROR_MESSAGE
        val url = "test url"
        Mockito.`when`(filesRepository.deleteAssignment(IDENTIFIER, url))
            .thenReturn(Observable.error(Throwable(actual)))
        val deleteLive = assignmentViewModel.deleteAssignment(IDENTIFIER, url)
        deleteLive.observeForever { }
        assertTrue(
            deleteLive.value?.status == STATUS.ERROR && deleteLive.value?.message == actual
        )
        Mockito.verify(filesRepository, times(1)).deleteAssignment(IDENTIFIER, url)
    }

    @Test
    fun `Delete assignment failed`() {
        val actual = IS_FAILURE
        val url = "test url"
        Mockito.`when`(filesRepository.deleteAssignment(IDENTIFIER, url))
            .thenReturn(Observable.just(actual))
        val deleteLive = assignmentViewModel.deleteAssignment(IDENTIFIER, url)
        deleteLive.observeForever { }
        assertTrue(
            deleteLive.value?.status == STATUS.SUCCESS && deleteLive.value?.data == actual
        )
        Mockito.verify(filesRepository, times(1)).deleteAssignment(IDENTIFIER, url)
    }

    @Test
    fun `Get all assignment success`() {
        val actual = TestFilesGenerator.getAssignments()

        Mockito.`when`(filesRepository.getAssignments())
            .thenReturn(Observable.just(actual.asAssignmentEntityList()))
        val assignmentLive = assignmentViewModel.getAssignments()
        assignmentLive.observeForever { }
        assertTrue(
            assignmentLive.value?.status == STATUS.SUCCESS && assignmentLive.value?.data?.size == actual.size
        )
        Mockito.verify(filesRepository, times(1)).getAssignments()
    }

    @Test
    fun `Get all assignment with errors success`() {
        Mockito.`when`(filesRepository.getAssignments())
            .thenReturn(Observable.error(Throwable(ERROR_MESSAGE)))
        val assignmentLive = assignmentViewModel.getAssignments()
        assignmentLive.observeForever { }
        assertTrue(
            assignmentLive.value?.status == STATUS.ERROR && assignmentLive.value?.message == ERROR_MESSAGE
        )
        Mockito.verify(filesRepository, times(1)).getAssignments()
    }

    @Test
    fun `Get assignment success`() {
        val actual = TestFilesGenerator.getAssignment()
        Mockito.`when`(filesRepository.getAssignment(IDENTIFIER))
            .thenReturn(
                Observable.just(
                    arrayListOf(actual).asAssignmentEntityList()
                )
            )
        val assignmentLive = assignmentViewModel.getAssignment(IDENTIFIER)
        assignmentLive.observeForever { }
        assertTrue(
            assignmentLive.value?.status == STATUS.SUCCESS && assignmentLive.value?.data == actual
        )
        Mockito.verify(filesRepository, times(1)).getAssignment(IDENTIFIER)
    }

    @Test
    fun `Get assignment with errors success`() {
        Mockito.`when`(filesRepository.getAssignment(IDENTIFIER))
            .thenReturn(Observable.error(Throwable(ERROR_MESSAGE)))
        val assignmentLive = assignmentViewModel.getAssignment(IDENTIFIER)
        assignmentLive.observeForever { }
        assertTrue(
            assignmentLive.value?.status == STATUS.ERROR && assignmentLive.value?.message == ERROR_MESSAGE
        )
        Mockito.verify(filesRepository, times(1)).getAssignment(IDENTIFIER)
    }

}