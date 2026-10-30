# CineBox

CineBox es una aplicación desarrollada en Kotlin para Android para consultar películas utilizando la API de The Movie Database (TMDB)

Permite observar películas populares y en tendencia, realizar búsquedas, consultar información detallada y guardar películas en una lista personal

## Tecnologías utilizadas

![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-SDK-3DDC84?logo=android&logoColor=white)
![XML](https://img.shields.io/badge/UI-XML-orange)
![Material Design](https://img.shields.io/badge/Material-3-757575?logo=materialdesign&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-REST_API-48B983)
![Gson](https://img.shields.io/badge/Gson-JSON-blue)
![Glide](https://img.shields.io/badge/Glide-Images-00C4CC)
![Paging 3](https://img.shields.io/badge/Jetpack-Paging_3-4285F4)
![Coroutines](https://img.shields.io/badge/Kotlin-Coroutines-7F52FF)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blueviolet)
![Repository](https://img.shields.io/badge/Pattern-Repository-informational)
![TMDB](https://img.shields.io/badge/API-TMDB-01B4E4?logo=themoviedatabase&logoColor=white)

> [!NOTE]
> CineBox utiliza MVVM y Repository Pattern para separar la interfaz, la lógica de presentación y el acceso a los datos

## Funcionalidades

### Enunciado principal

- Visualización de películas populares con título y portada
- Pantalla de detalles con:
  - Título
  - Portada
  - Sinopsis
  - Géneros
  - Rating
- Indicadores de carga durante las consultas
- Manejo de errores y opción para reintentar

### Desafíos opcionales

- **A. Búsqueda de películas:** búsqueda por nombre y mensaje cuando no existen resultados
- **B. Paginado de resultados:** carga de películas populares utilizando Paging 3
- **C. Lista de deseados:** Mi lista para guardar y eliminar películas
- **D. Lista de tendencias:** visualización de películas que fueron tendencia durante la última semana
- **E. Idioma en las solicitudes:** las consultas a TMDB utilizan el idioma configurado en el dispositivo
- **F. Recomendaciones y películas similares:** carruseles de películas similares y recomendadas desde la pantalla de detalles
- **G. Layout adaptativo:** Dos columnas al utilizar la aplicación en orientación horizontal
- **H. Interacción con otras aplicaciones:** opción para compartir una película y abrir su página oficial en el navegador

> [!NOTE]
> Se implementarón los 8 desafíos opcionales propuestos

### Requisitos generales

- Soporte para tema claro y oscuro
- Interfaz disponible en español e inglés
- Adaptación de la interfaz a diferentes configuraciones del dispositivo
- Manejo diferenciado de errores de conexión, servidor y errores desconocidos

### Mejoras adicionales

- Persistencia local de Mi lista mediante SharedPreferences
- Navegación entre Populares, Tendencias y Mi lista mediante Bottom Navigation
- Mantener de la sección seleccionada después de rotar el dispositivo
- Indicadores de desplazamiento en los carruseles de películas similares y recomendadas
- Debounce en las búsquedas para evitar realizar consultas innecesarias mientras el usuario escribe

## Arquitectura

Se utiliza una arquitectura MVVM junto con Repository Pattern

```text
UI -> ViewModel -> Repository -> API / almacenamiento local
```

### UI

Contiene las Activities, RecyclerViews y Adapters encargados de mostrar la información y recibir las acciones del usuario

### ViewModel

Maneja los estados de la interfaz y conecta la UI con los repositorios

Se utiliza LiveData para comunicar cambios de estado con las pantallas

### Repository

Acceso a los datos y evitar que las Activities realicen consultas directamente

- **TMDB API:** películas populares, búsquedas, tendencias, detalles, similares y recomendaciones
- **SharedPreferences:** almacenamiento local

### Organización del proyecto

```text
ar.edu.unicen.seminario
├── data
│   ├── local
│   ├── paging
│   ├── remote
│   └── repository
├── model
└── ui
    ├── detail
    ├── popular
    ├── trending
    └── watchlist
```

## Conceptos aplicados 

Durante el desarrollo de la aplicación se aplicaron distintos conceptos aprendidos durante el seminario y se utilizaron para resolver problemas reales de la aplicación

### Ciclo de vida

Se aprendió a utilizar el ciclo de vida de Android para mantener correctamente el estado de la aplicación

Por ejemplo al rotar el dispositivo se volvía a mostrar la sección de películas populares aunque se estuviera en tendencias o mi lista

Se utilizó la restauración del estado para mantener la parte seleccionada después de un cambio de configuración

### MVVM y LiveData

Se utilizó MVVM para separar la interfaz de la lógica de la aplicación (arquitectura obligatoria)

Las Activities se encargan principalmente de mostrar información mientras que los ViewModels manejan los estados y las operaciones necesarias para obtener los datos

LiveData permite que las pantallas observen cambios como

- Cargando
- Datos disponibles
- Error

### Repository Pattern

Se aprendió a separar el acceso a los datos de la interfaz utilizando Repository Pattern

Las Activities no realizan consultas directamente a la API sino que pasa por los ViewModels y los repositorios


### Coroutines

Se utilizaron Coroutines para realizar operaciones de red y almacenamiento sin bloquear la interfaz

También se utilizaron para implementar un pequeño tiempo de espera en las búsquedas evitando realizar una consulta cada vez que el usuario presiona una tecla

### Retrofit y consumo de APIs

Se aprendió a consumir una API REST utilizando Retrofit

CineBox realiza consultas para obtener:

- Películas populares
- Resultados de búsqueda
- Tendencias
- Detalles
- Películas similares
- Recomendaciones

Gson se utiliza para convertir las respuestas JSON en objetos Kotlin

### RecyclerView y Paging

Se utilizó RecyclerView para mostrar las listas de películas y los carruseles de similares y recomendaciones

También se utilizó Paging 3 para cargar nuevas páginas de películas populares a medida que el usuario llega al final de la lista

### Persistencia local

Se utilizó SharedPreferences para guardar la lista de películas de forma local

Esto permite mantener las películas guardadas aunque se cierre y vuelva a abrir

### Intents

Se utilizaron Intents para distintas formas de navegación e interacción

- Abrir la pantalla de detalles de una película
- Compartir información con otras aplicaciones
- Abrir la página oficial de una película en el navegador

### Kotlin OOP

Durante el proyecto se aplicaron conceptos de Kotlin como

- Clases y funciones
- Data classes
- Interfaces
- Herencia
- Null safety
- Object para Singleton
- Lambdas

### Manejo de errores

Se aprendió a representar distintos estados de una consulta y mostrar una respuesta adecuada al usuario

Diferencia entre errores de conexión, errores del servidor y errores desconocidos además de permitir reintentar las consultas

## Investigación complementaria

Se investigó documentación complementaria para resolver algunos desafíos y mejorar el funcionamiento de la aplicación

### Paging 3

Para implementar el paginado de películas populares se investigó Paging 3

Esto permitió cargar nuevas páginas automáticamente a medida que el usuario llega al final de la lista sin tener que manejar manualmente cada número de página

### Bottom Navigation

Para implementar Mi lista y Tendencias se investigó Bottom Navigation

Se utilizó para cambiar entre las distintas secciones de la pantalla principal manteniendo una navegación simple

### Idioma de las consultas

Se investigó el uso de Locale y los parámetros de Retrofit para enviar el idioma en las consultas

De esta manera la información de las películas se obtiene en español o inglés según la configuración del dispositivo

### Películas similares y recomendaciones

Se investigaron los endpoints de TMDB para obtener películas similares y recomendadas

También fue necesario implementar la navegación desde estos carruseles hacia los detalles de otra película

### Interacción con otras aplicaciones

Se investigó el uso de Intents para comunicar con otras aplicaciones:

- Compartir información de una película
- Abrir la página oficial en el navegador

### Debounce

Se investigó una forma de evitar realizar una consulta a la API por cada letra escrita

Se implementó un debounce utilizando Coroutines que espera unos milisegundos antes de realizar la búsqueda y cancela la consulta anterior si el usuario sigue escribiendo

### Restauración de navegación

Durante las pruebas se encontró que al rotar el dispositivo la aplicación podía volver a mostrar películas populares aunque el usuario estuviera en otra sección

Se investigó la restauración del estado para mantener correctamente la sección seleccionada después de un cambio de configuración

### Manejo de estados y errores

También se trabajó sobre la forma de representar los distintos estados de las consultas

Se implementaron estados para carga, resultados y errores permitiendo mostrar distintos mensajes dependiendo del problema encontrado

> [!NOTE]
> Parte de esta investigación viene a partir de los desafíos opcionales del enunciado y otra parte apareció por optimización

## Calidad del código

Se realizaron distintas revisiones para mejorar la legibilidad y evitar código repetido
Pero tuvo que ser reducido debido al alto consumo de Android Studio dando problemas de rendimiento 

### Clean Code

Se revisaron nombres de variables, funciones, organización de archivos y responsabilidades de las clases

También se intentó mantener funciones pequeñas y nombres que expliquen claramente qué realiza cada parte del código

### Code Smells

Se buscaron problemas como código duplicado, responsabilidades innecesarias y lógica repetida

Algunas mejoras realizadas fueron

- Centralizar el manejo de errores de la pantalla principal
- Reutilizar la lógica de estados de los carruseles de películas similares y recomendadas
- Revisar las consultas y evitar agregar abstracciones

> [!IMPORTANT]
> No todos fueron modificados ya que en algunos casos una nueva abstracción agregaba más complejidad de la que solucionaba

### Operaciones asíncronas

Las operaciones de red y almacenamiento se realizan utilizando Coroutines para evitar bloquear la interfaz

También se utiliza cancelación de tareas en las búsquedas para evitar consultas innecesarias

### Separación de responsabilidades

La aplicación mantiene separadas las responsabilidades principales:

- Activities para la interfaz
- ViewModels para los estados y lógica de presentación
- Repositories para el acceso a los datos
- Retrofit para las consultas a la API
- SharedPreferences para el almacenamiento local

  
### Seguridad

El token utilizado para acceder a TMDB no se encuentra escrito directamente

Se obtiene desde la configuración local del proyecto evitando incluirlo directamente en el repositorio

## Manejo de errores

### Estados de carga

Mientras se obtiene información desde la API se muestra un indicador de carga para populares, búsquedas, tendencias, detalles, similares y recomendaciones

### Errores de conexión

Si el dispositivo no tiene conección a internet se muestra un mensaje específico indicando el problema

El usuario puede volver a intentar la consulta utilizando el botón de reintentar

### Errores del servidor

Si la consulta llega al servidor pero ocurre un error en la respuesta se muestra un mensaje diferente al error de conexión

Esto permite identificar el origen del problema

### Errores desconocidos

Si ocurre un error que no corresponde a conexión o servidor se muestra un mensaje general evitando dejar la pantalla sin información

### Resultados vacíos

También se manejan casos donde la consulta funciona correctamente pero no devuelve información

Por ejemplo

- Una búsqueda sin resultados
- Mi lista vacía
- Una lista de películas similares vacía
- Una lista de recomendaciones vacía

### Reintentos

Las pantallas que dependen de consultas permiten volver a intentar la carga cuando ocurre un error

De esta manera no es necesario cerrar y volver a abrir la aplicación

## Persistencia

Se utiliza SharedPreferences para guardar las películas agregadas a la lista

Los datos se convierten a JSON utilizando Gson antes de almacenarse de forma local

### Mi lista

Cuando el usuario agrega una película se guardan los datos necesarios para volver a mostrarla dentro de la sección Mi lista

También se puede eliminar una película desde la pantalla de detalles

### Conservación de datos

La información guardada permanece disponible aunque la aplicación se cierre y se vuelva a abrir

Esto permite mantener la lista personal sin necesidad de consultar nuevamente todos los datos desde cero

> [!NOTE]
> Se utilizó SharedPreferences porque la cantidad de información almacenada es pequeña y no requiere una base de datos más compleja como Room como se menciona en el seminario

## API de TMDB

### Datos utilizados

Se consultan distintos servicios para obtener

- Películas populares
- Resultados de búsqueda
- Películas en tendencia
- Detalles de una película
- Películas similares
- Recomendaciones

### Idioma

Las consultas incluyen el idioma configurado en el dispositivo

Esto permite obtener títulos, descripciones y demás información en español o inglés según la configuración del usuario

### Imágenes

Las portadas de las películas se cargan utilizando las rutas de imágenes proporcionadas por TMDB

Glide se encarga de descargar y mostrar las imágenes dentro de la aplicación

### Autenticación

Para acceder a la API es necesario utilizar un token de TMDB, obtenido desde la web

> [!IMPORTANT]
> Para ejecutar es necesario contar con un token válido

## Configuración del proyecto

Para ejecutar es necesario configurar un token válido de TMDB

### 1. Clonar el repositorio

```bash
git clone (URL)
```

### 2. Abrir el proyecto

Abrir la carpeta del proyecto desde Android Studio y sincronizar con Gradle

### 3. Configurar el token

Agregar el token dentro del archivo `local.properties`

```properties
TMDB_ACCESS_TOKEN= (TOKEN)
```

### 4. Ejecutar la aplicación

Conectar un dispositivo Android o iniciar uno desde Android Studio y ejecutar la aplicación desde Android Studio

> [!WARNING]
> Si el token no está configurado correctamente las consultas no podrán completarse

## Pruebas realizadas

Se realizaron pruebas manuales para comprobar funcionalidades

### Navegación

- Cambiar Populares, Tendencias y Mi lista
- Navegación desde una película hacia sus detalles
- Navegación hacia películas similares y recomendadas
- Regreso correcto a la película anterior

### Búsqueda

- Búsqueda de películas por nombre
- Resultados sin coincidencias
- Volver a películas populares al borrar la búsqueda
- Debounce para evitar consultas innecesarias

### Mi lista

- Agregar películas
- Eliminar películas
- Actualización de la lista al volver desde detalles
- Persistencia después de cerrar y volver a abrir la aplicación

### Rotación y diseño

Se probaron diferentes estados antes y después de rotar el dispositivo

- Películas populares
- Búsqueda activa
- Tendencias
- Mi lista
- Detalles de una película
- Vista horizontal con dos columnas

### Estados y errores

- Indicadores de carga
- Error de conexión
- Error del servidor
- Error desconocido
- Reintento de consultas
- Listas vacías
- Búsquedas sin resultados

### Configuración

- Tema claro
- Tema oscuro
- Interfaz en español
- Interfaz en inglés
- Información obtenida desde TMDB según el idioma del dispositivo

### Otras funcionalidades

- Carga mediante Paging 3
- Películas similares
- Películas recomendadas
- Compartir una película con otra aplicación
- Abrir la página oficial de una película en el navegador

## Decisiones técnicas

### MVVM y Repository Pattern

Se utilizó MVVM junto con Repository Pattern para separar la interfaz, los estados y el acceso a los datos

Esto evita que las Activities tengan que realizar consultas directamente a la API o manejar la persistencia

### SharedPreferences en lugar de Room

Se utilizó SharedPreferences porque se almacena una cantidad reducida de información

No fue necesario implementar una base de datos completa ya que no se realizan consultas complejas sobre los datos guardados

### XML y View Binding

La interfaz se desarrolló utilizando XML junto con View Binding

Se mantuvo este enfoque durante todo el proyecto en lugar de mezclarlo con Jetpack Compose

### Paging 3

Se utilizó Paging 3 para cargar películas populares por páginas

Esto evita tener que manejar manualmente cada número de página y permite cargar nuevos resultados a medida que el usuario avanza por la lista

### LiveData

Se utilizó LiveData para comunicar los estados de los ViewModels con las Activities

Esto permite actualizar la interfaz cuando cambian los datos sin consultar constantemente el estado desde la Activity

### Coroutines

Se utilizaron Coroutines para las operaciones de red y almacenamiento

También permiten cancelar búsquedas anteriores cuando el usuario sigue escribiendo evitando realizar consultas innecesarias

### Manejo de errores

Se decidió diferenciar los errores según su origen

- Error de conexión
- Error del servidor
- Error desconocido

Esto permite mostrar mensajes más claros y ofrecer la opción de volver a intentar

Se decidió no hacerlo cuando la nueva solución agregaba más complejidad que la que eliminaba

> [!IMPORTANT]
> Las decisiones se tomaron buscando mantener el proyecto fácil de entender y mantener

## Referencias y bibliografía

Para el desarrollo de CineBox se utilizó material del Seminario de Desarrollo Mobile Android 2026 junto con documentación oficial y recursos externos para investigar conceptos y resolver problemas encontrados durante el desarrollo

> [!NOTE]
> La documentación oficial de Android, Kotlin, Material Design y TMDB fue utilizada como referencia principal

> [!NOTE]
> Algunos enlaces guardados en chats de clases anteriores quedaron truncados en el historial por lo que se agregan las páginas oficiales actuales correspondientes al mismo tema en lugar de reconstruir URLs incompletas

### Material del seminario y fundamentos de Android

- [Android Studio](https://developer.android.com/studio?hl=es-419)
- [Instalar Android Studio](https://developer.android.com/studio/install)
- [Android API Levels](https://apilevels.com)
- [Fundamentos de aplicaciones Android](https://developer.android.com/guide/components/fundamentals?hl=es-419)
- [SDK Platform Tools](https://developer.android.com/tools/releases/platform-tools?hl=es-419)
- [Android Developers](https://developer.android.com/)
- [Android Jetpack](https://developer.android.com/jetpack)
- [Sistema de compilación de Android](https://developer.android.com/build)
- [Build variants](https://developer.android.com/build/build-variants)
- [Consejos de Gradle y BuildConfig](https://developer.android.com/build/gradle-tips)
- [Android Gradle Plugin API](https://developer.android.com/reference/tools/gradle-api)
- [Recursos de Android](https://developer.android.com/guide/topics/resources/providing-resources)
- [Cambios de configuración](https://developer.android.com/guide/topics/resources/runtime-changes)

### Kotlin

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Basic syntax](https://kotlinlang.org/docs/basic-syntax.html)
- [Classes](https://kotlinlang.org/docs/classes.html)
- [Inheritance](https://kotlinlang.org/docs/inheritance.html)
- [Interfaces](https://kotlinlang.org/docs/interfaces.html)
- [Data classes](https://kotlinlang.org/docs/data-classes.html)
- [Object declarations y Singleton](https://kotlinlang.org/docs/object-declarations.html)
- [Null safety](https://kotlinlang.org/docs/null-safety.html)
- [Lambdas](https://kotlinlang.org/docs/lambdas.html)
- [Collections](https://kotlinlang.org/docs/collections-overview.html)
- [Control flow](https://kotlinlang.org/docs/control-flow.html)
- [Properties](https://kotlinlang.org/docs/properties.html)
- [Constructors](https://kotlinlang.org/docs/classes.html#constructors)
- [Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)

### XML, Views y View Binding

- [Layouts en XML](https://developer.android.com/develop/ui/views/layout/declaring-layout)
- [View Binding](https://developer.android.com/topic/libraries/view-binding)
- [Views](https://developer.android.com/develop/ui/views)
- [ConstraintLayout](https://developer.android.com/develop/ui/views/layout/constraint-layout)
- [Recursos de strings](https://developer.android.com/guide/topics/resources/string-resource)
- [Recursos alternativos](https://developer.android.com/guide/topics/resources/providing-resources)

### Ciclo de vida y estado

- [Activity lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
- [Save UI states](https://developer.android.com/topic/libraries/architecture/saving-states)
- [Configuration changes](https://developer.android.com/guide/topics/resources/runtime-changes)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)

### Intents y comunicación entre aplicaciones

- [Intents e Intent Filters](https://developer.android.com/guide/components/intents-filters)
- [Common intents](https://developer.android.com/guide/components/intents-common)
- [ACTION_SEND](https://developer.android.com/reference/android/content/Intent#ACTION_SEND)
- [ACTION_VIEW](https://developer.android.com/reference/android/content/Intent#ACTION_VIEW)

### Arquitectura, MVVM y Repository Pattern

- [Guía de arquitectura de aplicaciones Android](https://developer.android.com/topic/architecture)
- [Recomendaciones de arquitectura](https://developer.android.com/topic/architecture/recommendations)
- [Arquitectura para Views](https://developer.android.com/topic/architecture/views/recommendations-views)
- [UI layer](https://developer.android.com/topic/architecture/ui-layer)
- [Data layer](https://developer.android.com/topic/architecture/data-layer)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [Coroutines con componentes lifecycle-aware](https://developer.android.com/topic/libraries/architecture/coroutines)

### Coroutines y operaciones asíncronas

- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Coroutines en Android](https://developer.android.com/kotlin/coroutines)
- [Best practices for coroutines in Android](https://developer.android.com/kotlin/coroutines/coroutines-best-practices)
- [viewModelScope](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope)
- [lifecycleScope](https://developer.android.com/topic/libraries/architecture/coroutines#lifecyclescope)
- [repeatOnLifecycle](https://developer.android.com/topic/libraries/architecture/coroutines#restart)
- [Cancellation and timeouts](https://kotlinlang.org/docs/cancellation-and-timeouts.html)

### Persistencia local

- [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Opciones de almacenamiento](https://developer.android.com/training/data-storage)

### Hilt e inyección de dependencias

- [Dependency injection in Android](https://developer.android.com/training/dependency-injection)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Hilt y Jetpack](https://developer.android.com/training/dependency-injection/hilt-jetpack)

### Retrofit, OkHttp y Gson

- [Retrofit](https://github.com/square/retrofit)
- [Retrofit Javadoc](https://javadoc.io/doc/com.squareup.retrofit2/retrofit/latest/index.html)
- [Retrofit - referencia utilizada en el enunciado](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [OkHttp Interceptors](https://square.github.io/okhttp/features/interceptors/)
- [Gson](https://github.com/google/gson)
- [Gson User Guide](https://github.com/google/gson/blob/main/UserGuide.md)

### RecyclerView, listas e imágenes

- [RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview)
- [RecyclerView API](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView)
- [RecyclerView.Adapter](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter)
- [RecyclerView.ViewHolder](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder)
- [LinearLayoutManager](https://developer.android.com/reference/androidx/recyclerview/widget/LinearLayoutManager)
- [GridLayoutManager](https://developer.android.com/reference/androidx/recyclerview/widget/GridLayoutManager)
- [Glide](https://github.com/bumptech/glide)
- [Glide Documentation](https://bumptech.github.io/glide/)
- [Random User API - utilizada como recurso de ejemplo en clase](https://randomuser.me/)

### Paging 3

- [Paging 3 overview](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Load and display paged data](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data)
- [PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource)
- [Pager](https://developer.android.com/reference/kotlin/androidx/paging/Pager)
- [PagingDataAdapter](https://developer.android.com/reference/kotlin/androidx/paging/PagingDataAdapter)
- [LoadState](https://developer.android.com/reference/kotlin/androidx/paging/LoadState)
- [LoadStateAdapter](https://developer.android.com/reference/kotlin/androidx/paging/LoadStateAdapter)

### Bottom Navigation

- [BottomNavigationView](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)
- [Material Components - Bottom Navigation](https://github.com/material-components/material-components-android/blob/master/docs/components/BottomNavigation.md)
- [Material 3 Navigation Bar](https://m3.material.io/components/navigation-bar/overview)

### Idioma y localización

- [Locale](https://developer.android.com/reference/java/util/Locale)
- [Localización de aplicaciones](https://developer.android.com/guide/topics/resources/localization)
- [Soporte para distintos idiomas](https://developer.android.com/training/basics/supporting-devices/languages)
- [App languages](https://developer.android.com/guide/topics/resources/app-languages)

### Jetpack Compose

- [Android UI](https://developer.android.com/develop/ui)
- [Jetpack Compose](https://developer.android.com/compose)
- [Compose documentation](https://developer.android.com/develop/ui/compose/documentation)
- [Compose basics](https://developer.android.com/develop/ui/compose/tutorial)
- [Compose layouts](https://developer.android.com/develop/ui/compose/layouts/basics)
- [State in Compose](https://developer.android.com/develop/ui/compose/state)
- [Compose modifiers](https://developer.android.com/develop/ui/compose/modifiers)
- [Material 3 en Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)

### Navigation Compose

- [Navigation](https://developer.android.com/guide/navigation)
- [Navigation Compose](https://developer.android.com/develop/ui/compose/navigation)
- [Navigation with Compose](https://developer.android.com/develop/ui/compose/migrate/migration-scenarios/navigation)
- [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)

### Material Design 3

- [Material Design 3 - Styles](https://m3.material.io/styles)
- [Material Design 3 - Color](https://m3.material.io/styles/color/overview)
- [Material Design 3 - Color roles](https://m3.material.io/styles/color/roles)
- [Material Design 3 - Dynamic color](https://m3.material.io/styles/color/dynamic-color)
- [Material Design 3 - Typography](https://m3.material.io/styles/typography/overview)
- [Material Design 3 - Shape](https://m3.material.io/styles/shape/overview-principles)
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [Material Components - Color theming](https://github.com/material-components/material-components-android/blob/master/docs/theming/Color.md)

### Tema claro, oscuro y diseño adaptativo

- [Dark theme](https://developer.android.com/develop/ui/views/theming/darktheme)
- [Responsive and adaptive design with Views](https://developer.android.com/develop/ui/views/layout/responsive-adaptive-design-with-views)
- [Window size classes](https://developer.android.com/develop/adaptive-apps/guides/use-window-size-classes)
- [Adaptive layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive)

### Iconos y recursos gráficos

- [Material Symbols](https://fonts.google.com/icons)
- [Material Symbols Guide](https://developers.google.com/fonts/docs/material_symbols)
- [Material Design Icons](https://github.com/google/material-design-icons)
- [Vector Drawables](https://developer.android.com/develop/ui/views/graphics/vector-drawable-resources)
- [Adaptive Icons](https://developer.android.com/develop/ui/views/launch/icon_design_adaptive)
- [Adaptive Icons - AOSP](https://source.android.com/docs/core/display/adaptive-icons)
- [Launcher Icon Codelab](https://codelabs.developers.google.com/design-android-launcher)
- [App Icon Generator](https://www.appicon.co)

### GitHub y documentación del README

- [GitHub Markdown](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
- [GitHub Alerts](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#alerts)
- [Shields.io](https://shields.io/)
- [Shields.io Documentation](https://www.shields.io/docs)

### TMDB

- [The Movie Database](https://www.themoviedb.org/)
- [Configuración de API de TMDB](https://www.themoviedb.org/settings/api)
- [TMDB Developer Documentation](https://developer.themoviedb.org/)
- [Getting Started](https://developer.themoviedb.org/docs/getting-started)
- [Authentication](https://developer.themoviedb.org/docs/authentication-application)
- [Popular Movies](https://developer.themoviedb.org/reference/movie-popular-list)
- [Movie Details](https://developer.themoviedb.org/reference/movie-details)
- [Image Basics](https://developer.themoviedb.org/docs/image-basics)
- [Search Movie](https://developer.themoviedb.org/reference/search-movie)
- [Trending Movies](https://developer.themoviedb.org/reference/trending-movies)
- [Similar Movies](https://developer.themoviedb.org/reference/movie-similar)
- [Movie Recommendations](https://developer.themoviedb.org/reference/movie-recommendations)
- [Popularity and Trending](https://developer.themoviedb.org/docs/popularity-and-trending)
- [Finding Data](https://developer.themoviedb.org/docs/finding-data)
- [Search and Query for Details](https://developer.themoviedb.org/docs/search-and-query-for-details)
- [TMDB Errors](https://developer.themoviedb.org/docs/errors)

### Referencias incluidas directamente en el enunciado del TPE

- [TMDB](https://www.themoviedb.org/)
- [TMDB Authentication](https://developer.themoviedb.org/docs/authentication-application)
- [Popular Movies](https://developer.themoviedb.org/reference/movie-popular-list)
- [Movie Details](https://developer.themoviedb.org/reference/movie-details)
- [Image Basics](https://developer.themoviedb.org/docs/image-basics)
- [Search Movie](https://developer.themoviedb.org/reference/search-movie)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Trending Movies](https://developer.themoviedb.org/reference/trending-movies)
- [Locale](https://developer.android.com/reference/java/util/Locale)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp Interceptors](https://square.github.io/okhttp/features/interceptors/)
- [Similar Movies](https://developer.themoviedb.org/reference/movie-similar)
- [Movie Recommendations](https://developer.themoviedb.org/reference/movie-recommendations)

### Configuración y seguridad de claves

- [Secrets Gradle Plugin](https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin)
- [Gradle tips](https://developer.android.com/build/gradle-tips)
- [BuildConfig](https://developer.android.com/reference/androidx/build/gradle/internal/cxx/configure/BuildConfig)
- [Git Ignore Documentation](https://git-scm.com/docs/gitignore)

### Testing y depuración

- [Test apps on Android](https://developer.android.com/training/testing)
- [Fundamentals of testing Android apps](https://developer.android.com/training/testing/fundamentals)
- [Local tests](https://developer.android.com/training/testing/local-tests)
- [Instrumented tests](https://developer.android.com/training/testing/instrumented-tests)
- [Espresso](https://developer.android.com/training/testing/espresso)
- [Debug your app](https://developer.android.com/studio/debug)
- [Logcat](https://developer.android.com/studio/debug/logcat)
- [Android Debug Bridge](https://developer.android.com/tools/adb)

### Stack Overflow

- [Stack Overflow - Android](https://stackoverflow.com/questions/tagged/android)
- [Stack Overflow - Kotlin](https://stackoverflow.com/questions/tagged/kotlin)
- [Stack Overflow - Retrofit](https://stackoverflow.com/questions/tagged/retrofit2)
- [Stack Overflow - Glide](https://stackoverflow.com/questions/tagged/android-glide)
- [Stack Overflow - RecyclerView](https://stackoverflow.com/questions/tagged/android-recyclerview)
- [Stack Overflow - Room](https://stackoverflow.com/questions/tagged/android-room)
- [Stack Overflow - Paging 3](https://stackoverflow.com/questions/tagged/android-paging-3)
- [Stack Overflow - Coroutines](https://stackoverflow.com/questions/tagged/kotlin-coroutines)
- [Stack Overflow - SharedPreferences](https://stackoverflow.com/questions/tagged/sharedpreferences)
- [Stack Overflow en español - Android](https://es.stackoverflow.com/questions/tagged/android)
- [Stack Overflow en español - Kotlin](https://es.stackoverflow.com/questions/tagged/kotlin)

### Otros

- [API Levels](https://apilevels.com)
- [Random User API](https://randomuser.me/)
- [App Icon Generator](https://www.appicon.co)
- [Google Fonts Icons](https://fonts.google.com/icons)
- [Shields.io](https://shields.io/)
