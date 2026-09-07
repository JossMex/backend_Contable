# ⚖️ Portal Web Despacho Contable / Accounting Firm Web Portal

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-orange)
![Angular](https://img.shields.io/badge/Angular-17-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Docker](https://img.shields.io/badge/Docker-✓-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🇲🇽 Español

Portal web full stack para un despacho de asesoría fiscal y contable. Incluye un portal público para visitantes y un panel CMS completo para que el administrador gestione el contenido del sitio sin necesidad de programar.

### ✨ Funcionalidades

**Portal Público:**
- 🏠 **Página de inicio** — hero, servicios destacados y llamada a la acción
- 🛠️ **Servicios** — lista detallada de servicios contables
- 📝 **Blog** — artículos y recursos fiscales publicados
- 📬 **Contacto** — formulario que guarda mensajes en la base de datos

**Panel Admin CMS (protegido con JWT):**
- 🔐 **Login seguro** — autenticación con tokens JWT
- 📊 **Dashboard** — estadísticas en tiempo real
- 🛠️ **Gestión de servicios** — CRUD con toggle activo/inactivo
- 📝 **Gestión de artículos** — borrador/publicado con fecha automática
- 📬 **Bandeja de mensajes** — mensajes del formulario de contacto

### 🛠️ Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Frontend | Angular 17, Bootstrap 5, CSS Variables |
| Backend | Java 17, Spring Boot 3.2 |
| Seguridad | Spring Security, JWT (JJWT 0.12.3), BCrypt |
| Base de datos | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Contenedores | Docker |
| Control de versiones | Git / GitHub |

### 🏗️ Arquitectura del proyecto

```
┌─────────────────────────────────────────────────────┐
│              Angular (Puerto 4202)                   │
│                                                      │
│  Portal Público          Panel Admin CMS             │
│  / → Inicio              /admin/login                │
│  /servicios              /admin/dashboard            │
│  /blog                   /admin/servicios            │
│  /blog/:id               /admin/articulos            │
│  /contacto               /admin/mensajes             │
│                                                      │
│  AuthGuard ← Interceptor JWT (agrega token auto)    │
└─────────────────────────┬───────────────────────────┘
                          │ HTTP REST + JWT
┌─────────────────────────▼───────────────────────────┐
│           Spring Boot (Puerto 8084)                 │
│                                                      │
│  Rutas públicas (/api/public/)                      │
│    → servicios activos, artículos publicados        │
│    → recibir mensajes de contacto                   │
│                                                      │
│  Rutas admin (/api/admin/) — requieren JWT          │
│    → CRUD servicios, artículos, mensajes            │
│                                                      │
│  JwtFilter → Controller → Service → Repository     │
└─────────────────────────┬───────────────────────────┘
                          │ JPA / Hibernate
┌─────────────────────────▼───────────────────────────┐
│           MySQL 8.0 (Docker :3309)                  │
│  usuarios │ servicios │ articulos │ mensajes_contacto│
└─────────────────────────────────────────────────────┘
```

### 📁 Estructura del proyecto

```
despacho-contable/
├── backend/
│   └── src/main/java/com/despacho/backend/
│       ├── config/         → SecurityConfig, CorsConfig
│       ├── controller/     → Auth, Servicio, Articulo, MensajeContacto
│       ├── dto/            → LoginRequest, LoginResponse, RegisterRequest
│       ├── model/          → Usuario, Servicio, Articulo, MensajeContacto
│       ├── repository/     → Interfaces JPA
│       ├── security/       → JwtUtil, JwtFilter
│       └── service/        → Auth, Servicio, Articulo, MensajeContacto
└── frontend/
    └── src/app/
        ├── core/
        │   ├── services/     → AuthService, PortalService
        │   ├── guards/       → authGuard
        │   └── interceptors/ → authInterceptor
        ├── public/           → inicio, servicios, blog, contacto
        ├── admin/            → login, dashboard, servicios, articulos, mensajes
        └── shared/           → navbar, footer
```

### 🚀 Instalación y ejecución local

#### Prerrequisitos
- Java 17+
- Node.js 18+ y Angular CLI
- Docker Desktop

#### 1. Base de datos con Docker
```bash
docker run -d \
  --name mysql-despacho \
  -e MYSQL_ROOT_PASSWORD=root1234 \
  -e MYSQL_DATABASE=despacho_db \
  -p 3309:3306 \
  mysql:8.0
```

#### 2. Backend
```bash
cd backend
./mvnw spring-boot:run
```
Disponible en: `http://localhost:8084`

#### 3. Frontend
```bash
cd frontend
npm install
ng serve --port 4202
```
Disponible en: `http://localhost:4202`

#### 4. Crear usuario administrador
```bash
curl -X POST http://localhost:8084/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Admin","email":"admin@despacho.com","password":"admin123","rol":"ADMIN"}'
```

### 🔗 Endpoints de la API

#### Autenticación (públicos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Iniciar sesión → devuelve JWT |

#### Servicios públicos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/public/servicios` | Servicios activos ordenados |
| GET | `/api/public/articulos` | Artículos publicados |
| GET | `/api/public/articulos/{id}` | Detalle de artículo |
| POST | `/api/public/contacto` | Enviar mensaje de contacto |

#### Admin — Servicios (requieren JWT)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/servicios` | Todos los servicios |
| POST | `/api/admin/servicios` | Crear servicio |
| PUT | `/api/admin/servicios/{id}` | Actualizar servicio |
| PUT | `/api/admin/servicios/{id}/toggle` | Activar/desactivar |
| DELETE | `/api/admin/servicios/{id}` | Eliminar servicio |

#### Admin — Artículos (requieren JWT)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/articulos` | Todos los artículos |
| POST | `/api/admin/articulos` | Crear artículo |
| PUT | `/api/admin/articulos/{id}` | Actualizar artículo |
| PUT | `/api/admin/articulos/{id}/toggle` | Publicar/despublicar |
| DELETE | `/api/admin/articulos/{id}` | Eliminar artículo |

#### Admin — Mensajes (requieren JWT)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/admin/mensajes` | Todos los mensajes |
| GET | `/api/admin/mensajes/no-leidos` | Contador sin leer |
| PUT | `/api/admin/mensajes/{id}/toggle` | Marcar leído/no leído |
| DELETE | `/api/admin/mensajes/{id}` | Eliminar mensaje |

### 🔐 Flujo de autenticación JWT

```
1. POST /api/auth/login → devuelve token JWT
2. Angular guarda token en localStorage
3. AuthInterceptor agrega: Authorization: Bearer <token>
4. JwtFilter valida token en cada petición protegida
5. AuthGuard protege rutas del panel admin en Angular
```

### 🎨 Sistema de diseño

```css
/* Paleta corporativa */
--azul-primario:   #1A3A5C  /* color principal */
--azul-secundario: #2C5F8A  /* hover y variantes */
--acento:          #C8A96E  /* dorado para detalles */
--gris-claro:      #F7F9FC  /* fondos de sección */
```


## 🇺🇸 English

A full stack web portal for an accounting and tax advisory firm. Includes a public portal for visitors and a complete CMS admin panel for content management without coding.

### ✨ Features

**Public Portal:**
- 🏠 **Home page** — hero, featured services and call to action
- 🛠️ **Services** — detailed list of accounting services
- 📝 **Blog** — published tax and accounting articles
- 📬 **Contact** — form that saves messages to the database

**Admin CMS Panel (JWT protected):**
- 🔐 **Secure login** — JWT token authentication
- 📊 **Dashboard** — real-time statistics
- 🛠️ **Service management** — CRUD with active/inactive toggle
- 📝 **Article management** — draft/published with automatic date
- 📬 **Message inbox** — contact form messages

### 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 17, Bootstrap 5, CSS Variables |
| Backend | Java 17, Spring Boot 3.2 |
| Security | Spring Security, JWT (JJWT 0.12.3), BCrypt |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Containers | Docker |
| Version Control | Git / GitHub |

### 🚀 Local Setup

#### Prerequisites
- Java 17+
- Node.js 18+ and Angular CLI
- Docker Desktop

#### 1. Database with Docker
```bash
docker run -d \
  --name mysql-despacho \
  -e MYSQL_ROOT_PASSWORD=root1234 \
  -e MYSQL_DATABASE=despacho_db \
  -p 3309:3306 \
  mysql:8.0
```

#### 2. Run backend
```bash
cd backend
./mvnw spring-boot:run
```
Available at: `http://localhost:8084`

#### 3. Run frontend
```bash
cd frontend
npm install
ng serve --port 4202
```
Available at: `http://localhost:4202`

### 📄 License

This project is licensed under the MIT License.

---

Developed by **José Carlos Avila Martínez** — [GitHub](https://github.com/JossMex) · [LinkedIn](https://www.linkedin.com/in/carlos-avila561617163)
