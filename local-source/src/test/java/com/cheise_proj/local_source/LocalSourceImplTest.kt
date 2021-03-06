package com.cheise_proj.local_source

import com.cheise_proj.local_source.db.dao.FilesDao
import com.cheise_proj.local_source.db.dao.MessageDao
import com.cheise_proj.local_source.db.dao.PeopleDao
import com.cheise_proj.local_source.db.dao.UserDao
import com.cheise_proj.local_source.extensions.*
import com.cheise_proj.local_source.utils.TestFilesGenerator
import com.cheise_proj.local_source.utils.TestMessageGenerator
import com.cheise_proj.local_source.utils.TestPeopleGenerator
import com.cheise_proj.local_source.utils.TestUserGenerator
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

@RunWith(JUnit4::class)
class LocalSourceImplTest {
    private lateinit var localSourceImpl: LocalSourceImpl

    @Mock
    lateinit var userDao: UserDao

    @Mock
    lateinit var messageDao: MessageDao

    @Mock
    lateinit var filesDao: FilesDao

    @Mock
    lateinit var peopleDao: PeopleDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        localSourceImpl = LocalSourceImpl(userDao, messageDao, filesDao, peopleDao)
    }

    //region DELETE FILES

    //region REPORT
    @Test
    fun `Delete report from local success`() {
        localSourceImpl.deleteReport(IDENTIFIER)
        Mockito.verify(filesDao, times(1)).deleteReportByIdentifier(IDENTIFIER)
    }
    //endregion

    //region ASSIGNMENT
    @Test
    fun `Delete assignment from local success`() {
        localSourceImpl.deleteAssignment(IDENTIFIER)
        Mockito.verify(filesDao, times(1)).deleteAssignmentByIdentifier(IDENTIFIER)
    }
    //endregion

    //endregion

    //region people
    @Test
    fun `Get people list success`() {
        val actual = TestPeopleGenerator.getPeople()
        Mockito.`when`(peopleDao.getPeopleList()).thenReturn(Observable.just(actual))
        localSourceImpl.getPeopleList()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asLocalList()
            }
            .assertComplete()
    }

    @Test
    fun `Get people success`() {
        val actual = TestPeopleGenerator.getPeople()[0]
        Mockito.`when`(peopleDao.getPeople(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getPeople(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asData()
            }
            .assertComplete()
    }

    @Test
    fun `Save people local success`() {
        val actual = TestPeopleGenerator.getPeople()
        localSourceImpl.savePeople(actual.asLocalList())
        Mockito.verify(peopleDao, times(1)).clearAndInsertPeople(actual)
    }

    //endregion

    //region VIDEO
    @Test
    fun `Get all videos from local success`() {
        val actual = TestFilesGenerator.getVideos()
        Mockito.`when`(filesDao.getVideos()).thenReturn(Observable.just(actual))
        localSourceImpl.getVideos()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asVideoLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getVideos()
    }

    @Test
    fun `Get video from local success`() {
        val actual = TestFilesGenerator.getVideo()
        Mockito.`when`(filesDao.getVideo(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getVideo(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asVideoData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getVideo(IDENTIFIER)
    }

    @Test
    fun `Save videos to local success`() {
        val video = TestFilesGenerator.getVideos()
        Mockito.doNothing().`when`(filesDao).clearAndInsertVideo(video)
        localSourceImpl.saveVideo(video.asVideoLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertVideo(video)
    }
    //endregion


    //region BILL
    @Test
    fun `Get all bills from local success`() {
        val actual = TestFilesGenerator.getBills()
        Mockito.`when`(filesDao.getBills()).thenReturn(Observable.just(actual))
        localSourceImpl.getBills()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asBillLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getBills()
    }

    @Test
    fun `Get bill from local success`() {
        val actual = TestFilesGenerator.getBill()
        Mockito.`when`(filesDao.getBill(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getBill(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asBillData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getBill(IDENTIFIER)
    }

    @Test
    fun `Save bills to local success`() {
        val bill = TestFilesGenerator.getBills()
        Mockito.doNothing().`when`(filesDao).clearAndInsertBill(bill)
        localSourceImpl.saveBill(bill.asBillLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertBill(bill)
    }
    //endregion

    //region TIMETABLE
    @Test
    fun `Get all timetables from local success`() {
        val actual = TestFilesGenerator.getTimeTables()
        Mockito.`when`(filesDao.getTimeTables()).thenReturn(Observable.just(actual))
        localSourceImpl.getTimeTables()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asTimeTableLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getTimeTables()
    }

    @Test
    fun `Get timetable from local success`() {
        val actual = TestFilesGenerator.getTimeTable()
        Mockito.`when`(filesDao.getTimeTable(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getTimeTable(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asTimeTableData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getTimeTable(IDENTIFIER)
    }

    @Test
    fun `Save timetable to local success`() {
        val timetable = TestFilesGenerator.getTimeTables()
        Mockito.doNothing().`when`(filesDao).clearAndInsertTimeTable(timetable)
        localSourceImpl.saveTimeTable(timetable.asTimeTableLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertTimeTable(timetable)
    }
    //endregion


    //region REPORT
    @Test
    fun `Get all reports from local success`() {
        val actual = TestFilesGenerator.getReports()
        Mockito.`when`(filesDao.getReports()).thenReturn(Observable.just(actual))
        localSourceImpl.getReports()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asReportLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getReports()
    }

    @Test
    fun `Get report from local success`() {
        val actual = TestFilesGenerator.getReport()
        Mockito.`when`(filesDao.getReport(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getReport(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asReportData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getReport(IDENTIFIER)
    }

    @Test
    fun `Save report to local success`() {
        val report = TestFilesGenerator.getReports()
        Mockito.doNothing().`when`(filesDao).clearAndInsertReport(report)
        localSourceImpl.saveReport(report.asReportLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertReport(report)
    }
    //endregion


    //region ASSIGNMENT
    @Test
    fun `Get all assignment from local success`() {
        val actual = TestFilesGenerator.getAssignments()
        Mockito.`when`(filesDao.getAssignments()).thenReturn(Observable.just(actual))
        localSourceImpl.getAssignments()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asAssignmentLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getAssignments()
    }

    @Test
    fun `Get assignment from local success`() {
        val actual = TestFilesGenerator.getAssignment()
        Mockito.`when`(filesDao.getAssignment(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getAssignment(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asAssignmentData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getAssignment(IDENTIFIER)
    }

    @Test
    fun `Save assignment to local success`() {
        val assignment = TestFilesGenerator.getAssignments()
        Mockito.doNothing().`when`(filesDao).clearAndInsertAssignment(assignment)
        localSourceImpl.saveAssignment(assignment.asAssignmentLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertAssignment(assignment)
    }
    //endregion


    //region CIRCULAR
    @Test
    fun `Get all circular from local success`() {
        val actual = TestFilesGenerator.getCirculars()
        Mockito.`when`(filesDao.getCirculars()).thenReturn(Observable.just(actual))
        localSourceImpl.getCirculars()
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asCircularLocalList()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getCirculars()
    }

    @Test
    fun `Get circular from local success`() {
        val actual = TestFilesGenerator.getCircular()
        Mockito.`when`(filesDao.getCircular(IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getCircular(IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asCircularData()
            }
            .assertComplete()
        Mockito.verify(filesDao, times(1)).getCircular(IDENTIFIER)
    }

    @Test
    fun `Save circular to local success`() {
        val circular = TestFilesGenerator.getCirculars()
        Mockito.doNothing().`when`(filesDao).clearAndInsertCircular(circular)
        localSourceImpl.saveCircular(circular.asCircularLocalList())
        Mockito.verify(filesDao, times(1)).clearAndInsertCircular(circular)
    }
    //endregion

    //region MESSAGE
    //complaint
    @Test
    fun `Get all complaints from local success`() {
        val actual = TestMessageGenerator.getComplaint()
        Mockito.`when`(messageDao.getComplaints()).thenReturn(Observable.just(actual))
        localSourceImpl.getComplaints().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                println(it)
                it == actual.asDataList()
            }
            .assertComplete()
        Mockito.verify(messageDao, times(1)).getComplaints()
    }

    @Test
    fun `Get complaint from local success`() {
        val actual = TestMessageGenerator.getComplaint()[0]
        Mockito.`when`(messageDao.getComplaint(MESSAGE_IDENTIFIER.toString()))
            .thenReturn(Single.just(actual))
        localSourceImpl.getComplaint(MESSAGE_IDENTIFIER.toString())
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                println(it)
                it == actual.asData()
            }
            .assertComplete()
        Mockito.verify(messageDao, times(1)).getComplaint(MESSAGE_IDENTIFIER.toString())

    }

    @Test
    fun `Save complaint to local success`() {
        val actual = TestMessageGenerator.getComplaint()
        Mockito.doNothing().`when`(messageDao).clearAndInsertComplaints(actual)
        localSourceImpl.saveComplaint(actual.asDataList())
        Mockito.verify(messageDao, times(1)).clearAndInsertComplaints(actual)
    }

    //message
    @Test
    fun `Get all messages from local success`() {
        val actual = TestMessageGenerator.getMessages()
        Mockito.`when`(messageDao.getMessages()).thenReturn(Observable.just(actual))
        localSourceImpl.getMessages().test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                println(it)
                it == actual.asDataList()
            }
            .assertComplete()
        Mockito.verify(messageDao, times(1)).getMessages()
    }

    @Test
    fun `Get message from local success`() {
        val actual = TestMessageGenerator.getMessages()[0]
        Mockito.`when`(messageDao.getMessage(MESSAGE_IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getMessage(MESSAGE_IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                println(it)
                it == actual.asData()
            }
            .assertComplete()
        Mockito.verify(messageDao, times(1)).getMessage(MESSAGE_IDENTIFIER)

    }

    @Test
    fun `Save messages to local success`() {
        val actual = TestMessageGenerator.getMessages()
        Mockito.doNothing().`when`(messageDao).clearAndInsertMessages(actual)
        localSourceImpl.saveMessages(actual.asDataList())
        Mockito.verify(messageDao, times(1)).clearAndInsertMessages(actual)
    }
    //endregion


    //region USER
    @Test
    fun `Get user from local with credentials success`() {
        val actual = TestUserGenerator.user()
        with(actual) {
            Mockito.`when`(userDao.getUser(username, password)).thenReturn(Single.just(this))
            localSourceImpl.getUser(username, password)
                .test()
                .assertValueCount(1)
                .assertValue {
                    it == actual.asData()
                }
                .assertSubscribed()
                .assertComplete()
            Mockito.verify(userDao, times(1)).getUser(username, password)
        }
    }

    @Test
    fun `Save user to local success`() {
        val actual = TestUserGenerator.user()
        Mockito.doNothing().`when`(userDao).saveUser(actual)
        localSourceImpl.saveUser(actual.asData())
        Mockito.verify(userDao, times(1)).clearAndInsertUser(actual)
    }

    @Test
    fun `Get user profile from local success`() {
        val actual = TestUserGenerator.getProfile()
        Mockito.`when`(userDao.getProfile(USER_IDENTIFIER)).thenReturn(Single.just(actual))
        localSourceImpl.getProfile(USER_IDENTIFIER)
            .test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == actual.asData()
            }
            .assertComplete()
        Mockito.verify(userDao, times(1)).getProfile(USER_IDENTIFIER)
    }

    @Test
    fun `Save user profile to local success`() {
        val actual = TestUserGenerator.getProfile()
        Mockito.doNothing().`when`(userDao).saveProfile(actual)
        localSourceImpl.saveProfile(actual.asData())
        Mockito.verify(userDao, times(1)).saveProfile(actual)
    }

    @Test
    fun `Update local user password success`() {
        val newPassword = "12345"
        localSourceImpl.updatePassword(IDENTIFIER, newPassword)
        Mockito.verify(userDao, times(1)).updatePassword(IDENTIFIER, newPassword)
    }
    //endregion

    companion object {
        private const val IDENTIFIER: String = "1"
        private const val MESSAGE_IDENTIFIER: Int = 1
        private const val USER_IDENTIFIER: String = "test identifier"
    }
}