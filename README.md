# Clean Architecture Implementation of the newsapi.org API

This repository contains a sample app that implements Clean Architecture in MVVM using Kotlin, Live Data, ViewModel, Retrofit, Hilt, Coroutines, Flow and etc.

#### Instructions to open the project in Android Studio:
1. Open Android Studio
2. Select Project From Version Control
3. Clone the [Git URL](https://github.com/asdheeraj/News) by selecting the latest branch
4. Add the Base URL value with key "baseURL" and apiKey value with the key "apiKey" in the local.properties file. (Please note that without adding these values, the application would not run on a device/emulator)
5. Run the app on an emulator/connected Device

#### The app has following packages:
1. **data**: The Data Layer responsible for Fetching the Data from a Remote or a Local Data Source
2. **di**: The Dependency Injection Package responsible for Providing the required Dependencies
3. **domain**: The Domain Layer responsible for interacting with the Data and the Presentation Layer and is responsible to hold the Domain Logic
4. **presentation**: The Presentation Layer responsible for interacting with the User and displaying the required data.
5. **androidTest**: A Package holding the various UI test cases for the project
6. **test**: A Package holding the various unit test cases for the project, including tests for data flow and business logic
