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
