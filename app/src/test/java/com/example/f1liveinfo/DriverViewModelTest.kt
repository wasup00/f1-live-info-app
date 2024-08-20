import com.example.f1liveinfo.data.DriverApiService
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.ui.DriverViewModel
import com.example.f1liveinfo.ui.DriversUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DriverViewModelTest {

    @Mock
    private lateinit var driverApiService: DriverApiService

    private lateinit var driverViewModel: DriverViewModel

    private lateinit var driversFromData: List<Driver>

    private lateinit var positionsFromData: List<Position>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        driverViewModel = DriverViewModel() // Pass the mock service
        driverViewModel.driverApiService = driverApiService

        //Get data from json file
        driversFromData = getDataFromJson("data_drivers.json")
        println("driversFromData: $driversFromData")
        positionsFromData = getDataFromJson("data_positions.json")
        println("positionsFromData: $positionsFromData")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after the test
    }

    @Test
    fun `getDriversData success`() = runTest {
        // Mock successful API response
        val mockDrivers = driversFromData
        val mockPositions = positionsFromData
        Mockito.`when`(driverApiService.getDrivers()).thenReturn(mockDrivers)
        Mockito.`when`(driverApiService.getPositions()).thenReturn(mockPositions)

        driverViewModel.getDriversData()

        // Wait for the coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            DriversUiState.Success(
                expectedDriversAndPositions()
            ),
            driverViewModel.driversUiState
        )
    }

//    @Test
//    fun `getDriversData loading`() = runTest {
//        Mockito.`when`(driverApiService.getDrivers(LATEST)).thenReturn(listOf())
//
//        assertEquals(DriversUiState.Loading, driverViewModel.driversUiState)
//    }

    @Test
    fun `getDriversData error`() = runTest {
        Mockito.`when`(driverApiService.getDrivers()).thenReturn(null)

        driverViewModel.getDriversData()

        assertEquals(DriversUiState.Error, driverViewModel.driversUiState)
    }

    //Read and return Drivers data from Json
    private inline fun <reified T> getDataFromJson(fileName: String): List<T> {
        val json = Json { ignoreUnknownKeys = true }
        val inputStream: InputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return json.decodeFromString<List<T>>(jsonString)
    }

    private fun expectedDriversAndPositions(): List<Driver> {
        return listOf(
            Driver(
                lastName = "Verstappen",
                firstName = "Max",
                countryCode = "NED",
                teamName = "Red Bull Racing",
                driverNumber = 1,
                teamColor = "3671C6",
                currentPosition = 5
            ), Driver(
                lastName = "Sargeant",
                firstName = "Logan",
                countryCode = "USA",
                teamName = "Williams",
                driverNumber = 2,
                teamColor = "64C4FF",
                currentPosition = 19
            ), Driver(
                lastName = "Ricciardo",
                firstName = "Daniel",
                countryCode = "AUS",
                teamName = "RB",
                driverNumber = 3,
                teamColor = "6692FF",
                currentPosition = 11
            ), Driver(
                lastName = "Norris",
                firstName = "Lando",
                countryCode = "GBR",
                teamName = "McLaren",
                driverNumber = 4,
                teamColor = "FF8000",
                currentPosition = 6
            ), Driver(
                lastName = "Gasly",
                firstName = "Pierre",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 10,
                teamColor = "0093CC",
                currentPosition = 14
            ), Driver(
                lastName = "Perez",
                firstName = "Sergio",
                countryCode = "MEX",
                teamName = "Red Bull Racing",
                driverNumber = 11,
                teamColor = "3671C6",
                currentPosition = 8
            ), Driver(
                lastName = "Alonso",
                firstName = "Fernando",
                countryCode = "ESP",
                teamName = "Aston Martin",
                driverNumber = 14,
                teamColor = "229971",
                currentPosition = 9
            ), Driver(
                lastName = "Leclerc",
                firstName = "Charles",
                countryCode = "MON",
                teamName = "Ferrari",
                driverNumber = 16,
                teamColor = "E80020",
                currentPosition = 4
            ), Driver(
                lastName = "Stroll",
                firstName = "Lance",
                countryCode = "CAN",
                teamName = "Aston Martin",
                driverNumber = 18,
                teamColor = "229971",
                currentPosition = 12
            ), Driver(
                lastName = "Magnussen",
                firstName = "Kevin",
                countryCode = "DEN",
                teamName = "Haas F1 Team",
                driverNumber = 20,
                teamColor = "B6BABD",
                currentPosition = 15
            ), Driver(
                lastName = "Tsunoda",
                firstName = "Yuki",
                countryCode = "JPN",
                teamName = "RB",
                driverNumber = 22,
                teamColor = "6692FF",
                currentPosition = 17
            ), Driver(
                lastName = "Albon",
                firstName = "Alexander",
                countryCode = "THA",
                teamName = "Williams",
                driverNumber = 23,
                teamColor = "64C4FF",
                currentPosition = 13
            ), Driver(
                lastName = "Zhou",
                firstName = "Guanyu",
                countryCode = "CHN",
                teamName = "Kick Sauber",
                driverNumber = 24,
                teamColor = "52E252",
                currentPosition = 20
            ), Driver(
                lastName = "Hulkenberg",
                firstName = "Nico",
                countryCode = "GER",
                teamName = "Haas F1 Team",
                driverNumber = 27,
                teamColor = "B6BABD",
                currentPosition = 18
            ), Driver(
                lastName = "Ocon",
                firstName = "Esteban",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 31,
                teamColor = "0093CC",
                currentPosition = 10
            ), Driver(
                lastName = "Hamilton",
                firstName = "Lewis",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 44,
                teamColor = "27F4D2",
                currentPosition = 2
            ), Driver(
                lastName = "Sainz",
                firstName = "Carlos",
                countryCode = "ESP",
                teamName = "Ferrari",
                driverNumber = 55,
                teamColor = "E80020",
                currentPosition = 7
            ), Driver(
                lastName = "Russell",
                firstName = "George",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 63,
                teamColor = "27F4D2",
                currentPosition = 1
            ), Driver(
                lastName = "Bottas",
                firstName = "Valtteri",
                countryCode = "FIN",
                teamName = "Kick Sauber",
                driverNumber = 77,
                teamColor = "52E252",
                currentPosition = 16
            ), Driver(
                lastName = "Piastri",
                firstName = "Oscar",
                countryCode = "AUS",
                teamName = "McLaren",
                driverNumber = 81,
                teamColor = "FF8000",
                currentPosition = 3
            )
        ).sortedBy { it.currentPosition }
    }
}