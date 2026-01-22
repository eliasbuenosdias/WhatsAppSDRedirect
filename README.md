<img src="https://eliasbuenosdias.github.io/Elias-Porfolio/recursos/whatsAppSDRedirect.png" alt="WhatsApp SD Redirect" width="100" height="auto">


## 📱 Descripción
Módulo LSPosed que redirige automáticamente todos los archivos multimedia de WhatsApp (imágenes, videos, audios, documentos) a la microSD sin que la app pierda la capacidad de visualizarlos.

**NUEVO**: ¡Ahora detecta automáticamente tu microSD! No necesitas configurar manualmente la ruta.

## ✅ Requisitos
- ✓ Dispositivo Android con root
- ✓ Android 10 o superior
- ✓ LSPosed Framework instalado
- ✓ Tarjeta microSD instalada y formateada

## 🚀 Características Principales

### ✨ Detección Automática de MicroSD
El módulo detecta automáticamente tu tarjeta microSD buscando en:
- `/storage/` (todas las carpetas excepto memoria interna)
- Rutas comunes: `/storage/sdcard1`, `/storage/extSdCard`, etc.
- Patrones UUID (XXXX-XXXX)

**No necesitas editar ningún archivo antes de compilar.**

### 📋 Otras Características
✅ Redirección automática de todos los archivos multimedia  
✅ Compatible con imágenes, videos, audios, documentos, stickers, GIFs  
✅ Mantiene visibilidad en WhatsApp  
✅ Crea automáticamente estructura de carpetas  
✅ Logs detallados para debugging  
✅ Verificación de disponibilidad de SD  
✅ Fallback inteligente si no hay SD

## 🔨 Compilar

### Con Android Studio:
1. File → Open → Selecciona carpeta `WhatsAppSDRedirect`
2. Espera sincronización de Gradle
3. Build → Build Bundle(s)/APK(s) → Build APK(s)
4. APK en: `app/build/outputs/apk/debug/app-debug.apk`

### Con línea de comandos:
```bash
cd WhatsAppSDRedirect
./gradlew assembleDebug
```
(Windows: `gradlew.bat assembleDebug`)

## 📲 Instalación

1. Instala el APK generado en tu móvil
2. Abre **LSPosed Manager**
3. Ve a **Módulos**
4. Activa **WhatsApp SD Redirect**
5. Toca el módulo → **Scope** → Marca solo `com.whatsapp`
6. Reinicia el dispositivo

## ✔️ Verificación

### Ver logs (IMPORTANTE):
1. LSPosed Manager → **Logs**
2. Busca `[WhatsAppSDRedirect]`
3. Verás si detectó tu SD:
   ```
   [WhatsAppSDRedirect] ✓ MicroSD detectada en: /storage/XXXX-XXXX
   [WhatsAppSDRedirect] ✓ Estructura creada en: /storage/XXXX-XXXX/WhatsApp/Media
   ```

### Comprobar funcionamiento:
1. Abre WhatsApp
2. Recibe una imagen/video
3. Verifica en los logs que se redirigió
4. Comprueba que los archivos están en la SD
5. Verifica que WhatsApp los muestra correctamente

### Desde ADB:
```bash
adb logcat | grep WhatsAppSDRedirect
```

## 🔍 Cómo Funciona la Detección

El módulo ejecuta estos pasos al iniciar:

1. **Escanea `/storage/`**: Busca todas las carpetas montadas
2. **Filtra memoria interna**: Ignora `emulated`, `sdcard0`, etc.
3. **Verifica permisos**: Solo usa carpetas con lectura/escritura
4. **Intenta rutas comunes**: Si no encuentra, prueba rutas estándar
5. **Busca por UUID**: Detecta patrones XXXX-XXXX típicos de SD
6. **Reporta resultado**: Todo queda registrado en los logs

## 🛠️ Solución de Problemas

**El módulo no detecta la SD:**
- Revisa los logs: verás qué carpetas encontró
- Verifica que la SD tiene permisos de escritura
- Asegúrate de que está formateada correctamente
- Prueba con otra SD si es posible

**Los archivos no se guardan en la SD:**
- Verifica los logs para ver la ruta detectada
- Comprueba espacio disponible en la SD
- Verifica permisos de la app WhatsApp

**WhatsApp no muestra los archivos:**
- Limpia caché de WhatsApp
- Verifica que la estructura de carpetas se creó
- Revisa logs de errores

## 📊 Logs de Ejemplo

```
[WhatsAppSDRedirect] Módulo cargado para WhatsApp
[WhatsAppSDRedirect] Buscando microSD en /storage/...
[WhatsAppSDRedirect]   ✗ Ignorando: /storage/emulated (memoria interna)
[WhatsAppSDRedirect]   ✓ SD encontrada: /storage/0000-0000
[WhatsAppSDRedirect] ✓ MicroSD detectada en: /storage/0000-0000
[WhatsAppSDRedirect] ✓ Estructura creada en: /storage/0000-0000/WhatsApp/Media
[WhatsAppSDRedirect]   ✓ Subcarpeta creada: WhatsApp Images
[WhatsAppSDRedirect]   ✓ Subcarpeta creada: WhatsApp Video
[WhatsAppSDRedirect] → Redirigido getExternalFilesDir: /storage/0000-0000/...
```

## ⚠️ Advertencias

- **BACKUP obligatorio** antes de activar
- La SD puede ser más lenta que memoria interna
- Actualizaciones de WhatsApp pueden romper compatibilidad
- No remuevas la SD con el módulo activo
- Software experimental, úsalo bajo tu responsabilidad

## 🆕 Ventajas vs Versión Manual

| Característica | Versión Anterior | Esta Versión |
|----------------|------------------|--------------|
| Configuración manual | ✗ Requerida | ✅ Automática |
| Detección de SD | ✗ No | ✅ Sí |
| Fallback inteligente | ✗ No | ✅ Sí |
| Logs detallados | ✓ Básicos | ✅ Completos |
| Facilidad de uso | ⚠️ Media | ✅ Alta |

## 📄 Licencia
Código abierto - Libre uso y modificación

## 👤 Autor
Elías Prieto Parrilla

---
**Versión**: 1.0 (Auto-detect)  
**Fecha**: Noviembre 2025
