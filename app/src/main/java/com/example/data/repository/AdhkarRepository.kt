package com.example.data.repository

import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdhkarRepository {

    val categories: List<DhikrCategory> = listOf(
        DhikrCategory("morning", "أذكار الصباح", "سكينة وطمأنينة لبداية يومك", 12, 4),
        DhikrCategory("evening", "أذكار المساء", "حصن المسلم وحفظ الله ليلتك", 12, 0),
        DhikrCategory("after_prayer", "أذكار بعد الصلاة", "الأذكار المسنونة دبر كل صلاة مكتوبة", 8, 3),
        DhikrCategory("sleep", "أذكار النوم", "حفظ وسكينة قبل النوم", 7, 0),
        DhikrCategory("wake_up", "أذكار الاستيقاظ", "الحمد لله الذي أحيانا بعد ما أماتنا", 4, 1),
        DhikrCategory("duas", "الأدعية المأثورة", "جوامع الدعاء من الكتاب والسنة", 15, 2)
    )

    private val _items = MutableStateFlow(
        listOf(
            // Morning items
            DhikrItem(
                id = "m1",
                categoryId = "morning",
                textArabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
                referenceOrFadl = "رواه مسلم",
                targetCount = 1,
                currentCount = 1
            ),
            DhikrItem(
                id = "m2",
                categoryId = "morning",
                textArabic = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ.",
                referenceOrFadl = "رواه الترمذي",
                targetCount = 1,
                currentCount = 1
            ),
            DhikrItem(
                id = "m3",
                categoryId = "morning",
                textArabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
                referenceOrFadl = "يقال ثلاث مرات - رواه مسلم",
                targetCount = 3,
                currentCount = 2
            ),
            DhikrItem(
                id = "m4",
                categoryId = "morning",
                textArabic = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ.",
                referenceOrFadl = "سيد الاستغفار - رواه البخاري",
                targetCount = 1,
                currentCount = 0
            ),
            DhikrItem(
                id = "m5",
                categoryId = "morning",
                textArabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ.",
                referenceOrFadl = "من قالها مائة مرة حُطّت خطاياه - متفق عليه",
                targetCount = 100,
                currentCount = 33
            ),

            // After prayer items
            DhikrItem(
                id = "p1",
                categoryId = "after_prayer",
                textArabic = "أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ، تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ.",
                referenceOrFadl = "يقال دبر كل صلاة - رواه مسلم",
                targetCount = 1,
                currentCount = 1
            ),
            DhikrItem(
                id = "p2",
                categoryId = "after_prayer",
                textArabic = "سُبْحَانَ اللَّهِ.",
                referenceOrFadl = "التسبيح دبر الصلاة ثلاثاً وثلاثين",
                targetCount = 33,
                currentCount = 33
            ),
            DhikrItem(
                id = "p3",
                categoryId = "after_prayer",
                textArabic = "الْحَمْدُ لِلَّهِ.",
                referenceOrFadl = "التحميد دبر الصلاة ثلاثاً وثلاثين",
                targetCount = 33,
                currentCount = 33
            ),
            DhikrItem(
                id = "p4",
                categoryId = "after_prayer",
                textArabic = "اللَّهُ أَكْبَرُ.",
                referenceOrFadl = "التكبير دبر الصلاة ثلاثاً وثلاثين",
                targetCount = 33,
                currentCount = 12
            ),

            // Evening items
            DhikrItem(
                id = "e1",
                categoryId = "evening",
                textArabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
                referenceOrFadl = "رواه مسلم",
                targetCount = 1,
                currentCount = 0
            ),
            DhikrItem(
                id = "e2",
                categoryId = "evening",
                textArabic = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ.",
                referenceOrFadl = "رواه الترمذي",
                targetCount = 1,
                currentCount = 0
            )
        )
    )
    val items: StateFlow<List<DhikrItem>> = _items.asStateFlow()

    fun incrementDhikr(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id && item.currentCount < item.targetCount) {
                item.copy(currentCount = item.currentCount + 1)
            } else {
                item
            }
        }
    }

    fun resetDhikr(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id) {
                item.copy(currentCount = 0)
            } else {
                item
            }
        }
    }

    fun getItemsForCategory(categoryId: String): List<DhikrItem> {
        val filtered = _items.value.filter { it.categoryId == categoryId }
        return if (filtered.isNotEmpty()) {
            filtered
        } else {
            // Default authentic fallback if category has no specific items yet
            listOf(
                DhikrItem(
                    id = "gen_1",
                    categoryId = categoryId,
                    textArabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ.",
                    referenceOrFadl = "كلمتان خفيفتان على اللسان ثقيلتان في الميزان - متفق عليه",
                    targetCount = 33,
                    currentCount = 0
                ),
                DhikrItem(
                    id = "gen_2",
                    categoryId = categoryId,
                    textArabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ.",
                    referenceOrFadl = "كنز من كنوز الجنة - متفق عليه",
                    targetCount = 33,
                    currentCount = 0
                )
            )
        }
    }
}
