package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuranNavigationComposeInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testQuranNavigationSequence() {
        composeTestRule.waitForIdle()

        // If onboarding is displayed, skip to main screen
        val skipButtons = composeTestRule.onAllNodesWithTag("onboarding_skip_button")
        if (skipButtons.fetchSemanticsNodes().isNotEmpty()) {
            skipButtons[0].performClick()
            composeTestRule.waitForIdle()
        }

        // Open Quran index tab
        composeTestRule.onNodeWithTag("nav_item_القرآن").performClick()
        composeTestRule.waitForIdle()

        // 1. Tap سورة الفاتحة (surah 1)
        composeTestRule.onNodeWithTag("surah_item_1").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة الفاتحة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة الفاتحة")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("٧ آية")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("صفحة ١")

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 2. Tap سورة البقرة (surah 2)
        composeTestRule.onNodeWithTag("surah_item_2").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة البقرة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة البقرة")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("٢٨٦ آية")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("صفحة ٢")

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 3. Tap سورة آل عمران (surah 3)
        composeTestRule.onNodeWithTag("surah_item_3").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة آل عمران and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة آل عمران")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("٢٠٠ آية")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("صفحة ٥٠")

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 4. Tap سورة النساء (surah 4)
        composeTestRule.onNodeWithTag("surah_item_4").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة النساء and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة النساء")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("١٧٦ آية")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("صفحة ٧٧")

        // Return to Quran index
        composeTestRule.onNodeWithTag("reader_back_button").performClick()
        composeTestRule.waitForIdle()

        // 5. Tap سورة الفاتحة again (surah 1)
        composeTestRule.onNodeWithTag("surah_item_1").performClick()
        composeTestRule.waitForIdle()

        // Verify reader displays سورة الفاتحة and metadata
        composeTestRule.onNodeWithTag("reader_surah_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reader_surah_title").assertTextContains("سورة الفاتحة")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("٧ آية")
        composeTestRule.onNodeWithTag("reader_surah_metadata").assertTextContains("صفحة ١")
    }
}
