# Adventure Escape SA – Android Application

**Module:** XHAW5112 - Work intergrated learning
**POE Submission:** Task 2

## Group Members

1. Kabelo Litheko – ST10517750
2. Bongiwe Motona - ST10520889
3. Thando Shongwe – ST10539919
4. Thandazile Xaba – ST10515020

---

## 1. Project Overview

Adventure Escape SA is a small-to-medium enterprise (SME) founded by Liam Daniels in 2024. The company offers guided outdoor activities in the Western Cape.

The Android application allows customers to:

* Browse adventure packages and individual activities.
* View activity descriptions, prices and included features.
* Add activities to a quotation and calculate discounts.
* View the quotation total, including applicable discounts and conservation levies.
* Complete a contact form to submit a booking enquiry.

The application was developed using the low-fidelity wireframes created in Phase 1. It contains six main screens.

| Screen              | Purpose                                                                             |
| ------------------- | ----------------------------------------------------------------------------------- |
| 1. Home             | Displays the company branding, featured packages and a button to browse adventures. |
| 2. About Us         | Introduces the company, its founder and its mission.                                |
| 3. Overview         | Displays adventure packages and individual activities.                              |
| 4. Activity Details | Shows an activity's photo, description, price and included features.                |
| 5. Calculate Fees   | Displays selected bookings, quantities, discounts and the quotation total.          |
| 6. Contact Us       | Displays contact information and a form for booking enquiries.                      |

## 2. Client Requirements

### 2.1 Adventure Packages and Activities

The application includes the following packages and activities from the client brief.

| Type     | Name                       |  Price | What's Included                                                                                 |
| -------- | -------------------------- | -----: | ----------------------------------------------------------------------------------------------- |
| Package  | Ultimate Adventure Day     | R1 500 | Guided hiking trail, zip lining, kayaking, lunch, safety briefing and equipment.                |
| Package  | Family Explorer Package    | R1 500 | Nature walk, obstacle course, picnic area, family games and guided wildlife spotting.           |
| Package  | Mountain Adventure Package | R1 500 | Mountain hiking, scenic viewpoints, rock scrambling, safety equipment and a professional guide. |
| Package  | Corporate Team Challenge   | R1 500 | Obstacle course, orienteering, raft building, leadership exercises and team awards.             |
| Activity | Zip Lining Adventure       |   R750 | Safety briefing, equipment hire and professional instructors.                                   |
| Activity | Kayaking Experience        |   R750 | Kayak, paddle, safety equipment and a guided route.                                             |
| Activity | Rock Climbing Session      |   R750 | Climbing equipment, safety instructions and a professional guide.                               |

The catalogue is stored in `model/BookingManager.kt`. Prices and package details can be updated there.

### 2.2 Discount Rules

The application calculates a discount based on the number of different packages or activities added to the quotation.

| Number of Bookings | Discount |
| ------------------ | -------: |
| 1                  |       0% |
| 2                  |       5% |
| 3                  |      10% |
| 4 or more          |      15% |

The quantity control determines the number of people for each selected package or activity. Increasing the number of people changes the line total but does not increase the number of different bookings.

**Example:**

* Ultimate Adventure Day for two people: R3 000
* Kayaking Experience for one person: R750
* Subtotal: R3 750
* Discount at 5%: R187.50
* Total after discount: R3 562.50

The discount is calculated by `BookingManager.discountPercentFor()`.

---

## 3. Design

### 3.1 User-Centred Design

The application follows user-centred design principles, which focus on the needs and tasks of users (ISO, 2019; Norman, 2013).

The main design decisions are:

* **Navigation:** A persistent bottom navigation bar provides access to Home, Overview, Calculate and Contact. Material Design recommends navigation bars for three to five main destinations on compact screens (Google, n.d.-a).
* **Feedback:** Customers receive confirmation when adding a booking, and the quotation updates when quantities change.
* **Error prevention:** The contact form checks that required fields are completed and that the email address is valid.
* **Accessibility:** Meaningful images have content descriptions, and visible text is stored in string resources (Google, n.d.-b).

### 3.2 Colour Palette

| Colour        | Hex Code  | Purpose                                               |
| ------------- | --------- | ----------------------------------------------------- |
| Forest Green  | `#19472A` | Main buttons, headings and selected navigation items. |
| Sunset Orange | `#D6810B` | Prices and highlights.                                |
| Coral         | `#FF7F6B` | Badges and remove icons.                              |
| White         | `#FFFFFF` | Backgrounds and cards.                                |
| Charcoal      | `#333333` | Main text.                                            |

The colours are defined in `res/values/colors.xml` and used throughout the application theme.

### 3.3 Logo

The logo features a diamond containing a mountain range, forest and rising sun, with the words *ADVENTURE ESCAPE SA* underneath.

The image is stored in `res/drawable-nodpi/img_adventure_logo.png`. The launcher icon uses the same artwork, configured through Android Studio's Image Asset Studio (Google, n.d.-c).

---

## 4. Technical Details

| Item                 | Details                                      |
| -------------------- | -------------------------------------------- |
| IDE                  | Android Studio                               |
| Programming language | Kotlin                                       |
| Build system         | Gradle 9.7.1 and Android Gradle Plugin 9.4.1 |
| SDK versions         | Minimum: 36; Target: 37; Compile: 37         |
| User interface       | XML layouts with View Binding                |
| Main layout          | ConstraintLayout                             |
| Navigation           | Fragments and BottomNavigationView           |
| Lists                | RecyclerView and ListAdapter                 |
| Unit testing         | JUnit 4.13.2                                 |
| Version control      | [GitHub repository link]                     |

### 4.1 Application Architecture

The application uses a single-activity architecture. `MainActivity` hosts the fragment container and persistent bottom navigation bar. Each screen is implemented as a Fragment (Google, n.d.-d).

The application structure is shown below:

```text
MainActivity
├── HomeFragment              Screen 1
├── AboutUsFragment           Screen 2
├── OverviewFragment          Screen 3
├── IndividualDetailFragment  Screen 4
├── CalculateFeeFragment      Screen 5
└── ContactUsFragment         Screen 6
```

Other technical decisions include:

* **RecyclerView:** Displays lists of packages, activities and quotation items (Google, n.d.-e).
* **ListAdapter and DiffUtil:** Help update list items efficiently (Google, n.d.-f).
* **Data classes:** `AdventureItem` and `BookingCartItem` represent adventure and quotation data (JetBrains, n.d.).
* **ConstraintLayout:** Positions interface elements and supports responsive layouts (Google, n.d.-g).
* **String resources:** Visible text is stored in `res/values/strings.xml` (Google, n.d.-b).
* **Edge-to-edge layout:** Window insets are handled to prevent content from being hidden behind system bars (Google, n.d.-h).
* **Image resources:** Large images are stored in `drawable-nodpi` where appropriate to avoid density-based image scaling (Google, n.d.-i).

The quotation is managed by the `BookingManager` object and is stored in memory while the application runs.

---

## 5. Project Structure

```text
app/src/main/
├── AndroidManifest.xml
├── java/com/example/adventureescapesa/
│   ├── MainActivity.kt
│   ├── adapter/
│   │   ├── CalculateFeeAdapter.kt
│   │   ├── FeaturedPackageAdapter.kt
│   │   └── OverviewAdapter.kt
│   ├── model/
│   │   ├── AdventureModels.kt
│   │   └── BookingManager.kt
│   └── ui/
│       ├── HomeFragment.kt
│       ├── AboutUsFragment.kt
│       ├── OverviewFragment.kt
│       ├── IndividualDetailFragment.kt
│       ├── CalculateFeeFragment.kt
│       └── ContactUsFragment.kt
└── res/
    ├── layout/
    ├── values/
    ├── drawable/
    ├── drawable-nodpi/
    ├── menu/
    └── mipmap-*/
```

### 5.1 Updating Images

| Image                         | Location                          |
| ----------------------------- | --------------------------------- |
| Package and activity images   | `BookingManager.kt`               |
| Home logo and hero image      | `fragment_home.xml`               |
| About Us team photo and icons | `fragment_about_us.xml`           |
| Contact page map              | `fragment_contact_us.xml`         |
| Launcher icon                 | Android Studio Image Asset Studio |

Image filenames should use lowercase letters, numbers and underscores, for example `img_kayaking.jpg`.

---

## 6. Running the Application

1. Install Android Studio.
2. Open the `AdventureEscapeSA` project folder.
3. Wait for Gradle synchronisation to finish.
4. Install any required SDK components.
5. Start a compatible Android emulator or connect a physical Android device with USB debugging enabled.
6. Select **Run** in Android Studio.

If the project encounters build issues after files have been moved or renamed, try **Build → Clean Project**, followed by **Build → Rebuild Project**.

---

## 7. Testing

### 7.1 Unit Tests

The project includes local unit tests for the catalogue, discount calculations and quotation totals.

The tests should verify that:

* The catalogue contains four packages priced at R1 500 each.
* The catalogue contains three activities priced at R750 each.
* The discount percentages match the client requirements.
* Sample quotation subtotals, discounts and totals are calculated correctly.

To run the tests, right-click the test folder in Android Studio and select **Run Tests**, or execute:

```bash
./gradlew test
```

### 7.2 Manual Testing Checklist

| No. | Test                                                     | Expected Result                            |
| --- | -------------------------------------------------------- | ------------------------------------------ |
| 1   | Select each bottom navigation item.                      | The correct screen opens.                  |
| 2   | Open About Us and press Back.                            | The previous screen opens.                 |
| 3   | Switch between the Overview tabs.                        | The correct packages or activities appear. |
| 4   | Add an activity to the quotation.                        | A confirmation message appears.            |
| 5   | Add two different bookings.                              | A 5% discount is applied.                  |
| 6   | Add a third and fourth booking.                          | The discount changes to 10% and then 15%.  |
| 7   | Increase or decrease a quantity.                         | The quantity and quotation totals update.  |
| 8   | Remove a booking.                                        | The selected item is removed.              |
| 9   | Proceed with an empty quotation.                         | An appropriate message appears.            |
| 10  | Submit the contact form with missing or invalid details. | Validation errors appear.                  |
| 11  | Submit the contact form correctly.                       | A confirmation message appears.            |

### 7.3 Code Quality

Android Studio's code inspection and lint tools can be used to identify issues such as hardcoded text, unused resources and inefficient layouts (Google, n.d.-j).

---

## 8. Limitations and Future Improvements

The current application has the following limitations:

* **Quotation storage:** The quotation is stored in memory and is cleared when the application closes.
* **Contact form:** The form displays a confirmation message but does not send an actual email or save the enquiry to a server.
* **Images:** Some images may be placeholders until final photographs are available.
* **Contact details:** The contact details are demonstration data and must be replaced with verified company information.

Future improvements could include saving quotations using a local database, connecting the contact form to a backend service and adding real company photographs.

---

## 9. Change Log

# Changelog

All notable changes to the Adventure Escape SA app are recorded here.

## [1.1.0] - 2026-10-09 - Kabelo Litheko

### Added
- Real photos for the adventure packages and activities, stored in `res/drawable-nodpi/`:
  - `group_hike.png`: Ultimate Adventure Day
  - `outdoor_safari.png`: Family Explorer Package
  - `img_adventure.png`: Mountain Adventure Package
  - `kayaking_group.png`: Corporate Team Challenge
  - `ziplining.png`: Zip lining Adventure
  - TODO: Kayaking Experience photo
  - TODO: Rock Climbing Session photo

### Changed
- `BookingManager.kt`: each package and activity now uses its own photo
  (`imageResId`) instead of shared placeholder illustrations.
- Card and detail-page images now use `tools:src` instead of `android:src`,
  so it is clear the real image comes from `BookingManager.kt` and the XML
  image is for the Android Studio preview only:
  - `item_featured_package.xml`
  - `item_overview_card.xml`
  - `fragment_individual_detail.xml`
- Preview sample text (`tools:text`) updated to match the current packages,
  prices and discount rules in `BookingManager.kt`:
  - `item_featured_package.xml`: Ultimate Adventure Day, Full Day • Multi-Activity, From R 1,500 pp
  - `item_overview_card.xml`: Kayaking Experience, Half Day • Rivers & Lakes, R 750
  - `item_calculate_card.xml`: Kayaking Experience, R 750 per person
  - `fragment_individual_detail.xml`: R 750
  - `fragment_overview.xml`: Showing 4 adventure packages
  - `fragment_calculate_fee.xml`: 2 bookings, 5% applied, Next: 10% off,
    R 3,000.00 − R 150.00 = R 2,850.00
- `fragment_calculate_fee.xml`: discount progress bar maximum changed from 5 to 4
  to match `MAX_TIER_BOOKINGS` in the code.

### Removed
- Outdated preview text from an earlier version of the project
  ("Cape Peninsula Eco Odyssey", "Table Mountain 112m Abseil", R 1,450,
  R 2,850 and a "20% tier" that does not exist in the discount rules).
- TODO: Placeholder illustrations (`placeholder_*.xml`) no longer used.

### Fixed
- Changing an image in a card's XML layout appeared to do nothing in the
  running app. The app always loads card images from `BookingManager.kt`;
  the layouts now say this clearly with `tools:src`.
- "Unresolved reference 'drawable'" build error caused by code and layouts
  pointing to image files that had been removed or renamed.
  TODO: confirm fixed after Clean + Rebuild.

### Notes for the group
- Package and activity images: change them in `BookingManager.kt`
  (`imageResId = R.drawable.your_file_name`).
- Home, About Us and Contact images: change `android:src` in that screen's XML.
- Photos go in `res/drawable-nodpi/`. Names: lowercase letters, numbers and
  underscores only, starting with a letter (e.g. `family_package.png`).

---

## 10. Declaration of AI Use

Generative AI (Claude, Anthropic, 2026) was used during development to assist with debugging build errors, reviewing package and import structures, addressing lint warnings, and aligning the application’s data and discount calculations with the client brief.

The group reviewed and adjusted the generated suggestions and accepts responsibility for the submitted work.

---

## 11. References

Anthropic (2026) *Claude* [Large language model]. Available at: https://claude.ai (Accessed: 30 September 2026).

Google (n.d.-a) *Navigation bar – Material Design 3*. Available at: https://m3.material.io/components/navigation-bar/guidelines (Accessed: 30 September 2026).

Google (n.d.-b) *String resources*. Android Developers. Available at: https://developer.android.com/guide/topics/resources/string-resource (Accessed: 30 September 2026).

Google (n.d.-c) *Create app icons*. Android Developers. Available at: https://developer.android.com/studio/write/create-app-icons (Accessed: 30 September 2026).

Google (n.d.-d) *Fragments*. Android Developers. Available at: https://developer.android.com/guide/fragments (Accessed: 30 September 2026).

Google (n.d.-e) *Create dynamic lists with RecyclerView*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/recyclerview (Accessed: 30 September 2026).

Google (n.d.-f) *ListAdapter*. Android Developers. Available at: https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter (Accessed: 30 September 2026).

Google (n.d.-g) *Build a responsive UI with ConstraintLayout*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/constraint-layout (Accessed: 30 September 2026).

Google (n.d.-h) *Display content edge-to-edge in views*. Android Developers. Available at: https://developer.android.com/develop/ui/views/layout/edge-to-edge (Accessed: 30 September 2026).

Google (n.d.-i) *Support different pixel densities*. Android Developers. Available at: https://developer.android.com/training/multiscreen/screendensities (Accessed: 30 September 2026).

Google (n.d.-j) *Improve your code with lint checks*. Android Developers. Available at: https://developer.android.com/studio/write/lint (Accessed: 30 September 2026).

ISO (2019) *ISO 9241-210:2019 Ergonomics of human-system interaction – Part 210: Human-centred design for interactive systems*. Geneva: International Organization for Standardization.

JetBrains (n.d.) *Data classes*. Kotlin Documentation. Available at: https://kotlinlang.org/docs/data-classes.html (Accessed: 30 September 2026).

Norman, D. (2013) *The Design of Everyday Things*. Revised and expanded edn. New York: Basic Books.
