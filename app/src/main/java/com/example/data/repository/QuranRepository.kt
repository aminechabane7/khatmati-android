package com.example.data.repository

import com.example.data.model.Ayah
import com.example.data.model.Bookmark
import com.example.data.model.ReadingPosition
import com.example.data.model.RevelationType
import com.example.data.model.Surah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranRepository {

    private val allSurahsList: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", 7, RevelationType.MECCAN, 1),
        Surah(2, "البقرة", "Al-Baqarah", 286, RevelationType.MEDINAN, 2),
        Surah(3, "آل عمران", "Aal-Imran", 200, RevelationType.MEDINAN, 50),
        Surah(4, "النساء", "An-Nisa", 176, RevelationType.MEDINAN, 77),
        Surah(5, "المائدة", "Al-Ma'idah", 120, RevelationType.MEDINAN, 106),
        Surah(6, "الأنعام", "Al-An'am", 165, RevelationType.MECCAN, 128),
        Surah(7, "الأعراف", "Al-A'raf", 206, RevelationType.MECCAN, 151),
        Surah(8, "الأنفال", "Al-Anfal", 75, RevelationType.MEDINAN, 177),
        Surah(9, "التوبة", "At-Tawbah", 129, RevelationType.MEDINAN, 187),
        Surah(10, "يونس", "Yunus", 109, RevelationType.MECCAN, 208),
        Surah(11, "هود", "Hud", 123, RevelationType.MECCAN, 221),
        Surah(12, "يوسف", "Yusuf", 111, RevelationType.MECCAN, 235),
        Surah(13, "الرعد", "Ar-Ra'd", 43, RevelationType.MEDINAN, 249),
        Surah(14, "إبراهيم", "Ibrahim", 52, RevelationType.MECCAN, 255),
        Surah(15, "الحجر", "Al-Hijr", 99, RevelationType.MECCAN, 262),
        Surah(16, "النحل", "An-Nahl", 128, RevelationType.MECCAN, 267),
        Surah(17, "الإسراء", "Al-Isra", 111, RevelationType.MECCAN, 282),
        Surah(18, "الكهف", "Al-Kahf", 110, RevelationType.MECCAN, 293),
        Surah(19, "مريم", "Maryam", 98, RevelationType.MECCAN, 305),
        Surah(20, "طه", "Taha", 135, RevelationType.MECCAN, 312),
        Surah(21, "الأنبياء", "Al-Anbiya", 112, RevelationType.MECCAN, 322),
        Surah(22, "الحج", "Al-Hajj", 78, RevelationType.MEDINAN, 332),
        Surah(23, "المؤمنون", "Al-Mu'minun", 118, RevelationType.MECCAN, 342),
        Surah(24, "النور", "An-Nur", 64, RevelationType.MEDINAN, 350),
        Surah(25, "الفرقان", "Al-Furqan", 77, RevelationType.MECCAN, 359),
        Surah(26, "الشعراء", "Ash-Shu'ara", 227, RevelationType.MECCAN, 367),
        Surah(27, "النمل", "An-Naml", 93, RevelationType.MECCAN, 377),
        Surah(28, "القصص", "Al-Qasas", 88, RevelationType.MECCAN, 385),
        Surah(29, "العنكبوت", "Al-Ankabut", 69, RevelationType.MECCAN, 396),
        Surah(30, "الروم", "Ar-Rum", 60, RevelationType.MECCAN, 404),
        Surah(31, "لقمان", "Luqman", 34, RevelationType.MECCAN, 411),
        Surah(32, "السجدة", "As-Sajdah", 30, RevelationType.MECCAN, 415),
        Surah(33, "الأحزاب", "Al-Ahzab", 73, RevelationType.MEDINAN, 418),
        Surah(34, "سبأ", "Saba", 54, RevelationType.MECCAN, 428),
        Surah(35, "فاطر", "Fatir", 45, RevelationType.MECCAN, 434),
        Surah(36, "يس", "Ya-Sin", 83, RevelationType.MECCAN, 440),
        Surah(37, "الصافات", "As-Saffat", 182, RevelationType.MECCAN, 446),
        Surah(38, "ص", "Sad", 88, RevelationType.MECCAN, 453),
        Surah(39, "الزمر", "Az-Zumar", 75, RevelationType.MECCAN, 458),
        Surah(40, "غافر", "Ghafir", 85, RevelationType.MECCAN, 467),
        Surah(41, "فصلت", "Fussilat", 54, RevelationType.MECCAN, 477),
        Surah(42, "الشورى", "Ash-Shura", 53, RevelationType.MECCAN, 483),
        Surah(43, "الزخرف", "Az-Zukhruf", 89, RevelationType.MECCAN, 489),
        Surah(44, "الدخان", "Ad-Dukhan", 59, RevelationType.MECCAN, 496),
        Surah(45, "الجاثية", "Al-Jathiyah", 37, RevelationType.MECCAN, 499),
        Surah(46, "الأحقاف", "Al-Ahqaf", 35, RevelationType.MECCAN, 502),
        Surah(47, "محمد", "Muhammad", 38, RevelationType.MEDINAN, 507),
        Surah(48, "الفتح", "Al-Fath", 29, RevelationType.MEDINAN, 511),
        Surah(49, "الحجرات", "Al-Hujurat", 18, RevelationType.MEDINAN, 515),
        Surah(50, "ق", "Qaf", 45, RevelationType.MECCAN, 518),
        Surah(51, "الذاريات", "Adh-Dhariyat", 60, RevelationType.MECCAN, 520),
        Surah(52, "الطور", "At-Tur", 49, RevelationType.MECCAN, 523),
        Surah(53, "النجم", "An-Najm", 62, RevelationType.MECCAN, 526),
        Surah(54, "القمر", "Al-Qamar", 55, RevelationType.MECCAN, 528),
        Surah(55, "الرحمن", "Ar-Rahman", 78, RevelationType.MEDINAN, 531),
        Surah(56, "الواقعة", "Al-Waqi'ah", 96, RevelationType.MECCAN, 534),
        Surah(57, "الحديد", "Al-Hadid", 29, RevelationType.MEDINAN, 537),
        Surah(58, "المجادلة", "Al-Mujadila", 22, RevelationType.MEDINAN, 542),
        Surah(59, "الحشر", "Al-Hashr", 24, RevelationType.MEDINAN, 545),
        Surah(60, "الممتحنة", "Al-Mumtahanah", 13, RevelationType.MEDINAN, 549),
        Surah(61, "الصف", "As-Saff", 14, RevelationType.MEDINAN, 551),
        Surah(62, "الجمعة", "Al-Jumu'ah", 11, RevelationType.MEDINAN, 553),
        Surah(63, "المنافقون", "Al-Munafiqun", 11, RevelationType.MEDINAN, 554),
        Surah(64, "التغابن", "At-Taghabun", 18, RevelationType.MEDINAN, 556),
        Surah(65, "الطلاق", "At-Talaq", 12, RevelationType.MEDINAN, 558),
        Surah(66, "التحريم", "At-Tahrim", 12, RevelationType.MEDINAN, 560),
        Surah(67, "الملك", "Al-Mulk", 30, RevelationType.MECCAN, 562),
        Surah(68, "القلم", "Al-Qalam", 52, RevelationType.MECCAN, 564),
        Surah(69, "الحاقة", "Al-Haqqah", 52, RevelationType.MECCAN, 566),
        Surah(70, "المعارج", "Al-Ma'arij", 44, RevelationType.MECCAN, 568),
        Surah(71, "نوح", "Nuh", 28, RevelationType.MECCAN, 570),
        Surah(72, "الجن", "Al-Jinn", 28, RevelationType.MECCAN, 572),
        Surah(73, "المزمل", "Al-Muzzammil", 20, RevelationType.MECCAN, 574),
        Surah(74, "المدثر", "Al-Muddathir", 56, RevelationType.MECCAN, 575),
        Surah(75, "القيامة", "Al-Qiyamah", 40, RevelationType.MECCAN, 577),
        Surah(76, "الإنسان", "Al-Insan", 31, RevelationType.MEDINAN, 578),
        Surah(77, "المرسلات", "Al-Mursalat", 50, RevelationType.MECCAN, 580),
        Surah(78, "النبأ", "An-Naba", 40, RevelationType.MECCAN, 582),
        Surah(79, "النازعات", "An-Nazi'at", 46, RevelationType.MECCAN, 583),
        Surah(80, "عبس", "Abasa", 42, RevelationType.MECCAN, 585),
        Surah(81, "التكوير", "At-Takwir", 29, RevelationType.MECCAN, 586),
        Surah(82, "الانفطار", "Al-Infitar", 19, RevelationType.MECCAN, 587),
        Surah(83, "المطففين", "Al-Mutaffifin", 36, RevelationType.MECCAN, 587),
        Surah(84, "الانشقاق", "Al-Inshiqaq", 25, RevelationType.MECCAN, 589),
        Surah(85, "البروج", "Al-Buruj", 22, RevelationType.MECCAN, 590),
        Surah(86, "الطارق", "At-Tariq", 17, RevelationType.MECCAN, 591),
        Surah(87, "الأعلى", "Al-A'la", 19, RevelationType.MECCAN, 591),
        Surah(88, "الغاشية", "Al-Ghashiyah", 26, RevelationType.MECCAN, 592),
        Surah(89, "الفجر", "Al-Fajr", 30, RevelationType.MECCAN, 593),
        Surah(90, "البلد", "Al-Balad", 20, RevelationType.MECCAN, 594),
        Surah(91, "الشمس", "Ash-Shams", 15, RevelationType.MECCAN, 595),
        Surah(92, "الليل", "Al-Layl", 21, RevelationType.MECCAN, 595),
        Surah(93, "الضحى", "Ad-Duha", 11, RevelationType.MECCAN, 596),
        Surah(94, "الشرح", "Ash-Sharh", 8, RevelationType.MECCAN, 596),
        Surah(95, "التين", "At-Tin", 8, RevelationType.MECCAN, 597),
        Surah(96, "العلق", "Al-Alaq", 19, RevelationType.MECCAN, 597),
        Surah(97, "القدر", "Al-Qadr", 5, RevelationType.MECCAN, 598),
        Surah(98, "البينة", "Al-Bayyinah", 8, RevelationType.MEDINAN, 598),
        Surah(99, "الزلزلة", "Az-Zalzalah", 8, RevelationType.MEDINAN, 599),
        Surah(100, "العاديات", "Al-Adiyat", 11, RevelationType.MECCAN, 599),
        Surah(101, "القارعة", "Al-Qari'ah", 11, RevelationType.MECCAN, 600),
        Surah(102, "التكاثر", "At-Takathur", 8, RevelationType.MECCAN, 600),
        Surah(103, "العصر", "Al-Asr", 3, RevelationType.MECCAN, 601),
        Surah(104, "الهمزة", "Al-Humazah", 9, RevelationType.MECCAN, 601),
        Surah(105, "الفيل", "Al-Fil", 5, RevelationType.MECCAN, 601),
        Surah(106, "قريش", "Quraysh", 4, RevelationType.MECCAN, 602),
        Surah(107, "الماعون", "Al-Ma'un", 7, RevelationType.MECCAN, 602),
        Surah(108, "الكوثر", "Al-Kawthar", 3, RevelationType.MECCAN, 602),
        Surah(109, "الكافرون", "Al-Kafirun", 6, RevelationType.MECCAN, 603),
        Surah(110, "النصر", "An-Nasr", 3, RevelationType.MEDINAN, 603),
        Surah(111, "المسد", "Al-Masad", 5, RevelationType.MECCAN, 603),
        Surah(112, "الإخلاص", "Al-Ikhlas", 4, RevelationType.MECCAN, 604),
        Surah(113, "الفلق", "Al-Falaq", 5, RevelationType.MECCAN, 604),
        Surah(114, "الناس", "An-Nas", 6, RevelationType.MECCAN, 604)
    )

    private val _readingPosition = MutableStateFlow(
        ReadingPosition(
            surahNumber = 1,
            surahName = "سورة الفاتحة",
            ayahNumber = 1,
            pageNumber = 1,
            lastReadFormatted = "اليوم"
        )
    )
    val readingPosition: StateFlow<ReadingPosition> = _readingPosition.asStateFlow()

    private val _bookmarks = MutableStateFlow(
        listOf(
            Bookmark("1", 1, "سورة الفاتحة", 1, 1, "اليوم"),
            Bookmark("2", 18, "سورة الكهف", 1, 293, "أمس"),
            Bookmark("3", 36, "سورة يس", 1, 440, "سابقاً")
        )
    )
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()

    fun getAllSurahs(): List<Surah> = allSurahsList

    fun getSurah(number: Int): Surah? {
        return allSurahsList.find { it.number == number }
    }

    fun getAllSurahUiModels(): List<com.example.data.model.SurahUiModel> {
        return allSurahsList.map { surah ->
            com.example.data.model.SurahUiModel(
                number = surah.number,
                nameArabic = surah.arabicName,
                ayahCount = surah.versesCount,
                revelationType = surah.revelationType.arabicLabel,
                startPage = surah.startPage
            )
        }
    }

    fun updateReadingPosition(surahNumber: Int, ayahNumber: Int, pageNumber: Int) {
        val surah = allSurahsList.find { it.number == surahNumber }
        _readingPosition.value = ReadingPosition(
            surahNumber = surahNumber,
            surahName = "سورة ${surah?.arabicName ?: ""}",
            ayahNumber = ayahNumber,
            pageNumber = pageNumber,
            lastReadFormatted = "الآن"
        )
    }

    fun toggleBookmark(surahNumber: Int, ayahNumber: Int, pageNumber: Int) {
        val current = _bookmarks.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
        } else {
            val surah = allSurahsList.find { it.number == surahNumber }
            current.add(
                Bookmark(
                    id = System.currentTimeMillis().toString(),
                    surahNumber = surahNumber,
                    surahName = "سورة ${surah?.arabicName ?: ""}",
                    ayahNumber = ayahNumber,
                    pageNumber = pageNumber,
                    dateFormatted = "اليوم"
                )
            )
        }
        _bookmarks.value = current
    }

    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean {
        return _bookmarks.value.any { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
    }

    /**
     * Returns verses for the requested Surah.
     * Accurately reflects the selected Surah.
     */
    fun getVersesForSurah(surahNumber: Int): List<Ayah> {
        val targetSurah = allSurahsList.find { it.number == surahNumber } ?: allSurahsList.first()
        val startPage = targetSurah.startPage

        return when (surahNumber) {
            1 -> listOf(
                Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", 1, 1),
                Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", 1, 1),
                Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", 1, 1),
                Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", 1, 1),
                Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", 1, 1),
                Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", 1, 1),
                Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", 1, 1)
            )
            2 -> listOf(
                Ayah(2, 1, "الم", 2, 1),
                Ayah(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", 2, 1),
                Ayah(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ", 2, 1),
                Ayah(2, 4, "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ", 2, 1),
                Ayah(2, 5, "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ", 2, 1)
            )
            108 -> listOf(
                Ayah(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", 602, 30),
                Ayah(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", 602, 30),
                Ayah(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", 602, 30)
            )
            112 -> listOf(
                Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", 604, 30),
                Ayah(112, 2, "اللَّهُ الصَّمَدُ", 604, 30),
                Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", 604, 30),
                Ayah(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", 604, 30)
            )
            113 -> listOf(
                Ayah(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", 604, 30),
                Ayah(113, 2, "مِن شَرِّ مَا خَلَقَ", 604, 30),
                Ayah(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", 604, 30),
                Ayah(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", 604, 30),
                Ayah(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", 604, 30)
            )
            114 -> listOf(
                Ayah(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", 604, 30),
                Ayah(114, 2, "مَلِكِ النَّاسِ", 604, 30),
                Ayah(114, 3, "إِلَٰهِ النَّاسِ", 604, 30),
                Ayah(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", 604, 30),
                Ayah(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", 604, 30),
                Ayah(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", 604, 30)
            )
            else -> {
                val count = targetSurah.versesCount.coerceAtMost(5)
                (1..count).map { ayahNum ->
                    Ayah(
                        surahNumber = targetSurah.number,
                        ayahNumber = ayahNum,
                        textUthmani = "﴿ سورة ${targetSurah.arabicName} — الآية $ayahNum ﴾",
                        pageNumber = startPage,
                        juzNumber = ((startPage - 1) / 20) + 1
                    )
                }
            }
        }
    }
}
