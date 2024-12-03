

### **Tech Stack**

1. **Frameworks and Libraries**:
    - **Jetpack Compose**: Used for building declarative and reactive UI components.
    - **Hilt**: Dependency injection for efficient service and data management.
    - **Room**: Database management for local data persistence.
    - **DataStore**: Storing user preferences such as selected cars and feature configurations.
    - **Flow**: For reactive streams and managing state efficiently.
    - **Coroutines**: Asynchronous programming and threading.
    - **Navigation Component**: For seamless navigation between screens.
    - **Material3**: For adopting modern Material Design principles.

---

### **Architecture**

The application follows **Clean Architecture**, ensuring separation of concerns, testability, and scalability.

1. **Presentation Layer**:
    - Contains `ViewModel` classes and UI components built using Jetpack Compose.
    - Uses `StateFlow` and `SharedFlow` to manage and observe state efficiently.

2. **Domain Layer**:
    - Includes `UseCase` classes for encapsulating business logic.
    - Models defined here are agnostic of specific frameworks or libraries.

3. **Data Layer**:
    - Repository pattern to abstract data sources like Room and DataStore.
    - Contains mappers to transform between domain models and entity models.
    - Uses Room for database management and DataStore for user preferences.

4. **Dependency Injection**:
    - Managed through Hilt with modules for Room, repositories, and managers.

---