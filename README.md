# Tastel – App Móvil de Recetas  
### Proyecto Académico – Parcial I y II + Final 
**Materia:** Aplicaciones Móviles (Da Vinci)  
**Profesor:** Sergio Daniel Medina  
**Integrantes:** Franco Martín Schimizzi · Melina Rocío Martins

---

# Descripción general

**Tastel** es una aplicación móvil de recetas desarrollada en Android Studio utilizando **Java** y **XML**, con un enfoque moderno basado en **Material Design**, API externas mediante **Retrofit**, y un backend completamente funcional soportado por **Firebase Authentication**, **Firebase Firestore** y **Firebase Storage**.

El proyecto se desarrolló en dos etapas principales:

- **Parcial I:** Implementación del catálogo local, navegación, diseño visual, API remota con Retrofit y favoritos.
- **Parcial II:** Integración completa con Firebase, autenticación real, comunidad de recetas, CRUD de publicaciones, perfil editable y sincronización en tiempo real.

El resultado es una aplicación sólida, con arquitectura clara, navegación fluida y funcionalidades que exceden ampliamente los requisitos mínimos de ambos parciales.

---

# 🟢 Parcial I – Base del proyecto

## Diseño y experiencia de usuario
En el Parcial I se definió la estructura base de la aplicación:

- Navegación mediante **Splash Screen**, Login, Home y Drawer lateral.
- Pantallas diseñadas con **Material Design Components**, utilizando:
  - `CardView`
  - `MaterialToolbar`
  - `TextInputLayout`
  - `ChipGroup`
  - `RecyclerView`
- Paleta de colores, tamaños y estilos unificados en:
  - `/values/colors.xml`
  - `/values/dimens.xml`
  - `/values/styles.xml`

El objetivo fue generar una interfaz moderna, consistente y visualmente atractiva.

---

## Catálogo de recetas con Retrofit

Se utilizó **Retrofit + Gson** para consumir una API personalizada basada en un archivo JSON alojado en:

```
https://cdn.jsdelivr.net/gh/usuario/repositorio/recipes.json
```

La estructura del JSON contiene:

- Título
- Descripción
- Categoría
- Ingredientes
- Pasos
- Tiempo estimado
- Imagen

Se implementaron las clases:

- `RetrofitClient`
- `RecipesApiService`
- `RecipeAdapter`
- `RecipeDetailActivity`

Estas permiten obtener información, mapearla en objetos Java y mostrarla de manera dinámica.

---

## Favoritos – Persistencia local

Los favoritos se manejan mediante:

```
SharedPreferences + Gson
```

Cada usuario mantiene sus propios favoritos gracias a un identificador asociado al login.  
La lógica contempla agregar, remover y renderizar favoritos en tiempo real.

---

# 🟣 Parcial II – Integración con Firebase

En el Parcial II el proyecto incorporó características propias de una plataforma real:

- Autenticación completa
- Gestión de usuarios
- Comunidad de recetas publicadas por usuarios
- CRUD completo
- Almacenamiento de imágenes
- Feed dinámico en tiempo real

---

# Autenticación Firebase

## Registro de usuario
El registro incluye:

- Nombre
- Apellido
- Email
- **Username único**
- Contraseña

Datos almacenados en:

```
Firestore → usuarios/{uid}
Auth → email/password
```

Esto permite manejar la identidad del usuario en toda la app.

---

## Inicio de sesión (Email o Username)

El usuario puede iniciar sesión con:

- Email directamente
- Username → búsqueda en Firestore → conversión a email automático

Esto permite una experiencia más flexible y moderna.

---

## Edición del perfil

En el perfil el usuario puede modificar:

- Nombre
- Apellido
- Email (requiere reautenticación)
- Contraseña

El **username no se puede editar**, garantizando consistencia en las recetas publicadas.

Toda modificación actualiza:

- Firebase Authentication
- Firestore (`usuarios/{uid}`)

---

# Comunidad – CRUD completo con Firestore

La comunidad es el núcleo del Parcial II: un espacio donde los usuarios pueden crear, editar y eliminar recetas.  
La implementación utiliza **Firestore + Firebase Storage** para almacenar datos y fotos.

---

## Estructura de datos en Firestore

```
community_recipes/
   └── recipeId
        ├── title
        ├── description
        ├── time
        ├── imageUrl
        ├── authorId
        ├── authorUsername
        ├── authorEmail
        ├── createdAt
```

Cada receta contiene información completa del autor y permite renderizar correctamente en cualquier sección.

---

## Crear receta

La creación se realiza desde `CreateCommunityRecipeDialog`.

Permite:

- Subir imagen (Firebase Storage)
- Ingresar título
- Ingresar descripción
- Seleccionar tiempo (con flechas ↑↓)
- Publicar la receta en Firestore

La receta queda automáticamente asociada al usuario mediante `authorId`.

---

## Editar receta

Solo el autor puede editar una receta:

```
if (recipe.authorId == FirebaseAuth.getInstance().getUid())
```

La edición afecta:

- Título
- Descripción
- Tiempo
- Imagen

El documento de Firestore se actualiza en tiempo real.

---

## Eliminar receta

Si el usuario es dueño:

- Se elimina el documento de Firestore
- Se borra la imagen de Firebase Storage
- La comunidad se actualiza dinámicamente por listeners en tiempo real

---

## Feed de comunidad

Implementado en `CommunityActivity`.

Utiliza:

```
Firebase Firestore addSnapshotListener()
```

Esto permite:

- Recetas en tiempo real
- Ordenadas por fecha (`createdAt DESC`)
- Renderizado en `RecyclerView` mediante `CommunityRecipeAdapter`

Cada card incluye:

- Imagen
- Título
- Autor con @username
- Tiempo de preparación
- Descripción resumida
- Acción de favoritos

---

# Favoritos avanzados

Los favoritos ahora soportan:

- Recetas locales del JSON
- Recetas de la comunidad cargadas desde Firestore

Las recetas publicadas por usuarios se convierten dinámicamente en objetos compatibles con el sistema de favoritos.

---

# 🔵 Mejoras Implementadas para el Final (Entrega Final)

En la instancia final se incorporaron funcionalidades y mejoras que extienden el proyecto y brindan una experiencia más completa.

---

## 🗺️ Integración con Google Maps (Mapa Gastronómico)

Nueva sección accesible desde el Drawer:

- Google Maps SDK completamente integrado  
- Marcadores temáticos:
  - 🍝 Pastas  
  - 🥗 Veggie  
  - 🍔 Fast Food  
  - 🍰 Postres  
  - 🍣 Sushi  
  - 🍕 Pizzerías  
  - ☕ Cafeterías  
- Drawer funcional dentro del mapa  
- Cámara inicial sobre Microcentro (CABA)  
- Estructura lista para markers dinámicos desde Firestore  

---

## Recuperación de contraseña desde el Login

Ahora el usuario puede recuperar su acceso:

- Funciona con email **o username**  
- Si ingresa username → se busca en Firestore → se convierte a email  
- Firebase envía el correo automático  
- Flujo más profesional y completo  

---

## Feedback de UX mejorado

Se agregaron mensajes descriptivos cuando no hay contenido:

- Favoritos vacíos  
- Comunidad sin recetas  
- Búsquedas sin resultados  
- Listas vacías en general  

Esto evita pantallas “muertas” y orienta al usuario.

---

## Mejoras en el Home y navegación

- Acceso al mapa gastronómico desde el menú  
- Ajustes visuales en cards, espaciados y tipografías  
- Drawer actualizado con nuevas secciones  

---

## Correcciones finales de API y CDN

Para estabilidad del catálogo:

- API apuntando al **repositorio final**  
- Uso de **jsDelivr** para evitar límites de GitHub (HTTP 429)  
- Validación final del archivo `recipes.json`  

---

## Refactor de MapActivity

Mejoras aplicadas:

- Drawer + Toolbar integrado  
- Marcadores categorizados  
- Código modular y escalable  
- UI coherente con el resto de la app  

---

# Estructura del proyecto

```
app/
├── java/com.app.tasteit/
│ ├── AccountMenuHelper.java
│ ├── CommunityActivity.java
│ ├── CommunityRecipe.java
│ ├── CommunityRecipeAdapter.java
│ ├── CommunityRecipeDetailActivity.java
│ ├── CreateCommunityRecipeDialog.java
│ ├── EditCommunityRecipeDialog.java
│ ├── LoginActivity.java
│ ├── RegisterActivity.java
│ ├── ProfileActivity.java
│ ├── RecipesApiService.java
│ ├── RecipeAdapter.java
│ ├── RecipeDetailActivity.java
│ ├── RecipeFormActivity.java
│ ├── SplashActivity.java
│ ├── MapActivity.java
│ └── RetrofitClient.java
├── res/
│ ├── layout/
│ ├── drawable/
│ ├── values/
│ └── mipmap/
└── AndroidManifest.xml
```

---

# Cómo ejecutar el proyecto

1. Clonar el repositorio  
2. Abrir en Android Studio  
3. Agregar `google-services.json`  
4. Agregar la API Key de Google Maps  
5. Sincronizar con Gradle  
6. Ejecutar en emulador o dispositivo real  

---

# Documentación entregada

- Informe Parcial I  
- Informe Parcial II
- Informe FINAL  
- Mockups completos  
- Capturas finales  
- Estructura Firebase  
- Diagramas de datos  
- Especificación técnica  

---

# Conclusión

Tastel evolucionó de un simple catálogo a una **plataforma social completa**, con funcionalidades robustas, arquitectura modular, sincronización en tiempo real y una interfaz totalmente integrada con Firebase.

El proyecto cumple y supera todas las consignas del Parcial I y II, logrando una aplicación profesional y lista para escalabilidad futura.

---

# Links

**Repositorio GitHub:**  
https://github.com/ccchimi/final-am-ac-acn4a-Schimizzi-Martins  
https://github.com/ccchimi/final-am-ac-acn4a-Schimizzi-Martins.git  

(Acceso habilitado para: *sergiomedinaio* y *IJSagnella*)

**Figma Mockups:**  
https://www.figma.com/design/FJAG6taGZRovizosxl6VxG/Mockups?node-id=0-1&t=a4yBUrBSA1g1g6Mb-1

---

# Autores

- **Franco Martín Schimizzi**  
- **Melina Rocío Martins**
