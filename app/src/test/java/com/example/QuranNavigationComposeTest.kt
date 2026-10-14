package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-xxhdpi")
class QuranNavigationComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testQuranNavigationSequence() {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        kotlinx.coroutines.runBlocking {
            com.example.data.repository.UserPreferencesRepository(context).setOnboardingCompleted(true)
        }

        composeTestRule.setContent {
            KhatmatiApp()
        }
        composeTestRule.waitForIdle()

        // If onboarding is displayed, click skip
        val skipButtons = composeTestRule.onAllNodesWithTag("onboarding_skip_button")
        if (skipButtons.fetchSemanticsNodes().isNotEmpty()) {
            skipButtons[0].performClick()
            composeTestRule.waitForIdle()
        }

        // Print semantics tree
        composeTestRule.onRoot().printToLog("QURAN_NAV_TEST")

        // Open Quran index tab
        composeTestRule.onNodeWithTag("nav_item_القرآن").performClick()
        composeTestRule.waitForIdle()

        // 1. Tap سورة الفاتحة (surah 1)
        composeTestRule.onNodeWithTag("quran_index_list").performScrollToNode(hasTestTag("surah_item_1"))
        composeTestRule.onNodeWithTag("surah_item_1").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة الفاتحة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة الفاتحة", substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(7), substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(1), substring = true)

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 2. Tap سورة البقرة (surah 2)
        composeTestRule.onNodeWithTag("quran_index_list").performScrollToNode(hasTestTag("surah_item_2"))
        composeTestRule.onNodeWithTag("surah_item_2").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة البقرة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة البقرة", substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(286), substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(2), substring = true)

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 3. Tap سورة آل عمران (surah 3)
        composeTestRule.onNodeWithTag("quran_index_list").performScrollToNode(hasTestTag("surah_item_3"))
        composeTestRule.onNodeWithTag("surah_item_3").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة آل عمران and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة آل عمران", substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(200), substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(3), substring = true)

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 4. Tap سورة النساء (surah 4)
        composeTestRule.onNodeWithTag("quran_index_list").performScrollToNode(hasTestTag("surah_item_4"))
        composeTestRule.onNodeWithTag("surah_item_4").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة النساء and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة النساء", substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(176), substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(4), substring = true)

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 5. Tap سورة الفاتحة again (surah 1)
        composeTestRule.onNodeWithTag("quran_index_list").performScrollToNode(hasTestTag("surah_item_1"))
        composeTestRule.onNodeWithTag("surah_item_1").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة الفاتحة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة الفاتحة", substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(7), substring = true)
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains(com.example.core.util.DateTimeHelper.toArabicDigits(1), substring = true)
    }
}
