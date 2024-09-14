class MeetingViewModelTest {

    /*@Mock
    private lateinit var meetingApiService: MeetingApiService

    private lateinit var meetingViewModel: MeetingViewModel

    private lateinit var meetingsFromData: List<Meeting>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        meetingViewModel = MeetingViewModel() // Pass the mock service
        meetingViewModel.meetingApiService = meetingApiService

        //Get data from json file
        meetingsFromData = getMeetingsFromDataSource()
        println("meetingsFromData: $meetingsFromData")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after the test
    }

    @Test
    fun `getMeetingData success`() = runTest {
        // Mock successful API response
        val mockMeetings = meetingsFromData
        Mockito.`when`(meetingApiService.getMeetings()).thenReturn(mockMeetings)

        meetingViewModel.getMeetingData()

        // Wait for the coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            MeetingUiState.Success(
                Meeting(
                    meetingName = "Belgian Grand Prix",
                    location = "Spa-Francorchamps",
                    countryName = "Belgium",
                    dateStart = "2024-07-26T11:30:00+00:00",
                    gmtOffset = "02:00:00",
                    year = 2024
                )
            ),
            meetingViewModel.meetingUiState
        )
    }

//    @Test
//    fun `getMeetingData loading`() = runTest {
//        Mockito.`when`(meetingApiService.getMeetings()).thenReturn(null)
//
//        assertEquals(MeetingUiState.Loading, meetingViewModel.meetingUiState)
//    }

    @Test
    fun `getMeetingData error`() = runTest {
        Mockito.`when`(meetingApiService.getMeetings()).thenReturn(null)

        meetingViewModel.getMeetingData()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(MeetingUiState.Error, meetingViewModel.meetingUiState)
    }

    //Read and return Meetings data from Json
    private fun getMeetingsFromDataSource(): List<Meeting> {

        //Create Json object that ignore unknown keys
        val json = Json {
            ignoreUnknownKeys = true
        }

        val inputStream: InputStream =
            javaClass.classLoader!!.getResourceAsStream("data_meetings.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return json.decodeFromString<List<Meeting>>(jsonString)
    }*/
}