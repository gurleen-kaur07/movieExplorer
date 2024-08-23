package com.example.androidassignment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.presentation.components.LoadImage
import com.android.presentation.components.TopBar
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
    }

    @Test
    fun performBackNavigation() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            TopBar(title = "title") {
                navController.popBackStack()
            }
        }

        composeTestRule
            .onNodeWithContentDescription("ArrowBack")
            .performClick()
    }

    @Test
    fun testLoadImage() {
        composeTestRule.setContent {
            LoadImage(
                url = (
                    "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
                ),
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
        }
        composeTestRule
            .onNodeWithContentDescription("Image")
            .assertIsDisplayed()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.androidassignment", appContext.packageName)
    }
}
