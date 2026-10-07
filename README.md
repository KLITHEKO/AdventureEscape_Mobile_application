# Adventure Escape SA – Android Application

> Student project: mobile application component
> **Module:** _[module name and code]_ · **Institution:** _[institution]_ · **Lecturer:** _[lecturer name]_
> **Group members:** _[Name Surname – student number]_, _[Name Surname – student number]_, _[Name Surname – student number]_
> **Submission date:** _[date]_

---

## 1. Project overview

Adventure Escape SA is a small-to-medium enterprise (SME) founded by **Liam Daniels** in **2024**. It offers professionally guided outdoor experiences throughout the **Western Cape**. According to the client brief, families, tourists, schools and corporate groups struggle to find one place where they can compare and book outdoor activities.

This Android application meets that need. It lets customers:

- **browse** the company's Adventure Packages and Individual Activities,
- **view the details** of each experience (purpose, what is included and price),
- **build a quotation** for one or more bookings, with the group discount calculated automatically, and
- **send a booking request** to the company through a contact form.

The app was developed from the low-fidelity wireframes produced in Phase 1 and implements the six required screens:

| # | Screen | Purpose |
|---|---|---|
| 1 | **Home** | Branding, hero banner, "Browse Adventures" call to action and featured Adventure Packages |
| 2 | **About Us** | The founder's story, when the company was established, and its values (teamwork, fitness, love of nature) |
| 3 | **Overview** | Tabbed list of all Adventure Packages and Individual Activities |
| 4 | **Individual page** | Details of one experience: photo, price, purpose, "What's Included" list and related suggestions |
| 5 | **Calculate Fee** | The customer's bookings, number of people per booking, discount tier progress and quotation total |
| 6 | **Contact Us** | Validated booking request form, contact details and operating area |

---

## 2. Client brief requirements and how the app meets them

### 2.1 Catalogue

| Type | Experience | Fee | Includes (from brief) |
|---|---|---|---|
| Package | Ultimate Adventure Day | R1 500 | Guided hiking trail, zip lining, kayaking, lunch, safety briefing and equipment |
| Package | Family Explorer Package | R1 500 | Nature walk, obstacle course, picnic area, family games, guided wildlife spotting |
| Package | Mountain Adventure Package | R1 500 | Mountain hiking, scenic viewpoints, rock scrambling, safety equipment, professional guide |
| Package | Corporate Team Challenge | R1 500 | Team obstacle course, orienteering challenge, raft building activity, leadership exercises, team awards |
| Activity | Zip lining Adventure | R750 | Safety briefing, equipment hire, professional instructors |
| Activity | Kayaking Experience | R750 | Kayak and paddle, safety equipment, guided route |
| Activity | Rock Climbing Session | R750 | Climbing equipment, safety instructions, professional guide |

All catalogue data is stored in one place, `model/BookingManager.kt`. Changing a price or an "Includes" item there updates every screen.

### 2.2 Discount rules

| Number of bookings | Discount |
|---|---|
| 1 | None |
| 2 | 5% |
| 3 | 10% |
| More than 3 | 15% |

**Design decision:** a *booking* is counted as each **different** package or activity in the quotation. The − / + stepper on each booking sets the **number of people** for it, which multiplies the price but does not count as extra bookings.

*Example:* Ultimate Adventure Day for 2 people (R3 000) plus Kayaking Experience for 1 person (R750) = **2 bookings**, which gives 5% off R3 750, so the total is **R3 562.50**.

This logic is in `BookingManager.discountPercentFor()` and is covered by the unit tests (see section 7).

---

## 3. Design

### 3.1 User-centred design

The app follows a user-centred design (UCD) approach. The needs, tasks and context of the intended users drive design decisions throughout development (ISO, 2019; Norman, 2013). In practice this meant:

- **Clear navigation:** a persistent bottom navigation bar gives one-tap access to the four main destinations: Home, Overview, Calculate and Contact. Material Design recommends a navigation bar for three to five top-level destinations on compact screens (Google, n.d.-a).
- **Immediate feedback:** adding a booking shows a confirmation message with a shortcut to the quotation, and the quotation total and discount tier update as soon as the number of people changes (Norman, 2013).
- **Error prevention:** the contact form checks for empty fields and invalid email addresses before a request is "sent".
- **Accessibility:** every meaningful image has a content description for screen readers, and all text is stored in string resources (Google, n.d.-b).

### 3.2 Colour palette

| Colour | Hex | Use in the app |
|---|---|---|
| Forest Green | `#19472A` | Primary colour: buttons, selected navigation item, headings, tick icons |
| Sunset Orange | `#D6810B` | Secondary colour: prices and highlight icons |
| Coral | `#FF7F6B` | Accent colour: badges and the remove (bin) icon |
| White | `#FFFFFF` | Backgrounds, cards, text on green |
| Charcoal | `#333333` | Main body text |

Light tints of these colours are used behind badges. All colours are defined in `res/values/colors.xml` and applied through the Material 3 theme in `res/values/themes.xml`.

### 3.3 Logo

The logo shows a diamond with a mountain range, forest and a rising sun, above the words *ADVENTURE ESCAPE SA*. It is stored as `res/drawable-nodpi/img_adventure_logo.png` with a transparent background. The phone's launcher icon was generated from the same artwork with Android Studio's Image Asset Studio as an adaptive icon on a white background (Google, n.d.-c).

---

## 4. Technical infrastructure

| Item | Version or detail |
|---|---|
| IDE | Android Studio |
| Language | Kotlin |
| Build system | Gradle 9.7.1 with Android Gradle Plugin 9.4.1 (Kotlin DSL, version catalogue in `gradle/libs.versions.toml`) |
| SDK levels | `minSdk` 36 · `targetSdk` 37 · `compileSdk` 37 |
| UI toolkit | XML layouts with **View Binding** (Google, n.d.-d) |
| Libraries | AndroidX Core KTX 1.10.1, AppCompat 1.6.1, Activity KTX 1.8.0, ConstraintLayout 2.1.4, Material Components 1.10.0 |
| Testing | JUnit 4.13.2 (local unit tests); AndroidX Test / Espresso (instrumented tests) |
| Version control | _[e.g. GitHub repository link]_ |

### 4.1 Architecture

The app uses a **single-activity architecture**. `MainActivity` hosts a `FragmentContainerView` and the bottom navigation bar, and each screen is a **Fragment** (Google, n.d.-e). The About Us and Individual screens are placed on the fragment back stack, so the system Back button returns the user to the previous screen.

```
MainActivity  (bottom navigation + fragment container)
 ├── HomeFragment              Screen 1
 ├── AboutUsFragment           Screen 2  (opened from Home)
 ├── OverviewFragment          Screen 3
 ├── IndividualDetailFragment  Screen 4  (opened from any package/activity card)
 ├── CalculateFeeFragment      Screen 5
 └── ContactUsFragment         Screen 6
```

Other technical choices:

- **Lists:** `RecyclerView` shows the package cards, the overview list and the quotation list (Google, n.d.-f). The Overview and Calculate lists use `ListAdapter` with `DiffUtil`, so only the rows that change are redrawn (Google, n.d.-g).
- **Data model:** `AdventureItem` and `BookingCartItem` are Kotlin **data classes**, which provide equality checks and `copy()` automatically (JetBrains, n.d.). The quotation state is kept in memory in the `BookingManager` object.
- **Layouts:** `ConstraintLayout` is used so screens adapt to different phone sizes (Google, n.d.-h).
- **Text:** all visible text is stored in `res/values/strings.xml`, and counts such as "1 booking / 2 bookings" use quantity strings (plurals) (Google, n.d.-b).
- **Edge-to-edge:** apps targeting Android 15 (API 35) or higher are shown edge-to-edge by default, so `MainActivity` applies window insets to stop content from being hidden behind the status bar (Google, n.d.-i).
- **Images:** large photos are stored in `drawable-nodpi` so Android does not rescale them for each screen density (Google, n.d.-j).

---

## 5. Project structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/adventureescapesa/
│   ├── MainActivity.kt                 Host activity, bottom navigation, screen switching
│   ├── adapter/
│   │   ├── CalculateFeeAdapter.kt      Quotation list (people stepper, remove)
│   │   ├── FeaturedPackageAdapter.kt   Horizontal package cards (Home, "You May Also Like")
│   │   └── OverviewAdapter.kt          Overview list cards
│   ├── model/
│   │   ├── AdventureModels.kt          AdventureItem and BookingCartItem data classes
│   │   └── BookingManager.kt           Catalogue, quotation state, discount calculation
│   └── ui/
│       ├── HomeFragment.kt             Screen 1
│       ├── AboutUsFragment.kt          Screen 2
│       ├── OverviewFragment.kt         Screen 3
│       ├── IndividualDetailFragment.kt Screen 4
│       ├── CalculateFeeFragment.kt     Screen 5
│       └── ContactUsFragment.kt        Screen 6
└── res/
    ├── layout/          One XML layout per screen and per list item
    ├── values/          colors.xml, strings.xml, themes.xml
    ├── drawable/        Vector icons, placeholder illustrations, badge shapes
    ├── drawable-nodpi/  Logo and hero photo
    ├── menu/            Bottom navigation menu
    └── mipmap-*/        Launcher icon
```

### 5.1 Where to change images

| What | File | How |
|---|---|---|
| Package and activity photos | `model/BookingManager.kt` | Change `imageResId = R.drawable.…` for each item |
| Home logo and hero banner | `res/layout/fragment_home.xml` | `android:src` on `img_app_logo` / `img_hero` |
| About Us team photo and icons | `res/layout/fragment_about_us.xml` | `android:src` on `img_team` and the three pillar icons |
| Contact Us map | `res/layout/fragment_contact_us.xml` | `android:src` on `img_operating_map` |
| App icon | Android Studio | **res → New → Image Asset**, background colour `#FFFFFF` |

Image files must be named with lowercase letters, numbers and underscores only (e.g. `img_kayaking.jpg`).

---

## 6. How to run the app

1. Install the latest stable version of **Android Studio**.
2. Open the `AdventureEscapeSA` folder with **File → Open**.
3. Wait for the Gradle sync to finish, and accept any SDK downloads Android Studio suggests (API 37).
4. Create or start an emulator running **Android 16 (API 36) or higher** in **Device Manager**, or connect a physical device with USB debugging enabled.
5. Press **Run ▶**.

If the build fails after files have been moved or renamed, use **Build → Clean Project** and then **Build → Rebuild Project**.

---

## 7. Testing

### 7.1 Automated unit tests

Local unit tests run on the computer's JVM without an emulator (Google, n.d.-k). `app/src/test/.../AdventureEscapeUnitTest.kt` checks that:

- the catalogue contains **4 packages at R1 500** and **3 activities at R750**,
- the discount tiers match the brief (0 / 0 / 5 / 10 / 15%), and
- a sample quotation's subtotal, discount and total are calculated correctly.

**To run them:** right-click the `test` folder → **Run 'Tests in …'**, or run `./gradlew test` in the terminal.

### 7.2 Manual test checklist

| # | Test | Expected result | Pass? |
|---|---|---|---|
| 1 | Tap each bottom navigation item | The correct screen opens and the tab is highlighted | |
| 2 | Home → About Us, then press Back | Returns to Home | |
| 3 | Overview: switch between the two tabs | The list shows 4 packages or 3 activities | |
| 4 | Open a package and tap **Add to Booking** | Confirmation appears; **View Quote** opens Calculate | |
| 5 | Add 2 different bookings | Discount shows **5%** | |
| 6 | Add a 3rd and a 4th booking | Discount shows **10%**, then **15%** | |
| 7 | Press + / − on a booking | People count, line total and grand total update | |
| 8 | Press − at 1 person, or press the bin icon | The booking is removed | |
| 9 | Tap **Proceed to Booking** with an empty quote | "Quotation Is Empty" dialog appears | |
| 10 | Submit the contact form empty or with a bad email | Error messages appear under the fields | |
| 11 | Submit the contact form correctly | Confirmation dialog appears and the fields clear | |

### 7.3 Code quality

Android Studio's lint inspection (**Code → Inspect Code**) was used to find and remove warnings such as hardcoded text, unused resources and inefficient list updates (Google, n.d.-l).

---

## 8. Known limitations and future work

- The quotation is stored **in memory only** and is cleared when the app is closed. A future version could save it with a local database such as Room.
- The contact form **does not send a real email or store the request**. It shows a confirmation only. A future version could connect to an email service or back-end API.
- Some images are **placeholder illustrations** until final photographs are supplied (see section 5.1).
- Contact details (phone number and email address) are **fictional** and used for demonstration.

---

## 9. Change log

_No entries yet._

---

## 10. Declaration of AI use

Generative AI (Claude, Anthropic, 2026) was used during development to help debug build errors, correct package and import structure, remove lint warnings, align the app's data and discount logic with the client brief. All AI output was reviewed, tested and adjusted by the group, which takes full responsibility for the submitted work. 

---

## 11. References

Anthropic. (2026) *Claude* [Large language model]. Available at: https://claude.ai (Accessed: 30 September 2026).

Google. (n.d.-a) *Navigation bar – Material Design 3*. Available at: https://m3.material.io/components/navigation-bar/guidelines (Accessed: 30 September 2026).

Google. (n.d.-b) *String resources*. Android Developers. Available at: https://developer.android.com/guide/topics/resources/string-resource (Accessed: 30 September 2026).

Google. (n.d.-c) *Create app icons*. Android Developers. Available at: https://developer.android.com/studio/write/create-app-icons (Accessed: 30 September 2026).

Google. (n.d.-d) *View binding*. Android Developers. Available at: https://developer.android.com/topic/libraries/view-binding (Accessed: 30 September 2026).

Google. (n.d.-e) *Fragments*. Android Developers. Available at: https://developer.android.com/guide/fragments (Accessed: 30 September 2026).

Google. (n.d.-f) *Create dynamic lists with RecyclerView*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/recyclerview (Accessed: 30 September 2026).

Google. (n.d.-g) *ListAdapter*. Android Developers. Available at: https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter (Accessed: 30 September 2026).

Google. (n.d.-h) *Build a responsive UI with ConstraintLayout*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/constraint-layout (Accessed: 30 September 2026).

Google. (n.d.-i) *Display content edge-to-edge in views*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/edge-to-edge (Accessed: 30 September 2026).

Google. (n.d.-j) *Support different pixel densities*. Android Developers. Available at: https://developer.android.com/training/multiscreen/screendensities (Accessed: 30 September 2026).

Google. (n.d.-k) *Build local unit tests*. Android Developers. Available at: https://developer.android.com/training/testing/unit-testing/local-unit-tests (Accessed: 30 September 2026).

Google. (n.d.-l) *Improve your code with lint checks*. Android Developers. Available at: https://developer.android.com/studio/write/lint (Accessed: 30 September 2026).

ISO. (2019) *ISO 9241-210:2019 Ergonomics of human-system interaction – Part 210: Human-centred design for interactive systems*. Geneva: International Organization for Standardization.

JetBrains. (n.d.) *Data classes*. Kotlin Documentation. Available at: https://kotlinlang.org/docs/data-classes.html (Accessed: 30 September 2026).

Norman, D. (2013) *The Design of Everyday Things*. Revised and expanded edn. New York: Basic Books.
