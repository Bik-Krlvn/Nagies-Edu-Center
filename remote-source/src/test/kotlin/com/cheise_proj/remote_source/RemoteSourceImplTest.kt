package com.cheise_proj.remote_source

import com.cheise_proj.remote_source.api.ApiService
import com.cheise_proj.remote_source.extensions.asDTO
import com.cheise_proj.remote_source.extensions.asDTOList
import com.cheise_proj.remote_source.extensions.asData
import com.cheise_proj.remote_source.extensions.asDataList
import com.cheise_proj.remote_source.model.request.LoginRequest
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import utils.TestFilesGenerator
import utils.TestMessageGenerator
import utils.TestPeopleGenerator
import utils.TestUserGenerator

@RunWith(JUnit4::class)
class RemoteSourceImplTest {

    companion object {
        private const val NETWORK_STATE = "No internet connection"
        private const val NO_NETWORK_ERROR = "Unable to resolve host"
        private const val IDENTIFIER = "1"
        private const val HTTP_OK = 200
        private const val IS_SUCCESS = true
    }

    private lateinit var remoteSourceImpl: RemoteSourceImpl

    private val username = "test username"
    private val password = "test password"
    private val parent = "parent role"

    @Mock
    private lateinit var apiService: ApiService


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteSourceImpl = RemoteSourceImpl(apiService)
    }


    //region SEND MESSAGE
    //region MESSAGE
    @Test
    fun `Send message success`() {
        val messageRequest = TestMessageGenerator.getMessageRequest()
        val sentDto = TestMessageGenerator.getSentMessageDto()
        val actual = IS_SUCCESS
        Mockito.`when`(apiService.sendMessage(messageRequest)).thenReturn(Observable.just(sentDto))
        with(messageRequest) {
            remoteSourceImpl.sendMessage(content, receiverName, identifier)
                .test()
                .assertSubscribed()
                .assertValueCount(1)
                .assertValue { it == actual }
                .assertComplete()
        }
    }

    @Test
    fun `Send message with no network`() {
        val messageRequest = TestMessageGenerator.getMessageRequest()
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.sendMessage(messageRequest)).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        with(messageRequest) {
            remoteSourceImpl.sendMessage(content, receiverName, identifier)
                .test()
                .assertSubscribed()
                .assertError {
                    it.localizedMessage == actual
                }
                .assertNotComplete()
        }
    }

    //endregion

    //region COMPLAINT
    @Test
    fun `Send complaint success`() {
        val complaintResult = TestMessageGenerator.getComplaintRequest()
        val sentMessageDto = TestMessageGenerator.getSentMessageDto()
        val actual = IS_SUCCESS
        Mockito.`when`(apiService.sendComplaint(complaintResult))
            .thenReturn(Observable.just(sentMessageDto))
        with(complaintResult) {
            remoteSourceImpl.sendComplaint(content, identifier)
                .test()
                .assertSubscribed()
                .assertValueCount(1)
                .assertValue { it == actual }
                .assertComplete()
        }
    }

    @Test
    fun `Send complaint with no network`() {
        val complaintResult = TestMessageGenerator.getComplaintRequest()
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.sendComplaint(complaintResult)).thenReturn(
            Observable.error(
                Throwable(NO_NETWORK_ERROR)
            )
        )
        with(complaintResult) {
            remoteSourceImpl.sendComplaint(content, identifier)
                .test()
                .assertSubscribed()
                .assertError { it.localizedMessage == actual }
                .assertNotComplete()
        }
    }

    //endregion

    //endregion

    //region DELETE FILES
    //region REPORT
    @Test
    fun `Delete report from remote success`() {
        val deleteDto = TestFilesGenerator.getDeleteDto()
        val actual = IS_SUCCESS
        val url = "test url"
        Mockito.`when`(apiService.deleteReport(IDENTIFIER, url))
            .thenReturn(Observable.just(deleteDto))
        remoteSourceImpl.deleteReport(IDENTIFIER, url)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual
            }
            .assertComplete()
    }

    @Test
    fun `Delete report from remote with no network`() {
        val actual = NETWORK_STATE
        val url = "test url"
        Mockito.`when`(apiService.deleteReport(IDENTIFIER, url))
            .thenReturn(Observable.error(Throwable(NO_NETWORK_ERROR)))
        remoteSourceImpl.deleteReport(IDENTIFIER, url)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }
    //endregion

    //region ASSIGNMENT
    @Test
    fun `Delete assignment from remote success`() {
        val deleteDto = TestFilesGenerator.getDeleteDto()
        val actual = IS_SUCCESS
        val url = "test url"
        Mockito.`when`(apiService.deleteAssignment(IDENTIFIER, url))
            .thenReturn(Observable.just(deleteDto))
        remoteSourceImpl.deleteAssignment(IDENTIFIER, url)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual
            }
            .assertComplete()
    }

    @Test
    fun `Delete assignment from remote with no network`() {
        val actual = NETWORK_STATE
        val url = "test url"
        Mockito.`when`(apiService.deleteAssignment(IDENTIFIER, url))
            .thenReturn(Observable.error(Throwable(NO_NETWORK_ERROR)))
        remoteSourceImpl.deleteAssignment(IDENTIFIER, url)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }
    //endregion
    //endregion

    //region PEOPLE
    @Test
    fun `Get class teacher from remote success`() {
        val teacher = TestPeopleGenerator.getPeopleDto()
        Mockito.`when`(apiService.getClassTeacher()).thenReturn(Observable.just(teacher))
        remoteSourceImpl.getPeople(TYPE_TEACHER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == teacher.teacher!!.asDataList()
            }
            .assertComplete()
    }

    @Test
    fun `Get class student from remote success`() {
        val student = TestPeopleGenerator.getPeopleDto()
        Mockito.`when`(apiService.getClassStudent()).thenReturn(Observable.just(student))
        remoteSourceImpl.getPeople(TYPE_STUDENT)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == student.student.asDataList()
            }
            .assertComplete()
    }

    @Test
    fun `Get people from remote with no network`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getClassTeacher()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getPeople(TYPE_TEACHER)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }

    //endregion

    //region FILES

    //region VIDEO
    @Test
    fun `Upload video success`() {
        val filePart = TestFilesGenerator.getFilePart()
        val actual = TestFilesGenerator.getUploadDto()
        val status = HTTP_OK
        Mockito.`when`(apiService.uploadVideo(filePart)).thenReturn(Observable.just(actual))
        remoteSourceImpl.uploadVideo(filePart)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == status
            }
            .assertComplete()
    }

    @Test
    fun `Upload video to remote with no network success`() {
        val part = TestFilesGenerator.getFilePart()
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.uploadVideo(part)).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.uploadVideo(part)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).uploadVideo(part)
    }
    //endregion


    //region REPORT
    @Test
    fun `Upload report success`() {
        val part = TestFilesGenerator.getFilePart()
        val actual = TestFilesGenerator.getUploadDto()
        val status = HTTP_OK
        Mockito.`when`(apiService.uploadReport(part, part, part))
            .thenReturn(Observable.just(actual))
        remoteSourceImpl.uploadReport(part, part, part)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == status
            }
            .assertComplete()

    }

    @Test
    fun `Upload report to remote with no network success`() {
        val part = TestFilesGenerator.getFilePart()
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.uploadReport(part, part, part)).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.uploadReport(part, part, part)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).uploadReport(part, part, part)
    }
    //endregion

    //region ASSIGNMENT
    @Test
    fun `Upload assignment success`() {
        val filePart = TestFilesGenerator.getFilePart()
        val actual = TestFilesGenerator.getUploadDto()
        val status = HTTP_OK
        Mockito.`when`(apiService.uploadAssignment(filePart)).thenReturn(Observable.just(actual))
        remoteSourceImpl.uploadAssignment(filePart)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == status
            }
            .assertComplete()
    }

    @Test
    fun `Upload assignment to remote with no network success`() {
        val part = TestFilesGenerator.getFilePart()
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.uploadAssignment(part)).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.uploadAssignment(part)
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).uploadAssignment(part)
    }
    //endregion

    //region RECEIPT
    @Test
    fun `Upload receipt success`() {
        val filePart = TestFilesGenerator.getFilePart()
        val actual = TestFilesGenerator.getUploadDto()
        val status = HTTP_OK
        Mockito.`when`(apiService.uploadReceipt(filePart)).thenReturn(Observable.just(actual))
        remoteSourceImpl.uploadReceipt(filePart)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == status
            }
            .assertComplete()
    }
    //endregion


    //region VIDEO
    @Test
    fun `Get all videos from remote success`() {
        val actual = TestFilesGenerator.getVideosDto()
        Mockito.`when`(apiService.getVideos()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getVideo()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDTOList()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getVideos()
    }

    @Test
    fun `Get all videos from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getVideos()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getVideo()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).getVideos()
    }
    //endregion


    //region BILL
    @Test
    fun `Get all bills from remote success`() {
        val actual = TestFilesGenerator.getBillsDto()
        Mockito.`when`(apiService.getBilling()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getBill()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDTOList()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getBilling()
    }

    @Test
    fun `Get all bill from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getBilling()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getBill()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).getBilling()
    }
    //endregion

    //region REPORT
    @Test
    fun `Get all timetables from remote success`() {
        val actual = TestFilesGenerator.getTimeTablesDto()
        Mockito.`when`(apiService.getTimeTable()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getTimeTable()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDTOList()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getTimeTable()
    }

    @Test
    fun `Get all timetables from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getTimeTable()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getTimeTable()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).getTimeTable()
    }
    //endregion


    //region REPORT
    @Test
    fun `Get all reports from remote success`() {
        val actual = TestFilesGenerator.getReportsDto()
        Mockito.`when`(apiService.getReport()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getReport()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDTOList()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getReport()
    }

    @Test
    fun `Get all reports from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getReport()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getReport()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).getReport()
    }
    //endregion

    //region ASSIGNMENT
    @Test
    fun `Get all assignment from remote success`() {
        val actual = TestFilesGenerator.getAssignmentDto()
        Mockito.`when`(apiService.getAssignment()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getAssignment()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDTOList()
            }
            .assertComplete()
    }

    @Test
    fun `Get assignment from network with no network throws an exception`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getAssignment()).thenReturn(Observable.error(Throwable(actual)))
        remoteSourceImpl.getAssignment()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }
    //endregion

    //region CIRCULAR
    @Test
    fun `Get all circulars from remote success`() {
        val actual = TestFilesGenerator.getCircularDto()
        Mockito.`when`(apiService.getCircular()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getCircular()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.data.asDataList()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getCircular()
    }

    @Test
    fun `Get all circulars from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getCircular()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getCircular()
            .test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
        Mockito.verify(apiService, times(1)).getCircular()
    }
    //endregion
    //endregion

    //region MESSAGES

    //region SENT COMPLAINT
    //complaint
    @Test
    fun `Get sent complaints from remote success`() {
        val actual = TestMessageGenerator.getComplaintDto()
        Mockito.`when`(apiService.getSentComplaint()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getSentComplaint().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.complaint.asDataList()
            }
            .assertComplete()
    }

    @Test
    fun `Get sent complaints from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getSentComplaint()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getSentComplaint().test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }
    //endregion

    //complaint
    @Test
    fun `Get complaints from remote success`() {
        val actual = TestMessageGenerator.getComplaintDto()
        Mockito.`when`(apiService.getComplaint()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getComplaint().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.complaint.asDataList()
            }
            .assertComplete()
    }

    @Test
    fun `Get complaints from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getComplaint()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getComplaint().test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }

    //region SENT MESSAGE

    @Test
    fun `Get sent messages from remote success`() {
        val actual = TestMessageGenerator.getMessageDto()
        Mockito.`when`(apiService.getSentMessages()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getSentMessages().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.message.asDTOList()
            }
            .assertComplete()
    }

    @Test
    fun `Get sent messages from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getSentMessages()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getSentMessages().test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }

    //endregion

    //message
    @Test
    fun `Get messages from remote success`() {
        val actual = TestMessageGenerator.getMessageDto()
        Mockito.`when`(apiService.getMessages()).thenReturn(Observable.just(actual))
        remoteSourceImpl.getMessages().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.message.asDTOList()
            }
            .assertComplete()
    }

    @Test
    fun `Get messages from remote with no network success`() {
        val actual = NETWORK_STATE
        Mockito.`when`(apiService.getMessages()).thenReturn(
            Observable.error(
                Throwable(
                    NO_NETWORK_ERROR
                )
            )
        )
        remoteSourceImpl.getMessages().test()
            .assertSubscribed()
            .assertError {
                it.localizedMessage == actual
            }
            .assertNotComplete()
    }
    //endregion

    //region USERS
    @Test
    fun `Authenticate parent role success`() {
        val response = TestUserGenerator.user()
        Mockito.`when`(apiService.getAuthenticateUser(parent, LoginRequest(username, password)))
            .thenReturn(
                Single.just(response)
            )
        remoteSourceImpl.authenticateUser(parent, username, password).test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it.token == it.asDTO().token
            }
            .assertComplete()
        Mockito.verify(apiService, times(1))
            .getAuthenticateUser(parent, LoginRequest(username, password))
    }

    @Test
    fun `Authenticate parent role on error`() {
        val errorMsg = "HTTP 401"
        val actual = "username or password invalid"
        Mockito.`when`(apiService.getAuthenticateUser(parent, LoginRequest(username, password)))
            .thenReturn(
                Single.error(Throwable(errorMsg))
            )
        remoteSourceImpl.authenticateUser(parent, username, password).test()
            .assertSubscribed()
            .assertErrorMessage(actual)
        Mockito.verify(apiService, times(1))
            .getAuthenticateUser(parent, LoginRequest(username, password))
    }

    @Test
    fun `Get student profile success`() {
        val actual = TestUserGenerator.getProfile()
        Mockito.`when`(apiService.getProfile()).thenReturn(Single.just(actual))
        remoteSourceImpl.getProfile().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.student!!.asData()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getProfile()
    }

    @Test
    fun `Get teacher profile success`() {
        val actual = TestUserGenerator.getProfile()
        Mockito.`when`(apiService.getProfile()).thenReturn(Single.just(actual))
        remoteSourceImpl.getProfile().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.teacher.asData()
            }
            .assertComplete()
        Mockito.verify(apiService, times(1)).getProfile()
    }

    @Test
    fun `Update user password success`() {
        val request = TestUserGenerator.getChangePasswordRequest()
        val passwordDto = TestUserGenerator.getChangePassword()
        val actual = true
        with(request) {
            Mockito.`when`(apiService.changeAccountPassword(this))
                .thenReturn(Observable.just(passwordDto))
            remoteSourceImpl.changePassword(oldPassword, newPassword)
                .test()
                .assertSubscribed()
                .assertValueCount(1)
                .assertValue {
                    it == actual
                }
                .assertComplete()
        }
    }

    @Test
    fun `Update user password with no network`() {
        val request = TestUserGenerator.getChangePasswordRequest()
        val actual = NETWORK_STATE
        with(request) {
            Mockito.`when`(apiService.changeAccountPassword(this))
                .thenReturn(Observable.error(Throwable(NO_NETWORK_ERROR)))
            remoteSourceImpl.changePassword(oldPassword, newPassword)
                .test()
                .assertError {
                    it.localizedMessage == actual
                }
                .assertNotComplete()
        }
    }

    //endregion
}