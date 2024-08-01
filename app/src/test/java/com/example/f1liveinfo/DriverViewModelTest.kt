import com.example.f1liveinfo.data.DriverApiService
import com.example.f1liveinfo.data.LATEST
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

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        driverViewModel = DriverViewModel() // Pass the mock service
        driverViewModel.driverApiService = driverApiService

        //Get data from json file
        driversFromData = getDriverFromDataSource()
        println("driversFromData: $driversFromData")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after the test
    }

    @Test
    fun `getDriversData success`() = runTest {
        // Mock successful API response
        val mockDrivers = driversFromData
        Mockito.`when`(driverApiService.getDrivers()).thenReturn(mockDrivers)
        var i = 1
        mockDrivers.forEach { driver ->
            Mockito.`when`(driverApiService.getPositions(LATEST, driver.driverNumber))
                .thenReturn(listOf(Position(driver.driverNumber, i)))
            i++
        }

        driverViewModel.getDriversData()

        // Wait for the coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            DriversUiState.Success(
                getDriversAndPositions()
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
        Mockito.`when`(driverApiService.getDrivers(LATEST)).thenReturn(null)

        driverViewModel.getDriversData()

        assertEquals(DriversUiState.Error, driverViewModel.driversUiState)
    }


    private fun getDriverFromDataSource(): List<Driver> {

        //Create Json object that ignore unknown keys
        val json = Json {
            ignoreUnknownKeys = true
        }

        val inputStream: InputStream =
            javaClass.classLoader!!.getResourceAsStream("data_drivers.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return json.decodeFromString<List<Driver>>(jsonString)
    }

    private fun getDriversAndPositions(): List<Driver> {
        return listOf(
            Driver(
                lastName = "Verstappen",
                firstName = "Max",
                countryCode = "NED",
                teamName = "Red Bull Racing",
                driverNumber = 1,
                teamColour = "3671C6",
                position = 1
            ), Driver(
                lastName = "Sargeant",
                firstName = "Logan",
                countryCode = "USA",
                teamName = "Williams",
                driverNumber = 2,
                teamColour = "64C4FF",
                position = 2
            ), Driver(
                lastName = "Ricciardo",
                firstName = "Daniel",
                countryCode = "AUS",
                teamName = "RB",
                driverNumber = 3,
                teamColour = "6692FF",
                position = 3
            ), Driver(
                lastName = "Norris",
                firstName = "Lando",
                countryCode = "GBR",
                teamName = "McLaren",
                driverNumber = 4,
                teamColour = "FF8000",
                position = 4
            ), Driver(
                lastName = "Gasly",
                firstName = "Pierre",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 10,
                teamColour = "0093CC",
                position = 5
            ), Driver(
                lastName = "Perez",
                firstName = "Sergio",
                countryCode = "MEX",
                teamName = "Red Bull Racing",
                driverNumber = 11,
                teamColour = "3671C6",
                position = 6
            ), Driver(
                lastName = "Alonso",
                firstName = "Fernando",
                countryCode = "ESP",
                teamName = "Aston Martin",
                driverNumber = 14,
                teamColour = "229971",
                position = 7
            ), Driver(
                lastName = "Leclerc",
                firstName = "Charles",
                countryCode = "MON",
                teamName = "Ferrari",
                driverNumber = 16,
                teamColour = "E80020",
                position = 8
            ), Driver(
                lastName = "Stroll",
                firstName = "Lance",
                countryCode = "CAN",
                teamName = "Aston Martin",
                driverNumber = 18,
                teamColour = "229971",
                position = 9
            ), Driver(
                lastName = "Magnussen",
                firstName = "Kevin",
                countryCode = "DEN",
                teamName = "Haas F1 Team",
                driverNumber = 20,
                teamColour = "B6BABD",
                position = 10
            ), Driver(
                lastName = "Tsunoda",
                firstName = "Yuki",
                countryCode = "JPN",
                teamName = "RB",
                driverNumber = 22,
                teamColour = "6692FF",
                position = 11
            ), Driver(
                lastName = "Albon",
                firstName = "Alexander",
                countryCode = "THA",
                teamName = "Williams",
                driverNumber = 23,
                teamColour = "64C4FF",
                position = 12
            ), Driver(
                lastName = "Zhou",
                firstName = "Guanyu",
                countryCode = "CHN",
                teamName = "Kick Sauber",
                driverNumber = 24,
                teamColour = "52E252",
                position = 13
            ), Driver(
                lastName = "Hulkenberg",
                firstName = "Nico",
                countryCode = "GER",
                teamName = "Haas F1 Team",
                driverNumber = 27,
                teamColour = "B6BABD",
                position = 14
            ), Driver(
                lastName = "Ocon",
                firstName = "Esteban",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 31,
                teamColour = "0093CC",
                position = 15
            ), Driver(
                lastName = "Hamilton",
                firstName = "Lewis",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 44,
                teamColour = "27F4D2",
                position = 16
            ), Driver(
                lastName = "Sainz",
                firstName = "Carlos",
                countryCode = "ESP",
                teamName = "Ferrari",
                driverNumber = 55,
                teamColour = "E80020",
                position = 17
            ), Driver(
                lastName = "Russell",
                firstName = "George",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 63,
                teamColour = "27F4D2",
                position = 18
            ), Driver(
                lastName = "Bottas",
                firstName = "Valtteri",
                countryCode = "FIN",
                teamName = "Kick Sauber",
                driverNumber = 77,
                teamColour = "52E252",
                position = 19
            ), Driver(
                lastName = "Piastri",
                firstName = "Oscar",
                countryCode = "AUS",
                teamName = "McLaren",
                driverNumber = 81,
                teamColour = "FF8000",
                position = 20
            )
        )
    }
}