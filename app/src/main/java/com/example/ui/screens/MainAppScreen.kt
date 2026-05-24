package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import com.example.data.database.PoojaChecklistItem
import com.example.data.database.SavedReading
import com.example.data.database.SavedRecipe
import com.example.ui.theme.*
import com.example.ui.viewmodel.JyotishViewModel
import com.example.ui.viewmodel.LlmState

data class FestivalItem(
    val name: String,
    val subtitle: String,
    val deity: String,
    val countdownText: String,
    val colorHex: Color
)

fun tr(key: String, lang: String): String {
    val en = when (key) {
        "tab_home" -> "Home"
        "tab_pooja" -> "Pooja"
        "tab_panchang" -> "Panchang"
        "tab_palmistry" -> "Palmistry"
        "tab_ask_guru" -> "Ask Guru"
        "btn_ask_astro_guru" -> "Ask Astro Guru"
        "app_subtitle" -> "Your Divine Vedic Guide"
        "home_greeting_title" -> "🙏 Jai Shri Ram & Namaste,"
        "home_greeting_desc" -> "May the alignment of dynamic planets bring prosperity, deep clarity, and peace to your spiritual journey today."
        "panchang_today" -> "📅 PANCHANG TODAY"
        "shukla_paksha" -> "Shukla Paksha"
        "tithi" -> "Tithi"
        "ashtami" -> "Ashtami"
        "ashtami_time" -> "until 08:44 PM"
        "nakshatra" -> "Nakshatra"
        "purva_phalguni" -> "Purva Phalguni"
        "venus_lord" -> "Venus Lord"
        "var" -> "Var"
        "sunday" -> "Sunday"
        "surya_dev" -> "Surya Dev"
        "shubh_muhurat_time" -> "🌟 Shubh Muhurat: 11:45 AM - 12:35 PM"
        "see_more" -> "See More"
        "holy_services" -> "🌸 HOLY SERVICES"
        "srv_pooja_vidhi" -> "Pooja Vidhi"
        "srv_pooja_vidhi_desc" -> "Rituals & Mantras"
        "srv_hasta_rekha" -> "Hasta Rekha"
        "srv_hasta_rekha_desc" -> "Palm line secrets"
        "srv_sacred_prasad" -> "Sacred Prasad"
        "srv_sacred_prasad_desc" -> "Satvik food recipes"
        "srv_daily_calendar" -> "Daily Calendar"
        "srv_daily_calendar_desc" -> "Tithi & Rahu Kaal"
        "upcoming_festivals" -> "📿 UPCOMING FESTIVALS"
        "subtab_pooja_guides" -> "🪔 POOJA GUIDES"
        "subtab_prasad_recipes" -> "🍛 PRASAD RECIPES"
        "subtab_list" -> "📋 POOJA CHECKLIST"
        "placeholder_pooja_search" -> "Search e.g. Ganesh, Diwali, Shivratri..."
        "placeholder_recipe_search" -> "Search Prasad recipe e.g. Modak, Panjiri..."
        "placeholder_item_name" -> "Item e.g. Roli"
        "placeholder_item_qty" -> "Qty e.g. 1 packet"
        "placeholder_panchang_search" -> "e.g. Diwali 2026 Nakshatra, Today's Rahu Kaal timings"
        "placeholder_palmistry_desc" -> "Write about your hand lines, mount puffiness, or line forks..."
        "placeholder_chat_input" -> "Ask JyotishGuru about astrology..."
        "subtab_hasta_diagram" -> "✋ HASTA DIAGRAM"
        "subtab_request_reading" -> "✨ REQUEST READING"
        "subtab_historic_scrolls" -> "📂 HISTORIC SCROLLS"
        "guru_chat_heading" -> "🔱 JyotishGuru"
        "sadhaka" -> "🙏 Sadhaka"
        "contemplating" -> "Guru is contemplating celestial planets..."
        "settings_profile_title" -> "Profile & Settings"
        "settings_profile_label" -> "Enter your sacred name or nickname:"
        "settings_lang_label" -> "Choose Sacred Consultant Language:"
        "settings_regional_pref" -> "Regional Ritual Preferences:"
        "settings_calendar_std" -> "• Calendar Standard: Amanta (Sriman/Saka)"
        "settings_timezone" -> "• Timezone: UTC (+05:30 Equivalent)"
        "settings_btn_save" -> "Save Divine Profile"
        "settings_btn_cancel" -> "Cancel"
        else -> key
    }

    if (lang == "en") return en

    return when (lang) {
        "hi" -> when (key) {
            "tab_home" -> "मुख्य पृष्ठ"
            "tab_pooja" -> "पूजा विधी"
            "tab_panchang" -> "पंचांग"
            "tab_palmistry" -> "हस्तरेखा"
            "tab_ask_guru" -> "वार्तालाप"
            "btn_ask_astro_guru" -> "ज्योतिष गुरु से पूछें"
            "app_subtitle" -> "आपका दिव्य वैदिक मार्गदर्शक"
            "home_greeting_title" -> "🙏 जय श्री राम और नमस्ते,"
            "home_greeting_desc" -> "ग्रहों की अनुकूल स्थिति आपके जीवन में समृद्धि, स्पष्टता और शांति लाए।"
            "panchang_today" -> "📅 आज का पंचांग"
            "shukla_paksha" -> "शुक्ल पक्ष"
            "tithi" -> "तिथि"
            "ashtami" -> "अष्टमी"
            "ashtami_time" -> "रात 08:44 तक"
            "nakshatra" -> "नक्षत्र"
            "purva_phalguni" -> "पूर्वा फाल्गुनी"
            "venus_lord" -> "शुक्र स्वामी"
            "var" -> "वार"
            "sunday" -> "रविवार"
            "surya_dev" -> "सूर्य देव"
            "shubh_muhurat_time" -> "🌟 शुभ मुहूर्त: सुबह 11:45 - दोपहर 12:35"
            "see_more" -> "और देखें"
            "holy_services" -> "🌸 पवित्र सेवाएं"
            "srv_pooja_vidhi" -> "पूजा विधी"
            "srv_pooja_vidhi_desc" -> "मंत्र और अनुष्ठान"
            "srv_hasta_rekha" -> "हस्तरेखा"
            "srv_hasta_rekha_desc" -> "हाथ की लकीरें"
            "srv_sacred_prasad" -> "पवित्र प्रसाद"
            "srv_sacred_prasad_desc" -> "सात्विक भोजन व्यंजन"
            "srv_daily_calendar" -> "दैनिक कैलेंडर"
            "srv_daily_calendar_desc" -> "तिथि और राहु काल"
            "upcoming_festivals" -> "📿 आगामी त्योहार"
            "subtab_pooja_guides" -> "🪔 पूजा मार्गदर्शक"
            "subtab_prasad_recipes" -> "🍛 प्रसाद रेसिपी"
            "subtab_list" -> "📋 पूजा सूची"
            "placeholder_pooja_search" -> "खोजें उदा. गणेश, दीवाली..."
            "placeholder_recipe_search" -> "खोजें प्रसाद रेसिपी उदा. मोदक..."
            "placeholder_item_name" -> "सामग्री जैसे रोली"
            "placeholder_item_qty" -> "मात्रा जैसे 1 पैकेट"
            "placeholder_panchang_search" -> "उदा. दीवाली 2026 नक्षत्र, राहु काल"
            "placeholder_palmistry_desc" -> "हाथ की रेखाओं, उभार आदि के बारे में लिखें..."
            "placeholder_chat_input" -> "ज्योतिषगुरु से ज्योतिष के बारे में पूछें..."
            "subtab_hasta_diagram" -> "✋ हस्तरेखा चित्र"
            "subtab_request_reading" -> "✨ विश्लेषण का अनुरोध"
            "subtab_historic_scrolls" -> "📂 पुराना इतिहास"
            "guru_chat_heading" -> "🔱 ज्योतिषगुरु"
            "sadhaka" -> "🙏 साधक"
            "contemplating" -> "गुरुदेव ग्रहों की स्थिति का अध्ययन कर रहे हैं..."
            "settings_profile_title" -> "प्रोफ़ाइल और सेटिंग्स"
            "settings_profile_label" -> "अपना पवित्र नाम या उपनाम दर्ज करें:"
            "settings_lang_label" -> "परामर्श के लिए दिव्य भाषा चुनें:"
            "settings_regional_pref" -> "क्षेत्रीय पूजा संबंधी प्राथमिकताएं:"
            "settings_calendar_std" -> "• कैलेंडर मानक: अमान्त (श्रीमंत / शक)"
            "settings_timezone" -> "• समय क्षेत्र: UTC (+05:30 के सामान)"
            "settings_btn_save" -> "दिव्य प्रोफ़ाइल सहेजें"
            "settings_btn_cancel" -> "रद्द करें"
            else -> en
        }
        "te" -> when (key) {
            "tab_home" -> "హోమ్"
            "tab_pooja" -> "పూజ"
            "tab_panchang" -> "पंचాంగం"
            "tab_palmistry" -> "హస్తసాముద్రికం"
            "tab_ask_guru" -> "గురువును అడగండి"
            "btn_ask_astro_guru" -> "ఆస్ట్రో గురువును అడగండి"
            "app_subtitle" -> "మీ దివ్య వైదిక మార్గదర్శి"
            "home_greeting_title" -> "🙏 జై శ్రీరామ్ & నమస్తే,"
            "home_greeting_desc" -> "గ్రహాల అనుకూల స్థితి మీ జీవితంలో ఐశ్వర్యం, స్పష్టత మరియు శాంతిని ప్రసాదించుగాక."
            "panchang_today" -> "📅 నేటి పంచాంగం"
            "shukla_paksha" -> "శుక్ల పక్షం"
            "tithi" -> "తిథి"
            "ashtami" -> "అష్టమి"
            "ashtami_time" -> "రాత్రి 08:44 వరకు"
            "nakshatra" -> "నక్షత్రం"
            "purva_phalguni" -> "పూర్వ ఫల్గుణి"
            "venus_lord" -> "శుక్రుడు అధిపతి"
            "var" -> "వారం"
            "sunday" -> "ఆదివారం"
            "surya_dev" -> "సూర్య దేవుడు"
            "shubh_muhurat_time" -> "🌟 శుభ ముహూర్తం: ఉదయం 11:45 - మధ్యాహ్నం 12:35"
            "see_more" -> "మరిన్ని చూడండి"
            "holy_services" -> "🌸 పవిత్ర సేవలు"
            "srv_pooja_vidhi" -> "పూజా విధానం"
            "srv_pooja_vidhi_desc" -> "పూజ సూత్రాలు & మంత్రాలు"
            "srv_hasta_rekha" -> "హస్తరేఖ"
            "srv_hasta_rekha_desc" -> "అరచేతి రేఖల రహస్యాలు"
            "srv_sacred_prasad" -> "పవిత్ర ప్రసాదం"
            "srv_sacred_prasad_desc" -> "సాత్విక వంటకాలు"
            "srv_daily_calendar" -> "ದಿನಸರಿ ಕ್ಯಾಲೆಂಡರ್"
            "srv_daily_calendar_desc" -> "తిథి & రాహు కాలం"
            "upcoming_festivals" -> "📿 రాబోయే పండుగలు"
            "subtab_pooja_guides" -> "🪔 పూజా మార్గదర్శకాలు"
            "subtab_prasad_recipes" -> "🍛 ప్రసాదం సాత్విక వంటకాలు"
            "subtab_list" -> "📋 పూజా చెక్‌లిస్ట్"
            "placeholder_pooja_search" -> "శోధించండి ఉదా. గణేష్, దీపావళి..."
            "placeholder_recipe_search" -> "ప్రసాదం వంటకం శోధించండి..."
            "placeholder_item_name" -> "సామాగ్రి ఉదా. కుంకుమ"
            "placeholder_item_qty" -> "పరిమాణం ఉదా. 1 ప్యాకెట్"
            "placeholder_panchang_search" -> "ఉదా. దీపావళి 2026 నక్షత్రం, రాహు కాలం"
            "placeholder_palmistry_desc" -> "మీ చేతి రేఖలు, పర్వతాల గురించి రాయండి..."
            "placeholder_chat_input" -> "జ్యోతిష్ గురువును జ్యోతిష్యం గురించి అడగండి..."
            "subtab_hasta_diagram" -> "✋ హస్తసాముద్రిక పటం"
            "subtab_request_reading" -> "✨ విశ్లేషణ అభ్యర్థించండి"
            "subtab_historic_scrolls" -> "📂 పాత చరిత్ర"
            "guru_chat_heading" -> "🔱 జ్యోతిష్ గురు"
            "sadhaka" -> "🙏 సాధకుడు"
            "contemplating" -> "గురువుగారు గ్రహాల అనుకూలతను పరిశీలిస్తున్నారు..."
            "settings_profile_title" -> "ప్రొఫైల్ & సెట్టింగులు"
            "settings_profile_label" -> "మీ పవిత్ర నామం నమోదు చేయండి:"
            "settings_lang_label" -> "సలహా కోసం దివ్య భాషను ఎంచుకోండి:"
            "settings_regional_pref" -> "ప్రాంతీయ పూజా ప్రాధాన్యతలు:"
            "settings_calendar_std" -> "• క్యాలెಂಡర్ ప్రామాణికం: అమాంత (శ్రీమన్/శక)"
            "settings_timezone" -> "• కాల మండలం: UTC (+05:30 కి సమానం)"
            "settings_btn_save" -> "دیవ్య ప్రొఫైల్ సేవ్ చేయి"
            "settings_btn_cancel" -> "రద్దు చేయి"
            else -> en
        }
        "ta" -> when (key) {
            "tab_home" -> "முகப்பு"
            "tab_pooja" -> "பூஜை"
            "tab_panchang" -> "பஞ்சாங்கம்"
            "tab_palmistry" -> "கைரேகை"
            "tab_ask_guru" -> "குருவிடம் கேள்"
            "btn_ask_astro_guru" -> "ஜோதிட குருவிடம் கேள்"
            "app_subtitle" -> "உங்கள் தெய்வீக வேத வழிகாட்டி"
            "home_greeting_title" -> "🙏 ஜெய் ஸ்ரீ ராம் & நமஸ்தே,"
            "home_greeting_desc" -> "கிரகங்களின் சாதகமான நிலை உங்கள் வாழ்வில் வளம், தெளிவு மற்றும் அமைதியை கொண்டு வரட்டும்."
            "panchang_today" -> "📅 இன்றைய பஞ்சாங்கம்"
            "shukla_paksha" -> "சுக்ல பட்சம்"
            "tithi" -> "திதி"
            "ashtami" -> "அஷ்டமி"
            "ashtami_time" -> "இரவு 08:44 வரை"
            "nakshatra" -> "நட்சத்திரம்"
            "purva_phalguni" -> "பூர நட்சத்திரம்"
            "venus_lord" -> "சுக்கிர பகவான்"
            "var" -> "வாரம்"
            "sunday" -> "ஞாயிறு"
            "surya_dev" -> "சூரிய பகவான்"
            "shubh_muhurat_time" -> "🌟 சுப முகூர்த்தம்: காலை 11:45 - மதியம் 12:35"
            "see_more" -> "மேலும் பார்க்க"
            "holy_services" -> "🌸 ஆன்மீக சேவைகள்"
            "srv_pooja_vidhi" -> "பூஜை முறைகள்"
            "srv_pooja_vidhi_desc" -> "மந்திரங்கள் & சடங்குகள்"
            "srv_hasta_rekha" -> "கைரேகை ஜோதிடம்"
            "srv_hasta_rekha_desc" -> "கைரேகை ரகசியங்கள்"
            "srv_sacred_prasad" -> "புண்ணிய பிரசாதம்"
            "srv_sacred_prasad_desc" -> "சாத்விக உணவு முறைகள்"
            "srv_daily_calendar" -> "தினசரி நாட்காட்டி"
            "srv_daily_calendar_desc" -> "திதி & ராகு காலம்"
            "upcoming_festivals" -> "📿 வரவிருக்கும் திருவிழாக்கள்"
            "subtab_pooja_guides" -> "🪔 பூஜை வழிகாட்டிகள்"
            "subtab_prasad_recipes" -> "🍛 பிரசாத சமையல்"
            "subtab_list" -> "📋 பூஜை சரிபார்ப்பு"
            "placeholder_pooja_search" -> "தேடுக உதா. கணேஷ், தீபாவளி..."
            "placeholder_recipe_search" -> "பிரசாத சமையல் தேடுக..."
            "placeholder_item_name" -> "பொருள் உதா. குங்குமம்"
            "placeholder_item_qty" -> "அளவு உதா. 1 பாக்கெட்"
            "placeholder_panchang_search" -> "உதா. தீபாவளி 2026 நட்சத்திரம், ராகு காலம்"
            "placeholder_palmistry_desc" -> "உங்கள் கைரேகை, மேடுகள் பற்றி எழுதுங்கள்..."
            "placeholder_chat_input" -> "ஜோதிட குருவிடம் ஜோதிடம் குறித்து கேளுங்கள்..."
            "subtab_hasta_diagram" -> "✋ கைரேகை வரைபடம்"
            "subtab_request_reading" -> "✨ கைரேகை கணிப்பு கோருக"
            "subtab_historic_scrolls" -> "📂 பழைய பதிவுகள்"
            "guru_chat_heading" -> "🔱 ஜோதிட குரு"
            "sadhaka" -> "🙏 சாதகர்"
            "contemplating" -> "குருதேவர் கிரகங்களை ஆராய்ந்து கொண்டிருக்கிறார்..."
            "settings_profile_title" -> "சுயவிவரம் & அமைப்புகள்"
            "settings_profile_label" -> "உங்கள் புனித பெயரை உள்ளிடவும்:"
            "settings_lang_label" -> "ஆலோசனைக் மொழி தேர்வு செய்க:"
            "settings_regional_pref" -> "பிராந்திய பூஜை விருப்பங்கள்:"
            "settings_calendar_std" -> "• நாட்காட்டி முறை: அமாந்தா (ஸ்ரீமன்/சகா)"
            "settings_timezone" -> "• நேர மண்டலம்: UTC (+05:30 க்கு இணையான)"
            "settings_btn_save" -> "சுயவிவரத்தை சேமி"
            "settings_btn_cancel" -> "ரத்து செய்"
            else -> en
        }
        "kn" -> when (key) {
            "tab_home" -> "ಮುಖಪುಟ"
            "tab_pooja" -> "ಪೂಜೆ"
            "tab_panchang" -> "ಪಂಚಾಂಗ"
            "tab_palmistry" -> "ಹಸ್ತಸಾಮುದ್ರಿಕ"
            "tab_ask_guru" -> "ಗುರುವನ್ನು ಕೇಳಿ"
            "btn_ask_astro_guru" -> "ಆಸ್ಟ್ರೋ ಗುರುವನ್ನು ಕೇಳಿ"
            "app_subtitle" -> "ನಿಮ್ಮ ದಿವ್ಯ ವೈದಿಕ ಮಾರ್ಗದರ್ಶಿ"
            "home_greeting_title" -> "🙏 ಜೈ ಶ್ರೀ ರಾಮ್ & ನಮಸ್ತೆ,"
            "home_greeting_desc" -> "ಗ್ರಹಗಳ ಅನುಕೂಲಕರ ಸ್ಥಿತಿಯು ನಿಮ್ಮ ಜೀವನದಲ್ಲಿ ಸಮೃದ್ಧಿ, ಸ್ಪಷ್ಟತೆ ಮತ್ತು ಶಾಂತಿಯನ್ನು ತರಲಿ."
            "panchang_today" -> "📅 ಇಂದಿನ ಪಂಚಾಂಗ"
            "shukla_paksha" -> "ಶುಕ್ಲ ಪಕ್ಷ"
            "tithi" -> "ತಿಥಿ"
            "ashtami" -> "ಅಷ್ಟಮಿ"
            "ashtami_time" -> "ರಾತ್ರಿ 08:44 ವರೆಗೆ"
            "nakshatra" -> "ನಕ್ಷತ್ರ"
            "purva_phalguni" -> "ಪೂರ್ವಾ ಫಾಲ್ಗುಣಿ"
            "venus_lord" -> "ಶುಕ್ರ ಅಧಿಪತಿ"
            "var" -> "ವಾರ"
            "sunday" -> "ಭಾನುವಾರ"
            "surya_dev" -> "ಸೂರ್ಯ ದೇವ"
            "shubh_muhurat_time" -> "🌟 ಶುಭ ಮುಹೂರ್ತ: ಬೆಳಿಗ್ಗೆ 11:45 - ಮಧ್ಯಾಹ್ನ 12:35"
            "see_more" -> "ಇನ್ನು ನೋಡಿ"
            "holy_services" -> "🌸 ಪವಿತ್ರ ಸೇವೆಗಳು"
            "srv_pooja_vidhi" -> "ಪೂಜಾ ವಿಧಾನ"
            "srv_pooja_vidhi_desc" -> "ಮಂತ್ರಗಳು ಮತ್ತು ವಿಧಿಗಳು"
            "srv_hasta_rekha" -> "ಹಸ್ತ ರೇಖೆ"
            "srv_hasta_rekha_desc" -> "ರೇಖೆಗಳ ರಹಸ್ಯಗಳು"
            "srv_sacred_prasad" -> "ಪವಿತ್ರ ಪ್ರಸಾದ"
            "srv_sacred_prasad_desc" -> "ಸಾತ್ವಿಕ ಅಡುಗೆಗಳು"
            "srv_daily_calendar" -> "ದಿನದ ಕ್ಯಾಲೆಂಡರ್"
            "srv_daily_calendar_desc" -> "ತಿಥಿ ಮತ್ತು ರಾಹುಕಾಲ"
            "upcoming_festivals" -> "📿 ಮುಂಬರುವ ಹಬ್ಬಗಳು"
            "subtab_pooja_guides" -> "🪔 ಪೂಜಾ ಮಾರ್ಗದರ್ಶಿಗಳು"
            "subtab_prasad_recipes" -> "🍛 ಪ್ರಸಾದ ಅಡುಗೆಗಳು"
            "subtab_list" -> "📋 ಪೂಜಾ ಪರಿಶೀಲನಾ ಪಟ್ಟಿ"
            "placeholder_pooja_search" -> "ಹುಡುಕಿ ಉದಾ. ಗಣೇಶ್, ದೀಪಾವಳಿ..."
            "placeholder_recipe_search" -> "ಪ್ರಸಾದ ಅಡುಗೆ ಹುಡುಕಿ..."
            "placeholder_item_name" -> "ವಸ್ತು ಉದಾ. ರೋಲಿ"
            "placeholder_item_qty" -> "ಪ್ರಮಾಣ ಉದಾ. 1 ಪ್ಯಾಕೆಟ್"
            "placeholder_panchang_search" -> "ಉದಾ. ದೀಪಾವಳಿ 2026 ನಕ್ಷತ್ರ, ರಾಹು ಕಾಲ"
            "placeholder_palmistry_desc" -> "ನಿಮ್ಮ ಹಸ್ತದ ರೇಖೆಗಳು, ಪರ್ವತಗಳ ಬಗ್ಗೆ ಬರೆಯಿರಿ..."
            "placeholder_chat_input" -> "ಜ್ಯೋತಿಷ್ ಗುರುವನ್ನು ಜ್ಯೋತಿಷ್ಯದ ಬಗ್ಗೆ ಕೇಳಿ..."
            "subtab_hasta_diagram" -> "✋ ಹಸ್ತ ರೇಖಾ ಚಿತ್ರ"
            "subtab_request_reading" -> "✨ ವಿಶ್ಲೇಷಣೆಗೆ ವಿನಂತಿಸಿ"
            "subtab_historic_scrolls" -> "📂 ಹಳೆಯ ಇತಿಹಾಸ"
            "guru_chat_heading" -> "🔱 ಜ್ಯೋತಿಷ್ ಗುರು"
            "sadhaka" -> "🙏 ಸಾಧಕ"
            "contemplating" -> "ಗುರುಗಳು ಗ್ರಹಗಳ ಸ್ಥಿತಿಯನ್ನು ಪರಿಶೀಲಿಸುತ್ತಿದ್ದಾರೆ..."
            "settings_profile_title" -> "ಪ್ರೊಫೈಲ್ ಮತ್ತು ಸೆಟ್ಟಿಂಗ್‌ಗಳು"
            "settings_profile_label" -> "ನಿಮ್ಮ ಪವಿತ್ರ ಹೆಸರನ್ನು ನಮೂದಿಸಿ:"
            "settings_lang_label" -> "ಸಲಹೆಗಾಗಿ ದಿವ್ಯ ಭಾಷೆಯನ್ನು ಆರಿಸಿ:"
            "settings_regional_pref" -> "ಪ್ರಾದೇಶಿಕ ಪೂಜಾ ಆದ್ಯತೆಗಳು:"
            "settings_calendar_std" -> "• ಕ್ಯಾಲೆಂಡರ್ ವಿಧಾನ: ಅಮಾಂತ (ಶ್ರೀಮನ್/ಶಕ)"
            "settings_timezone" -> "• ಸಮಯ ವಲಯ: UTC (+05:30 ಗೆ ಸಮಾನ)"
            "settings_btn_save" -> "ದಿವ್ಯ ಪ್ರೊಫೈಲ್ ಉಳಿಸಿ"
            "settings_btn_cancel" -> "ರದ್ದುಗೊಳಿಸಿ"
            else -> en
        }
        "mr" -> when (key) {
            "tab_home" -> "मुख्यपृष्ठ"
            "tab_pooja" -> "पूजा"
            "tab_panchang" -> "पंचांग"
            "tab_palmistry" -> "हस्तरेषा"
            "tab_ask_guru" -> "गुरू संवाद"
            "btn_ask_astro_guru" -> "ज्योतिषगुरु संवाद"
            "app_subtitle" -> "आपले दिव्य वैदिक मार्गदर्शक"
            "home_greeting_title" -> "🙏 जय श्री राम आणि नमस्ते,"
            "home_greeting_desc" -> "ग्रहांची अनुकूल स्थिती आपल्या जीवनात समृद्धी, स्पष्टता आणि शांतता आणो."
            "panchang_today" -> "📅 आजचे पंचांग"
            "shukla_paksha" -> "शुक्ल पक्ष"
            "tithi" -> "तिथी"
            "ashtami" -> "अष्टमी"
            "ashtami_time" -> "रात्री 08:44 पर्यंत"
            "nakshatra" -> "नक्षत्र"
            "purva_phalguni" -> "पूर्वा फाल्गुनी"
            "venus_lord" -> "शुक्र स्वामी"
            "var" -> "वार"
            "sunday" -> "रविवार"
            "surya_dev" -> "सूर्य देव"
            "shubh_muhurat_time" -> "🌟 शुभ मुहूर्त: सकाळी ११:४५ - दुपारी १२:३५"
            "see_more" -> "अधिक पहा"
            "holy_services" -> "🌸 पवित्र सेवा"
            "srv_pooja_vidhi" -> "पूजा विधी"
            "srv_pooja_vidhi_desc" -> "विधी आणि मंत्र"
            "srv_hasta_rekha" -> "हस्तरेषाशास्त्र"
            "srv_hasta_rekha_desc" -> "हातावरील रेषांचे रहस्य"
            "srv_sacred_prasad" -> "पवित्र प्रसाद"
            "srv_sacred_prasad_desc" -> "सात्त्विक खाद्य रेसिपी"
            "srv_daily_calendar" -> "दैनिक कॅलेंडर"
            "srv_daily_calendar_desc" -> "तिथी आणि राहू काळ"
            "upcoming_festivals" -> "📿 आगामी सण"
            "subtab_pooja_guides" -> "🪔 पूजा मार्गदर्शक"
            "subtab_prasad_recipes" -> "🍛 प्रसाद रेसिपी"
            "subtab_list" -> "📋 पूजा चेकलिस्ट"
            "placeholder_pooja_search" -> "शोधा उदा. गणेश, दिवाळी..."
            "placeholder_recipe_search" -> "शोधा प्रसाद रेसिपी उदा. मोदक..."
            "placeholder_item_name" -> "साहित्य उदा. हळद-कुंकू"
            "placeholder_item_qty" -> "प्रमाण उदा. १ पाकीट"
            "placeholder_panchang_search" -> "उदा. दिवाळी २०२६ नक्षत्र, राहू काळ"
            "placeholder_palmistry_desc" -> "हाताच्या रेषा आणि चिन्हांबद्दल लिहा..."
            "placeholder_chat_input" -> "ज्योतिषगुरुंना ज्योतिषशास्त्राबद्दल विचारा..."
            "subtab_hasta_diagram" -> "✋ हस्तरेषा चित्र"
            "subtab_request_reading" -> "✨ मार्गदर्शनाची विनंती करा"
            "subtab_historic_scrolls" -> "📂 जुने वाचन रेकॉर्ड"
            "guru_chat_heading" -> "🔱 ज्योतिषगुरु"
            "sadhaka" -> "🙏 साधक"
            "contemplating" -> "गुरुदेव ग्रहांच्या स्थितीचे विश्लेषण करत आहेत..."
            "settings_profile_title" -> "प्रोजाइल आणि सेटिंग्ज"
            "settings_profile_label" -> "आपले पवित्र नाव किंवा उपनाव प्रविष्ट करा:"
            "settings_lang_label" -> "सल्ल्यासाठी दिव्य भाषा निवडा:"
            "settings_regional_pref" -> "प्रादेशिक पूजा प्राधान्ये:"
            "settings_calendar_std" -> "• कॅलेंडर पद्धत: अमान्त (श्रीमंत / शक)"
            "settings_timezone" -> "• वेळ क्षेत्र: UTC (+०५:३० च्या समान)"
            "settings_btn_save" -> "दिव्य प्रोफाइल जतन करा"
            "settings_btn_cancel" -> "रद्द करा"
            else -> en
        }
        "bn" -> when (key) {
            "tab_home" -> "হোম"
            "tab_pooja" -> "পুজো"
            "tab_panchang" -> "পঞ্জিকা"
            "tab_palmistry" -> "হস্তরেখা"
            "tab_ask_guru" -> "গুরুকে জিজ্ঞাসা করুন"
            "btn_ask_astro_guru" -> "জ্যোতিষগুরুকে জিজ্ঞাসা করুন"
            "app_subtitle" -> "আপনার ঐশ্বরিক বৈদিক নির্দেশক"
            "home_greeting_title" -> "🙏 জয় শ্রী রাম ও নমস্কার,"
            "home_greeting_desc" -> "গ্রহের শুভ সংযোগ আপনার জীবনে সমৃদ্ধি, স্পষ্টতা এবং শান্তি নিয়ে আসুক।"
            "panchang_today" -> "📅 আজকের পঞ্জিকা"
            "shukla_paksha" -> "শুক্ল পক্ষ"
            "tithi" -> "তিথি"
            "ashtami" -> "অষ্টমী"
            "ashtami_time" -> "রাত 08:44 পর্যন্ত"
            "nakshatra" -> "নক্ষত্র"
            "purva_phalguni" -> "পূর্ব ফাল্গুনী"
            "venus_lord" -> "শুক্র অধিপতি"
            "var" -> "বার"
            "sunday" -> "রবিবার"
            "surya_dev" -> "সূর্য দেব"
            "shubh_muhurat_time" -> "🌟 শুভ মুহূর্ত: সকাল ১১:৪৫ - দুপুর ১২:৩৫"
            "see_more" -> "আরও দেখুন"
            "holy_services" -> "🌸 পবিত্র সেবা"
            "srv_pooja_vidhi" -> "পুজো পদ্ধতি"
            "srv_pooja_vidhi_desc" -> "মন্ত্র ও বিধি"
            "srv_hasta_rekha" -> "হস্তরেখা বিচার"
            "srv_hasta_rekha_desc" -> "হাতের রেখার রহস্য"
            "srv_sacred_prasad" -> "পবিত্র প্রসাদ"
            "srv_sacred_prasad_desc" -> "সাত্ত্বিক রান্না প্রণালী"
            "srv_daily_calendar" -> "দৈনিক পঞ্জিকা"
            "srv_daily_calendar_desc" -> "তিথি ও রাহু কাল"
            "upcoming_festivals" -> "📿 আসন্ন উৎসব"
            "subtab_pooja_guides" -> "🪔 পুজো নির্দেশিকা"
            "subtab_prasad_recipes" -> "🍛 প্রসাদ রেসিপি"
            "subtab_list" -> "📋 পুজো চেকলিস্ট"
            "placeholder_pooja_search" -> "খুঁজুন উদা. গণেশ, দীপাবলি..."
            "placeholder_recipe_search" -> "প্রসাদ রেসিপি খুঁজুন উদা. মোদক..."
            "placeholder_item_name" -> "সামগ্রী যেমন রলি"
            "placeholder_item_qty" -> "পরিমাণ যেমন ১ প্যাকেট"
            "placeholder_panchang_search" -> "উদা. দীপাবলি ২০২৬ নক্ষত্র, রাহু কাল"
            "placeholder_palmistry_desc" -> "আপনার হাতের রেখা ও পর্বতের বিষয়ে লিখুন..."
            "placeholder_chat_input" -> "জ্যোতিষগুরুকে জ্যোতিষশাস্ত্র সম্পর্কে জিজ্ঞাসা করুন..."
            "subtab_hasta_diagram" -> "✋ হস্তরেखा চিত্র"
            "subtab_request_reading" -> "✨ বিশ্লেষণের অনুরোধ"
            "subtab_historic_scrolls" -> "📂 পুরনো ইতিহাস"
            "guru_chat_heading" -> "🔱 জ্যোতিষগুরু"
            "sadhaka" -> "🙏 সাধক"
            "contemplating" -> "গুরুদেব গ্রহগুলির শুভ সংযোগ বিশ্লেষণ করছেন..."
            "settings_profile_title" -> "প্রোফাইল এবং সেটিংস"
            "settings_profile_label" -> "আপনার পবিত্র নাম বা ডাকনাম লিখুন:"
            "settings_lang_label" -> "পরামর্শের জন্য ভাষা নির্বাচন করুন:"
            "settings_regional_pref" -> "আঞ্চলিক পুজোর পছন্দগুলি:"
            "settings_calendar_std" -> "• ক্যালেন্ডার মানক: অমান্ত (শ্রীমান/শক)"
            "settings_timezone" -> "• সময় অঞ্চল: UTC (+05:30 এর সমতুল্য)"
            "settings_btn_save" -> "ঐশ্বরিক প্রোফাইল সংরক্ষণ করুন"
            "settings_btn_cancel" -> "বাতিল করুন"
            else -> en
        }
        "gu" -> when (key) {
            "tab_home" -> "હોમ"
            "tab_pooja" -> "પૂજા"
            "tab_panchang" -> "પંચાંગ"
            "tab_palmistry" -> "હસ્તરેખા"
            "tab_ask_guru" -> "ગુરુને પૂછો"
            "btn_ask_astro_guru" -> "જ્યોતિષ ગુરુને પૂછો"
            "app_subtitle" -> "આપનું દિવ્ય વૈદિક માર્ગદર્શક"
            "home_greeting_title" -> "🙏 જય શ્રી રામ અને નમસ્તે,"
            "home_greeting_desc" -> "ગ્રહોની સાનુકૂળ સ્થિતિ આપના જીવનમાં સમૃદ્ધિ, સ્પષ્ટતા અને શાંતિ લાવે."
            "panchang_today" -> "📅 આજનું પંચાંગ"
            "shukla_paksha" -> "શુકલ પક્ષ"
            "tithi" -> "તિથિ"
            "ashtami" -> "અષ્ટમી"
            "ashtami_time" -> "રાત્રે 08:44 સુધી"
            "nakshatra" -> "નક્ષત્ર"
            "purva_phalguni" -> "પૂર્વા ફાલ્ગુની"
            "venus_lord" -> "શુક્ર અધિપતિ"
            "var" -> "વાર"
            "sunday" -> "રવિવાર"
            "surya_dev" -> "સૂર્ય દેવ"
            "shubh_muhurat_time" -> "🌟 શુભ મુહૂર્ત: સવારે 11:45 - બપોરે 12:35"
            "see_more" -> "વધુ જુઓ"
            "holy_services" -> "🌸 પવિત્ર સેવાઓ"
            "srv_pooja_vidhi" -> "પૂજા વિધિ"
            "srv_pooja_vidhi_desc" -> "મંત્રો અને વિધિ"
            "srv_hasta_rekha" -> "હસ્તરેખા શાસ્ત્ર"
            "srv_hasta_rekha_desc" -> "હસ્તરેખા રહસ્યો"
            "srv_sacred_prasad" -> "પવિત્ર પ્રસાદ"
            "srv_sacred_prasad_desc" -> "સાત્વિક વાનગીઓ"
            "srv_daily_calendar" -> "દૈનિક કેલેન્ડર"
            "srv_daily_calendar_desc" -> "તિથિ અને રાહુ કાળ"
            "upcoming_festivals" -> "📿 આગામી તહેવારો"
            "subtab_pooja_guides" -> "🪔 પૂજા માર્ગદર્શન"
            "subtab_prasad_recipes" -> "🍛 પ્રસાદ રેસિપી"
            "subtab_list" -> "📋 પૂજા ચેકલિસ્ટ"
            "placeholder_pooja_search" -> "શોધો દા.ત. ગણેશ, દિવાળી..."
            "placeholder_recipe_search" -> "શોધો પ્રસાદ રેસિપી દા.ત. મોદક..."
            "placeholder_item_name" -> "સામગ્રી જેમ કે રોલી"
            "placeholder_item_qty" -> "જથ્થો દા.ત. ૧ પેકેટ"
            "placeholder_panchang_search" -> "દા.ત. દિવાળી ૨૦૨૬ નક્ષત્ર, રાહુ કાળ"
            "placeholder_palmistry_desc" -> "તમારા હાથની રેખાઓ અને પર્વતો વિશે લખો..."
            "placeholder_chat_input" -> "જ્યોતિષ ગુરુને જ્યોતિષશાસ્ત્ર વિશે પૂછો..."
            "subtab_hasta_diagram" -> "✋ હસ્તરેખા નકશો"
            "subtab_request_reading" -> "✨ વિશ્લેષણ વિનંતી"
            "subtab_historic_scrolls" -> "📂 જૂનો ઇતિહાસ"
            "guru_chat_heading" -> "🔱 જ્યોતિષ ગુરુ"
            "sadhaka" -> "🙏 સાધક"
            "contemplating" -> "ગુરુદેવ ગ્રહોના યોગોનું વિશ્લેષણ કરી રહ્યા છે..."
            "settings_profile_title" -> "પ્રોફાઇલ અને સેટિંગ્સ"
            "settings_profile_label" -> "તમારું પવિત્ર નામ અથવા ઉપનામ લખો:"
            "settings_lang_label" -> "સલાહ માટે દિવ્ય ભાષા પસંદ કરો:"
            "settings_regional_pref" -> "પ્રાદેશિક પૂજા પસંદગીઓ:"
            "settings_calendar_std" -> "• કેલેન્ડર સ્ટાન્ડર્ડ: અમાનતા (શ્રીમાન/શક)"
            "settings_timezone" -> "• ટાઇમઝોન: UTC (+05:30 ને સમકક્ષ)"
            "settings_btn_save" -> "દિવ્ય પ્રોફાઇલ સાચવો"
            "settings_btn_cancel" -> "રદ કરો"
            else -> en
        }
        else -> en
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }
    var showOnboarding by remember { mutableStateOf(false) }
    var onboardingIndex by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("Sadhaka") }
    var tempNameInput by remember { mutableStateOf("") }
    var editingName by remember { mutableStateOf(false) }
    
    val currentLang by viewModel.appLanguage.collectAsState()
    var tempLanguageSelected by remember { mutableStateOf("en") }

    // Navigation Tab state
    val currentTab by viewModel.currentTab.collectAsState()

    // Database streams
    val readings by viewModel.savedReadings.collectAsState()
    val recipes by viewModel.savedRecipes.collectAsState()
    val fullChecklist by viewModel.poojaChecklist.collectAsState()

    // --- Launch and Splash handler ---
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000) // Splash delay
        showSplash = false
        // Show onboarding if never completed (mock check)
        showOnboarding = true
    }

    // --- Main Layout Container ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (showSplash) {
            SplashScreenView()
        } else if (showOnboarding) {
            OnboardingView(
                activeIndex = onboardingIndex,
                userName = userName,
                onNameChange = { userName = it },
                onNext = {
                    if (onboardingIndex < 2) {
                        onboardingIndex++
                    } else {
                        showOnboarding = false
                    }
                },
                onSkip = {
                    showOnboarding = false
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "ॐ",
                                    fontSize = 28.sp,
                                    color = AntiqueGold,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                                Column {
                                    Text(
                                        text = "JyotishGuru",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            color = if (isDarkTheme) CreamWhite else DeepMaroon
                                        )
                                    )
                                    Text(
                                        text = "Your Divine Vedic Guide",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isDarkTheme) AntiqueGold else SecondaryText,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = onToggleTheme) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.NightsStay,
                                    contentDescription = "Toggle Theme",
                                    tint = AntiqueGold
                                )
                            }
                            IconButton(onClick = { 
                                tempNameInput = userName
                                tempLanguageSelected = currentLang
                                editingName = true 
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = AntiqueGold
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if (isDarkTheme) MidnightIndigo else DeepMaroon,
                            titleContentColor = CreamWhite,
                            actionIconContentColor = CreamWhite
                        ),
                        modifier = Modifier.border(
                            width = 0.5.dp,
                            brush = Brush.verticalGradient(listOf(AntiqueGold, Color.Transparent)),
                            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = if (isDarkTheme) MidnightIndigo else DeepMaroon,
                        tonalElevation = 8.dp,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = AntiqueGold.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                    ) {
                        val tabs = listOf(
                            Triple("home", Icons.Default.Home, tr("tab_home", currentLang)),
                            Triple("pooja", Icons.Default.Fireplace, tr("tab_pooja", currentLang)),
                            Triple("panchang", Icons.Default.CalendarMonth, tr("tab_panchang", currentLang)),
                            Triple("palmistry", Icons.Default.BackHand, tr("tab_palmistry", currentLang)),
                            Triple("chat", Icons.Default.Chat, tr("tab_ask_guru", currentLang))
                        )

                        tabs.forEach { (tabId, icon, label) ->
                            val isSelected = currentTab == tabId
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.selectTab(tabId) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        modifier = if (isSelected) Modifier.scale(1.15f) else Modifier
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) RoyalSaffron else CreamWhite.copy(alpha = 0.7f)
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = RoyalSaffron,
                                    selectedTextColor = RoyalSaffron,
                                    unselectedIconColor = CreamWhite.copy(alpha = 0.7f),
                                    unselectedTextColor = CreamWhite.copy(alpha = 0.7f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                },
                floatingActionButton = {
                    if (currentTab != "chat") {
                        ExtendedFloatingActionButton(
                            onClick = { viewModel.selectTab("chat") },
                            containerColor = RoyalSaffron,
                            contentColor = CreamWhite,
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            modifier = Modifier
                                .border(1.5.dp, AntiqueGold, CircleShape)
                                .padding(2.dp),
                            shape = CircleShape
                        ) {
                            Text("ॐ", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 4.dp))
                            Text(tr("btn_ask_astro_guru", currentLang))
                        }
                    }
                }
            ) { innerPadding ->
                // Banner alert when Gemini Key is unconfigured
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (viewModel.isApiKeyMissing) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning, 
                                    contentDescription = "Alert",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = "AI Key not set. Please add GEMINI_API_KEY in the Secrets panel for fully accurate predictions. Running in spiritual guidelines demo mode.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Render Selected Screen Tab
                    Box(modifier = Modifier.weight(1f)) {
                        when (currentTab) {
                            "home" -> HomeScreenView(
                                viewModel = viewModel,
                                userName = userName,
                                isDarkTheme = isDarkTheme,
                                onNavigateToPooja = { viewModel.selectTab("pooja") },
                                onNavigateToPanchang = { viewModel.selectTab("panchang") },
                                onNavigateToPalmistry = { viewModel.selectTab("palmistry") }
                            )
                            "pooja" -> PoojaGuideView(viewModel = viewModel, isDarkTheme = isDarkTheme)
                            "panchang" -> PanchangCalendarView(viewModel = viewModel, isDarkTheme = isDarkTheme)
                            "palmistry" -> PalmistryView(viewModel = viewModel, isDarkTheme = isDarkTheme, readings = readings)
                            "chat" -> AstroChatView(viewModel = viewModel, isDarkTheme = isDarkTheme)
                        }
                    }
                }
            }
        }

        // --- Editing Name Dialog ---
        if (editingName) {
            AlertDialog(
                onDismissRequest = { editingName = false },
                title = { Text(tr("settings_profile_title", currentLang), fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(tr("settings_profile_label", currentLang), style = MaterialTheme.typography.bodyMedium)
                        OutlinedTextField(
                            value = tempNameInput,
                            onValueChange = { tempNameInput = it },
                            placeholder = { Text("e.g. Kaushika, Priyadarshani") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(tr("settings_lang_label", currentLang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = DeepMaroon)
                        
                        val languageOpts = listOf(
                            "en" to "English",
                            "hi" to "हिंदी (Hindi)",
                            "te" to "తెలుగు (Telugu)",
                            "ta" to "தமிழ் (Tamil)",
                            "kn" to "ಕನ್ನಡ (Kannada)",
                            "mr" to "मराठी (Marathi)",
                            "bn" to "বাংলা (Bengali)",
                            "gu" to "ગુજરાતી (Gujarati)"
                        )
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            items(languageOpts) { (code, label) ->
                                val isSelected = tempLanguageSelected == code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) RoyalSaffron else ChandanBeige.copy(alpha = 0.4f))
                                        .border(1.dp, if (isSelected) AntiqueGold else ChandanBeige, RoundedCornerShape(16.dp))
                                        .clickable { tempLanguageSelected = code }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) CreamWhite else PrimaryText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(tr("settings_regional_pref", currentLang), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(tr("settings_calendar_std", currentLang), style = MaterialTheme.typography.bodyMedium)
                        Text(tr("settings_timezone", currentLang), style = MaterialTheme.typography.bodyMedium)
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                        onClick = {
                            if (tempNameInput.trim().isNotEmpty()) {
                                userName = tempNameInput.trim()
                            }
                            viewModel.updateLanguage(tempLanguageSelected)
                            editingName = false
                        }
                    ) {
                        Text(tr("settings_btn_save", currentLang))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingName = false }) {
                        Text(tr("settings_btn_cancel", currentLang), color = SecondaryText)
                    }
                },
                containerColor = LotusCream,
                textContentColor = PrimaryText,
                titleContentColor = DeepMaroon
            )
        }
    }
}

// --- Dynamic Scale Extension ---
fun Modifier.scale(scale: Float) = this.then(
    Modifier.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
)

// --- SPLASH SCREEN COMPOSABLE ---
@Composable
fun SplashScreenView() {
    val infiniteTransition = rememberInfiniteTransition(label = "OM Loading")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OM scale"
    )
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Star rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepMaroon, Color(0xFF5E0000), Color(0xFF230000))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Soft rotating celestial star constellation map background
        Box(
            modifier = Modifier
                .size(320.dp)
                .rotate(rotationAngle)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(AntiqueGold.copy(alpha = 0.08f), Color.Transparent)
                    )
                )
                .border(1.dp, AntiqueGold.copy(alpha = 0.15f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Blooming lotus and OM flower
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Lotus petal outlines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2.3f
                    val centerOffset = this.center
                    for (i in 0 until 8) {
                        val angle = i * Math.PI / 4
                        val petalX = centerOffset.x + radius * Math.cos(angle).toFloat()
                        val petalY = centerOffset.y + radius * Math.sin(angle).toFloat()
                        drawCircle(
                            color = AntiqueGold.copy(alpha = 0.3f),
                            radius = radius * 0.45f,
                            center = androidx.compose.ui.geometry.Offset(petalX, petalY),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }

                // Center OM Symbol
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .scale(glowScale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(RoyalSaffron, Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ॐ",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = AntiqueGold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Title and Subtitle
            Text(
                text = "JyotishGuru",
                color = AntiqueGold,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 2.sp
            )

            Text(
                text = "Your Divine Guide to Hindu Wisdom",
                color = CreamWhite,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Footer copyright/blessing
        Text(
            text = "ज्योतिषोऽयनं चक्षुः",
            color = AntiqueGold.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

// --- ONBOARDING COMPOSABLE ---
@Composable
fun OnboardingView(
    activeIndex: Int,
    userName: String,
    onNameChange: (String) -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val screenData = listOf(
        Triple(
            "Know Your Panchang Daily",
            "Instantly view auspicious Shubh Muhurats, Rahu Kaal alerts, daily tithi transitions, and Nakshatra influences derived from Vedic calendars.",
            "📅"
        ),
        Triple(
            "Explore Palmistry Mysteries",
            "Examine your Life, Heart, Head, and Fate lines with ancient Vedic Hasta Samudrika Shastra secrets. Interpret the energetic mounts of your palms.",
            "✋"
        ),
        Triple(
            "Perfect Every Pooja & Prasad",
            "Acquire step-by-step pooja guides, sacred Devanagari mantras, and pure satvik prasad recipes to worship your favorite deities seamlessly.",
            "🪔"
        )
    )

    val currentData = screenData[activeIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LotusCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onSkip) {
                Text("Skip", color = DeepMaroon, fontWeight = FontWeight.Bold)
            }
        }

        // Graphic Representation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(DeepMaroon.copy(alpha = 0.08f), CircleShape)
                    .border(1.5.dp, AntiqueGold.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentData.third,
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = currentData.first,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = DeepMaroon,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentData.second,
                fontSize = 14.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            if (activeIndex == 2) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = LotusCream),
                    border = BorderStroke(1.dp, ChandanBeige),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "✍️ Enter Your Name for Custom Blessings:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepMaroon
                        )
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { onNameChange(it) },
                            placeholder = { Text("e.g. Kaushika, Priyadarshani") },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = CreamWhite,
                                unfocusedContainerColor = CreamWhite,
                                focusedTextColor = PrimaryText,
                                unfocusedTextColor = PrimaryText,
                                focusedIndicatorColor = RoyalSaffron
                            )
                        )
                    }
                }
            }
        }

        // Indicator & Action Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Saffron Indicator Dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(if (activeIndex == i) 16.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (activeIndex == i) RoyalSaffron else ChandanBeige)
                    )
                }
            }

            // Next / Done button
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.5.dp, AntiqueGold, RoundedCornerShape(24.dp))
            ) {
                Text(
                    text = if (activeIndex == 2) "Jai JyotishGuru (Begin)" else "Continue",
                    style = MaterialTheme.typography.labelLarge,
                    color = CreamWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- HOME SCREEN VIEW COMPOSABLE ---
@Composable
fun HomeScreenView(
    viewModel: JyotishViewModel,
    userName: String,
    isDarkTheme: Boolean,
    onNavigateToPooja: () -> Unit,
    onNavigateToPanchang: () -> Unit,
    onNavigateToPalmistry: () -> Unit
) {
    val currentLang by viewModel.appLanguage.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Greeting Heading
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF2E2660) else DeepMaroon),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, AntiqueGold, RoundedCornerShape(16.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = tr("home_greeting_title", currentLang),
                            fontSize = 14.sp,
                            color = AntiqueGold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userName,
                            fontSize = 26.sp,
                            color = CreamWhite,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = tr("home_greeting_desc", currentLang),
                            fontSize = 13.sp,
                            color = CreamWhite.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Today's Panchang Quick Glance Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) MidnightIndigo else LotusCream),
                border = BorderStroke(1.dp, ChandanBeige),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tr("panchang_today", currentLang),
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) AntiqueGold else DeepMaroon,
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(PeacockTeal, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(tr("shukla_paksha", currentLang), color = CreamWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PanchangItem(tr("tithi", currentLang), tr("ashtami", currentLang), tr("ashtami_time", currentLang))
                        PanchangItem(tr("nakshatra", currentLang), tr("purva_phalguni", currentLang), tr("venus_lord", currentLang))
                        PanchangItem(tr("var", currentLang), tr("sunday", currentLang), tr("surya_dev", currentLang))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Auspicious times banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoyalSaffron.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = tr("shubh_muhurat_time", currentLang),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) AntiqueGold else DeepMaroon
                        )
                        TextButton(
                            onClick = onNavigateToPanchang,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(tr("see_more", currentLang), color = RoyalSaffron, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Interactive Feature Ribbon (2x2 grid)
        item {
            Text(
                text = tr("holy_services", currentLang),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = if (isDarkTheme) AntiqueGold else PrimaryText,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureCard(
                        title = tr("srv_pooja_vidhi", currentLang),
                        description = tr("srv_pooja_vidhi_desc", currentLang),
                        emoji = "🪔",
                        gradient = Brush.linearGradient(listOf(DeepMaroon, Color(0xFFB51A1A))),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToPooja
                    )
                    FeatureCard(
                        title = tr("srv_hasta_rekha", currentLang),
                        description = tr("srv_hasta_rekha_desc", currentLang),
                        emoji = "✋",
                        gradient = Brush.linearGradient(listOf(MidnightIndigo, Color(0xFF3B2A82))),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToPalmistry
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureCard(
                        title = tr("srv_sacred_prasad", currentLang),
                        description = tr("srv_sacred_prasad_desc", currentLang),
                        emoji = "🍛",
                        gradient = Brush.linearGradient(listOf(Color(0xFFFF6F00), Color(0xFFFF9F00))),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToPooja // Let them view via Pooja/Recipes sub-tab
                    )
                    FeatureCard(
                        title = tr("srv_daily_calendar", currentLang),
                        description = tr("srv_daily_calendar_desc", currentLang),
                        emoji = "📅",
                        gradient = Brush.linearGradient(listOf(Color(0xFF004D40), PeacockTeal)),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToPanchang
                    )
                }
            }
        }

        // Upcoming Festivals Slider & Quick Checklists
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr("upcoming_festivals", currentLang),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = if (isDarkTheme) AntiqueGold else PrimaryText
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            val festivals = listOf(
                FestivalItem("Diwali 2026", "Festival of Lights", "Lakshmi Pooja", "7 Days Left", DeepMaroon),
                FestivalItem("Ganesh Chaturthi", "Durva & Modak Bhog", "Lord Ganesha", "Sept 20", RoyalSaffron),
                FestivalItem("Navratri Shubh", "Nine Sacred Nights", "Ma Durga Shailaputri", "Oct 12", PeacockTeal),
                FestivalItem("Satyanarayan Katha", "Full Moon Ritual", "Lord Vishnu", "Purnima", ChandanBeige)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(festivals) { fest ->
                    Card(
                        modifier = Modifier
                            .width(230.dp)
                            .clickable {
                                onNavigateToPooja()
                                viewModel.generatePoojaGuide(fest.name)
                            },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(fest.colorHex.copy(alpha = 0.9f), fest.colorHex)
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fest.countdownText,
                                    color = AntiqueGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(text = "🕉️", fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = fest.name,
                                color = CreamWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )

                            Text(
                                text = fest.subtitle,
                                color = CreamWhite.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Dedicated to: ${fest.deity}",
                                color = AntiqueGold,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CreamWhite.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(vertical = 4.dp, horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Launch Pooja Guide →",
                                    color = CreamWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Vedic Wisdom Quotes Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) Color(0xFF1E1C38) else LotusCream),
                border = BorderStroke(1.dp, AntiqueGold.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "“कर्मण्येवाधिकारस्ते मा फलेषु कदाचन”",
                        color = DeepMaroon,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Translation: You have a right to perform your prescribed duties, but you are not entitled under any circumstance to the fruits of your actions.",
                        color = SecondaryText,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun VedicMarkdownText(
    text: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val lines = text.lines()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        lines.forEach { rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
            } else if (line.startsWith("###")) {
                val headerText = line.removePrefix("###").trim()
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) AntiqueGold else DeepMaroon
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            } else if (line.startsWith("##")) {
                val headerText = line.removePrefix("##").trim()
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) AntiqueGold else DeepMaroon
                    ),
                    modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                )
            } else if (line.startsWith("#")) {
                val headerText = line.removePrefix("#").trim()
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDarkTheme) AntiqueGold else DeepMaroon
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            } else if (line.startsWith("-") || line.startsWith("*") || line.startsWith("•")) {
                var prefix = "🔸"
                var content = line
                if (line.startsWith("-")) {
                    content = line.removePrefix("-").trim()
                } else if (line.startsWith("*")) {
                    content = line.removePrefix("*").trim()
                } else if (line.startsWith("•")) {
                    content = line.removePrefix("•").trim()
                }
                
                Row(
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = prefix,
                        color = RoyalSaffron,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = parseFormattedText(content, isDarkTheme),
                        fontSize = 13.sp,
                        color = if (isDarkTheme) CreamWhite else PrimaryText,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Text(
                    text = parseFormattedText(line, isDarkTheme),
                    fontSize = 13.sp,
                    color = if (isDarkTheme) CreamWhite.copy(alpha = 0.95f) else PrimaryText,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

fun parseFormattedText(text: String, isDarkTheme: Boolean): androidx.compose.ui.text.AnnotatedString {
    return androidx.compose.ui.text.buildAnnotatedString {
        val parts = text.split("**")
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                withStyle(
                    style = androidx.compose.ui.text.SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) AntiqueGold else DeepMaroon
                    )
                ) {
                    append(part)
                }
            } else {
                append(part)
            }
        }
    }
}

@Composable
fun PanchangItem(label: String, value: String, description: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = SecondaryText)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
        Text(text = description, fontSize = 9.sp, color = SecondaryText.copy(alpha = 0.8f))
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    emoji: String,
    gradient: Brush,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(95.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = AntiqueGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        color = CreamWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = description,
                        color = CreamWhite.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// --- POOJA GUIDE VIEW SCREEN (COVERS BOTH POOJA & RECIPES INTERNALLY VISUALLY IN TABS) ---
@Composable
fun PoojaGuideView(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean
) {
    val currentLang by viewModel.appLanguage.collectAsState()
    var searchInput by remember { mutableStateOf("") }
    val poojaState by viewModel.poojaState.collectAsState()
    val recipeState by viewModel.recipeState.collectAsState()

    var activeSubTab by remember { mutableStateOf("guides") } // "guides" | "recipes" | "checklists"
    val checklistItems by viewModel.poojaChecklist.collectAsState()

    // Mock search suggestions
    val templates = listOf(
        "Ganesh Chaturthi",
        "Diwali Lakshmi Pooja",
        "Satyanarayan Katha",
        "Navratri Durga Sthapana",
        "Griha Pravesh (House Warming)",
        "Vivah (Marriage Ceremony)",
        "Maha Shivratri Abhishekam"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        // Sub-tabs
        TabRow(
            selectedTabIndex = when (activeSubTab) {
                "guides" -> 0
                "recipes" -> 1
                else -> 2
            },
            containerColor = Color.Transparent,
            contentColor = DeepMaroon,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[
                        when (activeSubTab) {
                            "guides" -> 0
                            "recipes" -> 1
                            else -> 2
                        }
                    ]),
                    color = RoyalSaffron
                )
            }
        ) {
            Tab(
                selected = activeSubTab == "guides",
                onClick = { activeSubTab = "guides" },
                text = { Text(tr("subtab_pooja_guides", currentLang), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeSubTab == "recipes",
                onClick = { activeSubTab = "recipes" },
                text = { Text(tr("subtab_prasad_recipes", currentLang), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activeSubTab == "checklists",
                onClick = { activeSubTab = "checklists" },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(tr("subtab_list", currentLang) + " ", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        if (checklistItems.isNotEmpty()) {
                            Badge(containerColor = DeepMaroon, contentColor = CreamWhite) {
                                Text(checklistItems.size.toString())
                            }
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeSubTab) {
            "guides" -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Search or choose a divine spiritual occasion to view correct procedures, mantras, and materials list:",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )

                    OutlinedTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
                        placeholder = { Text(tr("placeholder_pooja_search", currentLang)) },
                        trailingIcon = {
                            if (searchInput.isNotEmpty()) {
                                IconButton(onClick = { searchInput = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (searchInput.trim().isNotEmpty()) {
                                viewModel.generatePoojaGuide(searchInput)
                            }
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = LotusCream,
                            unfocusedContainerColor = LotusCream,
                            focusedTextColor = PrimaryText,
                            unfocusedTextColor = PrimaryText,
                            focusedIndicatorColor = RoyalSaffron
                        )
                    )

                    // Suggestion Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val filteredList = if (searchInput.isEmpty()) {
                            templates
                        } else {
                            templates.filter { it.contains(searchInput, ignoreCase = true) }
                        }
                        items(filteredList) { item ->
                            SuggestionChip(
                                onClick = {
                                    searchInput = item
                                    viewModel.generatePoojaGuide(item)
                                },
                                label = { Text(item, fontSize = 11.sp, maxLines = 1) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = RoyalSaffron.copy(alpha = 0.08f),
                                    labelColor = DeepMaroon
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Guidance Output Area
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.5.dp, AntiqueGold, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = LotusCream)
                    ) {
                        PoojaGuidanceOutput(
                            state = poojaState,
                            onSave = { content ->
                                viewModel.saveCurrentReading(
                                    category = "Pooja",
                                    title = if (searchInput.isNotEmpty()) searchInput else "Pooja Vidhi Guide",
                                    query = searchInput,
                                    result = content
                                )
                            },
                            onLoadChecklist = { text ->
                                val name = if (searchInput.isNotEmpty()) searchInput else "Selected Pooja"
                                viewModel.parseAndLoadPoojaChecklist(name, text)
                                activeSubTab = "checklists"
                            }
                        )
                    }
                }
            }
            "recipes" -> {
                var recipeSearchInput by remember { mutableStateOf("") }
                val recipeTemplates = listOf(
                    "Modak (Ganesh Bhog)",
                    "Kaju Katli (Lakshmi)",
                    "Nariyal Barfi",
                    "Kheer (Satvik Rice Milk)",
                    "Navratri Singhara Halwa",
                    "Panchamrit Mixture"
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = "Vedic Sacred Satvik Cooking & Vrat Recipes. Offer divine bhog to details:",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )

                    OutlinedTextField(
                        value = recipeSearchInput,
                        onValueChange = { recipeSearchInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Restaurant, contentDescription = "Restaurant icon") },
                        placeholder = { Text(tr("placeholder_recipe_search", currentLang)) },
                        trailingIcon = {
                            if (recipeSearchInput.isNotEmpty()) {
                                IconButton(onClick = { recipeSearchInput = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (recipeSearchInput.trim().isNotEmpty()) {
                                viewModel.generatePrasadRecipe(recipeSearchInput)
                            }
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = LotusCream,
                            unfocusedContainerColor = LotusCream,
                            focusedTextColor = PrimaryText,
                            unfocusedTextColor = PrimaryText,
                            focusedIndicatorColor = RoyalSaffron
                        )
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(recipeTemplates) { rep ->
                            SuggestionChip(
                                onClick = {
                                    recipeSearchInput = rep
                                    viewModel.generatePrasadRecipe(rep)
                                },
                                label = { Text(rep, fontSize = 11.sp) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = RoyalSaffron.copy(alpha = 0.08f),
                                    labelColor = DeepMaroon
                                )
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.5.dp, AntiqueGold, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = LotusCream)
                    ) {
                        RecipeGuidanceOutput(
                            state = recipeState,
                            onSaveRecipe = { content ->
                                viewModel.saveCustomRecipe(
                                    recipeName = if (recipeSearchInput.isNotEmpty()) recipeSearchInput else "Tradition Bhog",
                                    occasion = "Hindu Ritual Festivity",
                                    ingredients = "See guide below",
                                    instructions = content,
                                    category = "Prasad"
                                )
                            }
                        )
                    }
                }
            }
            "checklists" -> {
                ChecklistManagerView(viewModel = viewModel, isDarkTheme = isDarkTheme)
            }
        }
    }
}

@Composable
fun PoojaGuidanceOutput(
    state: LlmState,
    onSave: (String) -> Unit,
    onLoadChecklist: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        when (state) {
            is LlmState.Idle -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🕯️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Vaidika Vidhi Mandap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepMaroon
                    )
                    Text(
                        text = "Select an auspicious Pooja or type your custom celebration to obtain the precise step-by-step guidance of JyotishGuru.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
            is LlmState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = RoyalSaffron)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Assembling Holy Mantras & Materials...",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = DeepMaroon
                    )
                }
            }
            is LlmState.Success -> {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    // Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSave(state.text) },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepMaroon),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = "Save", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Ritual", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onLoadChecklist(state.text) },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Icon(Icons.Default.PlaylistAddCheck, contentDescription = "List", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Load Samagri Checklist", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    VedicMarkdownText(
                        text = state.text,
                        isDarkTheme = false
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
            is LlmState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline, 
                        contentDescription = "Error", 
                        tint = DeepMaroon,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeGuidanceOutput(
    state: LlmState,
    onSaveRecipe: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        when (state) {
            is LlmState.Idle -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🍛", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Maha Prasad Rasoi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepMaroon
                    )
                    Text(
                        text = "Obtain satvik directions to prepare divine collections of modaks, halwas, panchamrits, and fasting food (vrat ka khana) with zero onion or garlic.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
            is LlmState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = RoyalSaffron)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Consulting Vedic Food Guidelines...",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = DeepMaroon
                    )
                }
            }
            is LlmState.Success -> {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Button(
                        onClick = { onSaveRecipe(state.text) },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepMaroon),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Prasad To Favorites")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    VedicMarkdownText(
                        text = state.text,
                        isDarkTheme = false
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
            is LlmState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline, 
                        contentDescription = "Error", 
                        tint = DeepMaroon,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// --- ACTIVE SHOPPING CHECKLIST MANAGER ---
@Composable
fun ChecklistManagerView(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean
) {
    val checklistItems by viewModel.poojaChecklist.collectAsState()
    var newItemName by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Form to add custom items to current shopping cart
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) MidnightIndigo else LotusCream),
            border = BorderStroke(1.dp, ChandanBeige),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "AddItem to Holy Samagri Checklist",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepMaroon
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        modifier = Modifier.weight(1.5f),
                        placeholder = { Text("Item e.g. Roli") },
                        colors = TextFieldDefaults.colors(focusedIndicatorColor = RoyalSaffron)
                    )
                    OutlinedTextField(
                        value = newItemQty,
                        onValueChange = { newItemQty = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Qty e.g. 1 packet") },
                        colors = TextFieldDefaults.colors(focusedIndicatorColor = RoyalSaffron)
                    )
                    IconButton(
                        onClick = {
                            if (newItemName.trim().isNotEmpty()) {
                                viewModel.addCustomChecklistItem(
                                    poojaName = "My Customs",
                                    itemName = newItemName.trim(),
                                    quantity = if (newItemQty.trim().isEmpty()) "As needed" else newItemQty.trim()
                                )
                                newItemName = ""
                                newItemQty = ""
                            }
                        },
                        modifier = Modifier
                            .background(RoyalSaffron, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add custom item", tint = CreamWhite)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shopping List Item Count: ${checklistItems.size}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isDarkTheme) AntiqueGold else DeepMaroon
            )
            if (checklistItems.isNotEmpty()) {
                TextButton(
                    onClick = { viewModel.clearPoojaChecklist("My Customs"); viewModel.clearPoojaChecklist("Selected Pooja"); viewModel.clearPoojaChecklist("Ganesh Chaturthi"); viewModel.clearPoojaChecklist("Diwali Lakshmi Pooja") },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Clear All", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        if (checklistItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛍️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Checklist is empty.",
                        color = SecondaryText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Generate any Pooja Vidhi above and click 'Load Samagri Checklist' to fill this dynamically, or add custom entries!",
                        color = SecondaryText.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(checklistItems) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (item.isCompleted) Color.Transparent else LotusCream),
                        border = BorderStroke(1.dp, if (item.isCompleted) ChandanBeige.copy(alpha = 0.4f) else ChandanBeige),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Checkbox(
                                    checked = item.isCompleted,
                                    onCheckedChange = { isChecked ->
                                        viewModel.toggleChecklistItem(item.id, isChecked)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = PeacockTeal)
                                )
                                Column {
                                    Text(
                                        text = item.itemName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (item.isCompleted) SecondaryText else PrimaryText,
                                        style = if (item.isCompleted) MaterialTheme.typography.bodyMedium.copy(
                                            fontStyle = FontStyle.Italic
                                        ) else MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${item.quantity} (${item.poojaName})",
                                        fontSize = 11.sp,
                                        color = SecondaryText
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteChecklistItem(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = DeepMaroon.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- DAILY PANCHANG & CALENDAR VIEW ---
@Composable
fun PanchangCalendarView(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean
) {
    var queryText by remember { mutableStateOf("") }
    val panchangState by viewModel.panchangState.collectAsState()

    val currentPanchangResponseDemo = """
        🔱 NAMASTE - DETAILED PANCHANG REPORT 🔱
        
        Date: May 24, 2026 (Gregorian)
        Vedic Calendar Date: Jyeshtha Shukla Paksha Ashtami tithi
        
        🪐 CURRENT PLANETARY CHRONICLES:
        - Tithi: Shukla Paksha Ashtami (Incredibly auspicious for durga worship)
        - Day/Var: Ravivar (Sunday - lorded by Lord Surya Dev)
        - Nakshatra: Purva Phalguni (Rule of comfort and divine creative expression, Lord is Shukra)
        - Yoga: Harshana (Brings joyousness, positive atmosphere)
        - Karan: Vishti (Bhadra - avoid launch of permanent new physical structures from 09:12 AM to 08:34 PM)
        
        ⏳ AUSPICIOUS INTERVALS TODAY (MUHURAT):
        - Abhijit Muhurat: 11:45 AM to 12:35 PM (Extremely beneficial for critical transactions)
        
        🚫 INAUSPICIOUS INTERVALS (AVOID MAIN RITUAL LAUNCHES):
        - Rahu Kaal: 04:30 PM to 06:00 PM
        - Yama Gandam: 12:00 PM to 01:30 PM
        
        Note: Astrological readings are for guidance and spiritual purpose only.
    """.trimIndent()

    var selectedDay by remember { mutableStateOf(8) } // Ashtami today default

    fun getDayDetail(day: Int): String {
        return when (day) {
            11 -> """
                📿 SACRED EKADASHI fasting (Apara Ekadashi)
                - Date: May 11, 2026
                - Lunar Day: Krishna Paksha Ekadashi tithi
                - Ruling Deity: Lord Sri Janardhana (Vishnu)
                - Benefits: Fasting from grains on this day removes mountain-like heavy past karmas and bestows deep mental tranquility.
            """.trimIndent()
            25 -> """
                📿 SACRED EKADASHI fasting (Nirjala Ekadashi)
                - Date: May 25, 2026
                - Lunar Day: Shukla Paksha Ekadashi tithi
                - Ruling Deity: Lord Shri Hari Vishnu
                - Benefits: Celebrated as the waterless fast of Bhima. Observer gains virtues of all 24 annual Ekadashis combined.
            """.trimIndent()
            14 -> """
                🌑 JYESHTHA AMAVASYA tithi
                - Date: May 14, 2026
                - Lunar Day: Dark Moon conjunction
                - Activities: Ideal for pitru tarpan rituals, offering black til to ancestors, and praying to Shani Dev to resolve Saturn transits.
            """.trimIndent()
            28 -> """
                🌕 JYESHTHA PURNIMA (Vat Savitri Vrat)
                - Date: May 28, 2026
                - Lunar Day: Glowing Full Moon
                - Activities: Wives fast and tie sacred threads around Banyan trees praying for husbands' longevity. Seek blessings of Satyadev.
            """.trimIndent()
            8 -> """
                🌸 SHUKLA ASHTAMI (Gaur Ashtami Today!)
                - Date: May 8, 2026 (Reflected Jyeshtha)
                - Ruling Deity: Ma Durga
                - Auspicious Muhurat: 11:45 AM - 12:35 PM
                - Rahu Kaal: 04:30 PM - 06:00 PM (Avoid launches)
            """.trimIndent()
            else -> """
                ✨ REGULAR LUNAR DWARA
                - Date: May $day, 2026
                - Lunar Day: Reflected Jyeshtha Paksha
                - Planets: Active solar current dominates the crown chakra. Maintain mindful meditation and satvik diet routines.
            """.trimIndent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dynamic Month Header Selector
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) MidnightIndigo else LotusCream),
            border = BorderStroke(1.dp, ChandanBeige),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Month selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, "Prev Month", tint = RoyalSaffron) }
                    Text(
                        text = "🌙 JYESHTHA MASA (MAY 2026)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepMaroon,
                        fontFamily = FontFamily.Serif
                    )
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, "Next Month", tint = RoyalSaffron) }
                }

                // Month Grid Days of Week
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Grid Numbers 1 to 31 inside standard Column chunks
                val totalDays = 31
                val chunkedDays = (1..totalDays).chunked(7)
                chunkedDays.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        for (i in 0 until 7) {
                            val dayNum = week.getOrNull(i)
                            if (dayNum != null) {
                                val isSelected = dayNum == selectedDay
                                val isEkadashi = dayNum == 11 || dayNum == 25
                                val isAmavasya = dayNum == 14
                                val isPurnima = dayNum == 28

                                val cellColor = when {
                                    isSelected -> RoyalSaffron
                                    isEkadashi -> RoyalSaffron.copy(alpha = 0.15f)
                                    isAmavasya -> Color.Black.copy(alpha = 0.08f)
                                    isPurnima -> AntiqueGold.copy(alpha = 0.25f)
                                    else -> Color.Transparent
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(cellColor)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) AntiqueGold else ChandanBeige.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedDay = dayNum },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayNum.toString(),
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected || isEkadashi || isPurnima) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) CreamWhite else PrimaryText
                                        )
                                        // Bullet decoration
                                        val decoration = when {
                                            isEkadashi -> "📿"
                                            isAmavasya -> "🌑"
                                            isPurnima -> "🌕"
                                            dayNum == 8 -> "📍"
                                            else -> ""
                                        }
                                        if (decoration.isNotEmpty()) {
                                            Text(decoration, fontSize = 9.sp)
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Divider(color = ChandanBeige.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))

                // Detail of tapped date
                Text(
                    text = "ASTRAL HIGHLIGHT FOR DAY $selectedDay:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DeepMaroon
                )
                Text(
                    text = getDayDetail(selectedDay),
                    fontSize = 12.sp,
                    color = PrimaryText,
                    lineHeight = 17.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RoyalSaffron.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                )
            }
        }

        // Search Ask Portal
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) MidnightIndigo else LotusCream),
            border = BorderStroke(1.dp, ChandanBeige),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Vedic Panchang Query Portal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepMaroon,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Convert dates, compute upcoming vrats, or query detailed planetary alignments for specific days.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )

                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("e.g. Diwali 2026 Nakshatra, Today's Rahu Kaal timings") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedIndicatorColor = RoyalSaffron)
                )

                Button(
                    onClick = {
                        if (queryText.trim().isNotEmpty()) {
                            viewModel.generatePanchangInfo(queryText)
                        } else {
                            viewModel.generatePanchangInfo("Provide today's Jyeshtha Month May 2026 astrological daily Panchang report details")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Calculate, contentDescription = "Calculate")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Request Vedic Alignment Report", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Output Report Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .border(1.5.dp, AntiqueGold, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = LotusCream)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                when (panchangState) {
                    is LlmState.Idle -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "✨ CURRENT SACRED TRANSITS (Report Container):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = DeepMaroon
                            )
                            Text(
                                text = "Submit custom query above to fetch current stellar alignments or ask our AI JyotishGuru about tithi details dynamically.",
                                fontSize = 13.sp,
                                color = PrimaryText,
                                lineHeight = 20.sp
                            )
                        }
                    }
                    is LlmState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = RoyalSaffron)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Computing Astral Conjunctions...",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = DeepMaroon
                            )
                        }
                    }
                    is LlmState.Success -> {
                        val text = (panchangState as LlmState.Success).text
                        Column(modifier = Modifier.fillMaxWidth()) {
                            VedicMarkdownText(
                                text = text,
                                isDarkTheme = false
                            )
                        }
                    }
                    is LlmState.Error -> {
                        val msg = (panchangState as LlmState.Error).message
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline, 
                                contentDescription = "Error", 
                                tint = DeepMaroon,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = msg,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- HASTA REKHA SHASTRA (PALMISTRY VIEW) ---
@Composable
fun PalmistryView(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean,
    readings: List<SavedReading>
) {
    val currentLang by viewModel.appLanguage.collectAsState()
    var activePalmTab by remember { mutableStateOf("learn") } // "learn" | "read" | "archives"
    val palmState by viewModel.palmistryState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabRow(
            selectedTabIndex = if (activePalmTab == "learn") 0 else if (activePalmTab == "read") 1 else 2,
            containerColor = Color.Transparent,
            contentColor = DeepMaroon,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[if (activePalmTab == "learn") 0 else if (activePalmTab == "read") 1 else 2]),
                    color = RoyalSaffron
                )
            }
        ) {
            Tab(
                selected = activePalmTab == "learn",
                onClick = { activePalmTab = "learn" },
                text = { Text(tr("subtab_hasta_diagram", currentLang), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activePalmTab == "read",
                onClick = { activePalmTab = "read" },
                text = { Text(tr("subtab_request_reading", currentLang), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = activePalmTab == "archives",
                onClick = { activePalmTab = "archives" },
                text = { Text(tr("subtab_historic_scrolls", currentLang), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
            )
        }

        when (activePalmTab) {
            "learn" -> {
                PalmistryLearnView(isDarkTheme = isDarkTheme)
            }
            "read" -> {
                PalmistryReadView(viewModel = viewModel, palmState = palmState, isDarkTheme = isDarkTheme)
            }
            "archives" -> {
                PalmistryArchivesView(readings = readings.filter { it.category == "Palmistry" }, onDelete = { id -> viewModel.deleteSavedReading(id) })
            }
        }
    }
}

@Composable
fun PalmistryLearnView(isDarkTheme: Boolean) {
    var selectedLineInfo by remember { mutableStateOf("Tap on any sacred coordinate line or mount descriptor bullet listed below to discover its influence over your destiny in Vedic Hast Samudrika Shastra.") }

    val lineDataList = listOf(
        Pair("❤️ Heart Line (Hridaya Rekha)", "Sits high on the palm. Signifies emotional depth, marriages, friendships, cardiac health stability, and innate devotion/bhakti capabilities."),
        Pair("🧠 Head Line (Mastiq Rekha)", "Slices horizontally across central palm. Deciphers logical thinking capacity, concentration focus, raw memory storage, and mental willpower."),
        Pair("🌱 Life Line (Aayush Rekha)", "Curves gracefully around the thumb mount. Dictates bodily vitality, energy currents, physical lifespan potential, and overall spiritual endurance."),
        Pair("💼 Fate Line (Bhagya Rekha)", "Ascends vertically towards middle Saturn finger. Unfolds structural career shifts, karmic obstructions, financial elevations, and external fate factors."),
        Pair("☀️ Sun Line (Surya Rekha)", "Climbs towards the ring finger. Reflects public fame, artistic creativity, divine fortune blessings, and leadership honors."),
        Pair("🌟 Mount of Venus (Shukra)", "Puffy base of the thumb. Governs love compatibility, luxurious tastes, fertility, aesthetic arts capability, and worldly comforts."),
        Pair("🪐 Mount of Jupiter (Guru)", "Mount below the index finger. Exhibits teaching authority, divine wisdom, magnanimous status, and religious leadership honors.")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hands Line Visual representation
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepMaroon),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, AntiqueGold, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✋ VEDIC HAND DIAGRAM",
                    color = AntiqueGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Custom palm representation vector using basic Canvas lines
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(CreamWhite.copy(alpha = 0.08f), CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerOffset = this.center

                        // Draw main Palm contour border
                        drawCircle(
                            color = AntiqueGold.copy(alpha = 0.4f),
                            radius = size.minDimension / 2.2f,
                            style = Stroke(width = 1.dp.toPx())
                        )

                        // Draw Life Line (Curve)
                        drawCircle(
                            color = RoyalSaffron,
                            radius = size.minDimension / 3.5f,
                            center = androidx.compose.ui.geometry.Offset(centerOffset.x - 30.dp.toPx(), centerOffset.y + 10.dp.toPx()),
                            style = Stroke(width = 1.8.dp.toPx())
                        )

                        // Draw Head Line (Horizontal slant)
                        drawLine(
                            color = Color.Green.copy(alpha = 0.4f),
                            start = androidx.compose.ui.geometry.Offset(centerOffset.x - 50.dp.toPx(), centerOffset.y),
                            end = androidx.compose.ui.geometry.Offset(centerOffset.x + 40.dp.toPx(), centerOffset.y + 15.dp.toPx()),
                            strokeWidth = 1.8.dp.toPx()
                        )

                        // Draw Heart Line (Upper curved horizontal)
                        drawLine(
                            color = Color.Cyan.copy(alpha = 0.4f),
                            start = androidx.compose.ui.geometry.Offset(centerOffset.x - 45.dp.toPx(), centerOffset.y - 30.dp.toPx()),
                            end = androidx.compose.ui.geometry.Offset(centerOffset.x + 45.dp.toPx(), centerOffset.y - 15.dp.toPx()),
                            strokeWidth = 1.8.dp.toPx()
                        )

                        // Draw Fate Line (Vertical)
                        drawLine(
                            color = AntiqueGold,
                            start = androidx.compose.ui.geometry.Offset(centerOffset.x, centerOffset.y + 50.dp.toPx()),
                            end = androidx.compose.ui.geometry.Offset(centerOffset.x, centerOffset.y - 45.dp.toPx()),
                            strokeWidth = 1.8.dp.toPx()
                        )
                    }

                    Text("ॐ", color = AntiqueGold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Aparanta Hasta Map (Click line list below)",
                    color = CreamWhite,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        // Selected line information display
        Card(
            colors = CardDefaults.cardColors(containerColor = LotusCream),
            border = BorderStroke(1.dp, ChandanBeige),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📖 SHASTRA KNOWLEDGE EXPLAINER",
                    fontWeight = FontWeight.Bold,
                    color = DeepMaroon,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedLineInfo,
                    fontSize = 13.sp,
                    color = PrimaryText,
                    lineHeight = 18.sp
                )
            }
        }

        // Tap list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            lineDataList.forEach { (lineTitle, desc) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedLineInfo = desc },
                    border = BorderStroke(1.dp, ChandanBeige.copy(alpha = 0.6f)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = lineTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DeepMaroon
                        )
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = "View", tint = AntiqueGold)
                    }
                }
            }
        }
    }
}

@Composable
fun PalmistryReadView(
    viewModel: JyotishViewModel,
    palmState: LlmState,
    isDarkTheme: Boolean
) {
    var handSide by remember { mutableStateOf("Right") } // "Left" | "Right"
    var focusArea by remember { mutableStateOf("General Destiny") } // "General Destiny"
    var queryDescription by remember { mutableStateOf("") }

    val questionSuggestions = listOf(
        "Deep headline fork with ascending fate line.",
        "Lifeline has small auspicious upwards arrows.",
        "Mount of Venus is highly elevated with stars.",
        "Heartline reaches between Index and Middle finger."
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDarkTheme) MidnightIndigo else LotusCream),
            border = BorderStroke(1.dp, ChandanBeige),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Request Personalized Samudrika Reading",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepMaroon,
                    fontFamily = FontFamily.Serif
                )

                // Hand choice
                Text("Step 1: Choose Active Hand", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SecondaryText)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { handSide = "Left" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (handSide == "Left") RoyalSaffron else ChandanBeige.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Left (Potential)", color = if (handSide == "Left") CreamWhite else PrimaryText, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { handSide = "Right" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (handSide == "Right") RoyalSaffron else ChandanBeige.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Right (Actual)", color = if (handSide == "Right") CreamWhite else PrimaryText, fontSize = 11.sp)
                    }
                }

                // Focus area
                Text("Step 2: Choose Focus Domain", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SecondaryText)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val list = listOf("Destiny", "Marriage", "Career", "Health")
                    list.forEach { area ->
                        Button(
                            onClick = { focusArea = area },
                            colors = ButtonDefaults.buttonColors(containerColor = if (focusArea == area) DeepMaroon else ChandanBeige.copy(alpha = 0.15f)),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 2.dp)
                        ) {
                            Text(area, fontSize = 11.sp, color = if (focusArea == area) CreamWhite else PrimaryText)
                        }
                    }
                }

                // Description
                Text("Step 3: Describe your lines or mounts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SecondaryText)
                OutlinedTextField(
                    value = queryDescription,
                    onValueChange = { queryDescription = it },
                    placeholder = { Text("Write about your mount puffiness, line forks, stars, lengths, or breaks...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = TextFieldDefaults.colors(focusedIndicatorColor = RoyalSaffron)
                )

                // Helpers
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(questionSuggestions) { suggestion ->
                        SuggestionChip(
                            onClick = { queryDescription = suggestion },
                            label = { Text(suggestion, fontSize = 10.sp, maxLines = 1) }
                        )
                    }
                }

                Button(
                    onClick = {
                        val finalPrompt = "Palmistry Consultation: Side=$handSide. Area=$focusArea. Details=$queryDescription"
                        viewModel.generatePalmistryReading(finalPrompt)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AntiqueGold, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Recommend, contentDescription = "Query")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Deconstruct My Palm Lines", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Result Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .border(2.dp, AntiqueGold, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = LotusCream)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                when (palmState) {
                    is LlmState.Idle -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔱", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Sacred Reading Chamber",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepMaroon
                            )
                            Text(
                                "Describe your lines with the chips above. Our AI JyotishGuru translates Sanskrit Samudrika texts to output elegant personal feedback scroll.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                    is LlmState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = RoyalSaffron)
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                "Analyzing Astral Lines...",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = DeepMaroon
                            )
                        }
                    }
                    is LlmState.Success -> {
                        val successText = (palmState as LlmState.Success).text
                        Column(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {
                                    viewModel.saveCurrentReading(
                                        category = "Palmistry",
                                        title = "$focusArea Reading ($handSide Hand)",
                                        query = queryDescription,
                                        result = successText
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepMaroon),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.BookmarkAdded, contentDescription = "Save", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Archive to Sacred Scrolls History", fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val scroll = rememberScrollState()
                            VedicMarkdownText(
                                text = successText,
                                isDarkTheme = false,
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(scroll)
                            )
                        }
                    }
                    is LlmState.Error -> {
                        val errMsg = (palmState as LlmState.Error).message
                        Text(
                            text = errMsg,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PalmistryArchivesView(
    readings: List<SavedReading>,
    onDelete: (Int) -> Unit
) {
    if (readings.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📜", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "No Historic Reading Scrolls Archived.",
                    color = SecondaryText,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Click on 'Request Reading', enter your hand characteristics and hit archive once generated successfully!",
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(readings) { read ->
                var expanded by remember { mutableStateOf(false) }
                Card(
                    colors = CardDefaults.cardColors(containerColor = LotusCream),
                    border = BorderStroke(1.dp, ChandanBeige),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = read.title,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepMaroon,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Date: May 24, 2026",
                                    fontSize = 10.sp,
                                    color = SecondaryText
                                )
                            }

                            Row {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand",
                                        tint = RoyalSaffron
                                    )
                                }
                                IconButton(onClick = { onDelete(read.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                                }
                            }
                        }

                        if (expanded) {
                            Divider(color = ChandanBeige.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))
                            Text(
                                text = "My Described Hand Features:\n${read.queryText}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = read.resultText,
                                fontSize = 13.sp,
                                color = PrimaryText,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- SANCTUM ASTRO-CHAT CONSULT VIEW (ASK AI SCREEN) ---
@Composable
fun AstroChatView(
    viewModel: JyotishViewModel,
    isDarkTheme: Boolean
) {
    var textInput by remember { mutableStateOf("") }
    val chatState by viewModel.chatState.collectAsState()
    val currentLang by viewModel.appLanguage.collectAsState()

    // Resource mapping for multi-language greetings and quick prompts
    val initialMessageText = when (currentLang) {
        "hi" -> "🙏 जय श्री राम! ज्योतिषगुरु के चैंबर में आपका स्वागत है। मैं हिंदू ज्योतिष, हस्तरेखा, पंचांग तिथि और पूजा विधियों का विशेषज्ञ हूँ। कृपया अपना प्रश्न पूछें।"
        "te" -> "🙏 జై శ్రీరామ్! జ్యోతిష్ గురు పవిత్ర సంభాషణ మందిరానికి స్వాగతం. జ్యోతిష్యం, హస్తసాముద్రికం, పంచాంగం మరియు పూజా విధానాల గురించి అడగండి."
        "ta" -> "🙏 ஜெய் ஸ்ரீ ராம்! ஜோதிட குருவின் புனித அறைக்கு உங்களை வரவேற்கிறோம். ஜோதிடம், கைரேகை, பஞ்சாங்கம் மற்றும் பூஜா முறைகள் குறித்து கேளுங்கள்."
        "kn" -> "🙏 ಜೈ ಶ್ರೀ ರಾಮ್! ಜ್ಯೋತಿಷ್ ಗುರು ಪವಿತ್ರ ಕೋಣೆಗೆ ತಮಗೆ ಸುಸ್ವಾಗತ. ಜ್ಯೋತಿಷ್ಯ, ಹಸ್ತ ಸಾಮುದ್ರಿಕ, ಪಂಚಾಂಗ ಮತ್ತು ಪೂಜಾ ವಿಧಾನಗಳ ಬಗ್ಗೆ ಪ್ರಶ್ನಿಸಿ."
        "mr" -> "🙏 जय श्री राम! ज्योतिषगुरुच्या या दालनात आपले स्वागत आहे. मी हिंदू ज्योतिष, हस्तरेषाशास्त्र, पंचांग आणि पूजा विधींचा तज्ज्ञ आहे. कृपया प्रश्न विचारा."
        "bn" -> "🙏 জয় শ্রী রাম! জ্যোতিষগুরুর পবিত্র পরামর্শ কক্ষে আপনাকে স্বাগত। আমি হিন্দু জ্যোতিষ, হস্তরেখা, পঞ্জিকা ও পুজো পদ্ধতির বিশেষজ্ঞ। আপনার প্রশ্ন জিজ্ঞাসা করুন।"
        "gu" -> "🙏 જય શ્રી રામ! જ્યોતિષ ગુરુના પવિત્ર સંવાદ કક્ષમાં આપનું સ્વાગત છે. હું હિન્દુ જ્યોતિષ, હસ્તરેખા, પંચાંગ અને પૂજા પદ્ધતિઓનો નિષ્ણાત છું. પૂછો."
        else -> "🙏 Jai Shri Ram! Welcome to the sacred consultation chamber of JyotishGuru. I am an expert in Hindu astrology, Hasta palmistry, Panchang tithis, and festival temple protocols. Ask me your questions respectfully."
    }

    val recommendationChips = when (currentLang) {
        "hi" -> listOf("दैनिक पंचांग दिखाएं", "गणेश चतुर्थी पूजा विधि?", "शुक्र पर्वत का महत्व?", "मंगलवार व्रत के नियम?")
        "te" -> listOf("నేటి పంచాంగం చూపించు", "వिनాయక చవితి పూజ విధానం?", "शुक्र పర్వతం ప్రాముఖ్యత?", "మంగళవారం పూజ నియమాలు?")
        "ta" -> listOf("இன்றைய பஞ்சாங்கம்", "விநாயகர் சதுர்த்தி பூஜை முறை?", "சுக்கிர மேடு பலன்கள்?", "செவ்வாய் விரத முறைகள்?")
        "kn" -> listOf("ಇಂದಿನ ಪಂಚಾಂಗ", "ಗಣೇಶ ಚತುರ್ಥಿ ಪೂಜೆ ವಿಧಾನ?", "ಶುಕ್ರ ಪರ್ವತದ ಮಹತ್ವ?", "ಮಂಗಳವಾರ ವ್ರತ ನಿಯಮಗಳು?")
        "mr" -> listOf("आजचे पंचांग दाखवा", "गणेश चतुर्थी पूजा विधी?", "शुक्र पर्वताचे महत्त्व?", "मंगळवार व्रत नियम?")
        "bn" -> listOf("আজকের পঞ্জিকা দেখান", "গণেশ চতুর্থী পুজো পদ্ধতি?", "শুক্র পর্বতের গুরুত্ব?", "মঙ্গলবার ব্রতের নিয়ম?")
        "gu" -> listOf("આજનું પંચાંગ બતાવો", "ગણેશ ચતુર્થી પૂજા વિધિ?", "શુક્ર પર્વતનું મહત્વ?", "મંગળવાર વ્રતના નિયમો?")
        else -> listOf("Show daily panchang", "Ganesh Chaturthi bhog samagri?", "Explain Mount of Venus", "Tuesday fasting rules?")
    }

    // Simple visual local chat log
    val chatLog = remember { mutableStateListOf<Pair<String, String>>() }

    // Synchronize initial greeting text when selected language changes
    LaunchedEffect(currentLang) {
        chatLog.clear()
        chatLog.add(Pair("namaste", initialMessageText))
    }

    // Handle incoming success state from ViewModel & append to log
    LaunchedEffect(chatState) {
        if (chatState is LlmState.Success) {
            val responseText = (chatState as LlmState.Success).text
            // Avoid duplicate additions
            if (chatLog.lastOrNull()?.first == "user" && chatLog.none { it.second == responseText }) {
                chatLog.add(Pair("guru", responseText))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Conversation Logs Display
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatLog) { msg ->
                val isGuru = msg.first == "guru" || msg.first == "namaste"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isGuru) Arrangement.Start else Arrangement.End
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isGuru) LotusCream else RoyalSaffron),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isGuru) 2.dp else 16.dp,
                            bottomEnd = if (isGuru) 16.dp else 2.dp
                        ),
                        border = BorderStroke(1.dp, if (isGuru) AntiqueGold else Color.Transparent),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isGuru) tr("msg_sender_guru", currentLang) else tr("msg_sender_sadhaka", currentLang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isGuru) DeepMaroon else CreamWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (isGuru) {
                                VedicMarkdownText(
                                    text = msg.second,
                                    isDarkTheme = false
                                )
                            } else {
                                Text(
                                    text = msg.second,
                                    fontSize = 13.sp,
                                    color = CreamWhite,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            if (chatState is LlmState.Loading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = RoyalSaffron, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            tr("guru_contemplating", currentLang),
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                            color = DeepMaroon
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Prompt helper chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(recommendationChips) { sug ->
                SuggestionChip(
                    onClick = {
                        textInput = sug
                        chatLog.add(Pair("user", sug))
                        viewModel.generateChatResponse(sug)
                        textInput = ""
                    },
                    label = { Text(sug, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Text input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(tr("placeholder_chat_input", currentLang)) },
                colors = TextFieldDefaults.colors(focusedIndicatorColor = RoyalSaffron),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (textInput.trim().isNotEmpty()) {
                        val prompt = textInput.trim()
                        chatLog.add(Pair("user", prompt))
                        viewModel.generateChatResponse(prompt)
                        textInput = ""
                    }
                })
            )
            IconButton(
                onClick = {
                    if (textInput.trim().isNotEmpty()) {
                        val prompt = textInput.trim()
                        chatLog.add(Pair("user", prompt))
                        viewModel.generateChatResponse(prompt)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .background(DeepMaroon, CircleShape)
                    .size(45.dp)
                ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = CreamWhite)
            }
        }
    }
}
