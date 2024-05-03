package com.gopark.core.config

import com.gopark.core.CoreApplication
import com.gopark.core.repos.ParkingRepository
import com.gopark.core.repos.PaymentRepository
import com.gopark.core.repos.RoleRepository
import com.gopark.core.repos.SpotRepository
import com.gopark.core.repos.UserRepository
import com.gopark.core.repos.VehicleTypeRepository
import io.restassured.RestAssured
import jakarta.annotation.PostConstruct
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.util.StreamUtils
import org.testcontainers.containers.PostgreSQLContainer


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
    classes = [CoreApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql(
    "/data/clearAll.sql",
    "/data/roleData.sql",
    "/data/userData.sql"
)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
abstract class BaseIT {

    @LocalServerPort
    var serverPort = 0

    @Autowired
    lateinit var vehicleTypeRepository: VehicleTypeRepository

    @Autowired
    lateinit var paymentRepository: PaymentRepository

    @Autowired
    lateinit var spotRepository: SpotRepository

    @Autowired
    lateinit var parkingRepository: ParkingRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @PostConstruct
    fun initRestAssured() {
        RestAssured.port = serverPort
        RestAssured.urlEncodingEnabled = false
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    fun readResource(resourceName: String): String =
            StreamUtils.copyToString(this.javaClass.getResourceAsStream(resourceName),
            StandardCharsets.UTF_8)

    fun bearerToken(): String {
        // user bootify, expires 2040-01-01
        return "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJib290aWZ5Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE2OTg4OTkyMzcsImV4cCI6MjIwODk4ODgwMH0." +
                "M8wrAAwu2McaGjglbzOAJ6O8mijqJ9HILogGXN1Qv_4dMJnPdd0LnF1hQnENGG6kAOHo034VbJCqgGBazAwZupTsDzNFG_S_FfGKCqbNH_Bu0YSHpX1L1GMFNwJ2ZtV8wcHOCaxD3oXtS_VJ-9BE-ASJIMtLrnRqAgLf2D6p_lX97mrG4WWDghZ-GUg29oIVrbuCTUl2QFyNib1pXOn1-DrIFlyljB60Rk4v1lCp79ae7QCa-UcyN_TeZc0kqwVOY-_3RiMQ0yLbjwt8xNFkzzYnh5vFS4bnMY7b7JoPj5t0wqw_D1XH2AbnVsFhN0PXSMgWcwG48vCNl94PjNKWjw"
    }


    companion object {

        @ServiceConnection
        val postgreSQLContainer = PostgreSQLContainer("postgres:16.2")

        init {
            postgreSQLContainer.withReuse(true)
                    .start()
        }

    }

}
