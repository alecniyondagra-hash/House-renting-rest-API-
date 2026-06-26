#  House Renting REST API

**UNILAK — Advanced Programming Final Project 2025/2026**
**Student:** Niyondagara Alec David | **Reg:** 22773/2023
**Lecturer:** NGIRIMANA Schadrack

---

##  Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.2.3 | Backend framework |
| Spring Security + JWT | Authentication & authorization |
| Spring Data JPA + Hibernate | Database ORM |
| MySQL | Database |
| Maven | Dependency management |
| JavaMailSender (Gmail SMTP) | OTP email delivery |
| Lombok | Reduce boilerplate code |

---

## ⚙️ How to Run the Project

### Requirements
- Java 17 installed
- MySQL running locally
- IntelliJ IDEA

### Steps

**1. Create the database**
```sql
CREATE DATABASE house_renting_db;
```

**2. Configure `application.properties`**
```properties
server.port=8080
server.servlet.context-path=/api/v1
spring.datasource.url=jdbc:mysql://localhost:3306/house_renting_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
jwt.secret=house_renting_secret_key_unilak_2025
jwt.expiration=86400000
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**3. Run the application**

Open the project in IntelliJ IDEA and click the green ▶️ Run button.

The API will start at:
```
http://localhost:8080/api/v1
```

---

##  Special Features (Beyond Basic Requirements)

### 🔐 OTP Expires in 10 Minutes
When a user signs up or requests a password reset, the OTP sent to their email is only valid for 10 minutes. After that, it is rejected automatically.

### 🔑 Password Strength Validation
Passwords must be at least 8 characters long and contain both letters and numbers. Weak passwords like `123456` or `abcdefgh` are rejected with a clear error message.

### 🔁 Forgot Password & Reset via Email
Users who forget their password can request a reset OTP to their email and set a new password — without needing admin help.

###  House Auto-Locks When Booked
When a landlord confirms a booking, the house automatically becomes **unavailable** and disappears from tenant search results. When the booking is cancelled, it becomes available again — fully automatic.

### 🔒 Tenant Cannot Cancel a Confirmed Booking
Once a landlord confirms a booking, the tenant cannot cancel it on their own. They must contact the landlord. This protects landlords from sudden cancellations.

###  Smart Search with Filters + Pagination
Tenants can search houses by location, price range, or both at the same time. Results are paginated to avoid overloading the response.

```
GET /houses?location=Kigali&minPrice=200&maxPrice=600&page=0&size=10
```

###  Tenants Only See Available Houses
Houses that are PENDING, REJECTED, or already rented are completely hidden from tenant search. Only APPROVED + available houses appear.

###  Landlord Booking Dashboard
Landlords can see all bookings across all their houses in one call, or filter by a specific house. They can confirm or cancel any booking directly.

---

## 🗂️ Project Structure

```
com.housereting.houserenting/
├── config/
│   └── SecurityConfig.java         — JWT security rules
├── controllers/
│   ├── AuthController.java         — signup, login, OTP, reset
│   ├── HouseController.java        — house CRUD + search
│   ├── BookingController.java      — booking management
│   └── AdminController.java        — admin controls
├── dto/
│   ├── request/                    — incoming request bodies
│   └── response/                   — outgoing response shapes
├── exception/
│   ├── GlobalExceptionHandler.java — handles all errors
│   ├── BadRequestException.java
│   ├── ForbiddenException.java
│   └── ResourceNotFoundException.java
├── models/
│   ├── User.java                   — users table
│   ├── House.java                  — houses table
│   └── Booking.java                — bookings table
├── repositories/
│   ├── UserRepository.java
│   ├── HouseRepository.java
│   └── BookingRepository.java
├── services/
│   ├── AuthService.java
│   ├── HouseService.java
│   ├── BookingService.java
│   └── AdminService.java
└── utils/
    ├── JwtUtil.java                — token generation & validation
    ├── JwtAuthFilter.java          — intercepts requests
    ├── UserDetailsServiceImpl.java — loads user from DB
    ├── OtpGenerator.java           — generates 6-digit OTP
    └── EmailService.java           — sends emails via Gmail
```

---

##  How It Works

### User Roles
| Role | What they can do |
|---|---|
| TENANT | Search houses, create bookings, cancel pending bookings |
| LANDLORD | Add houses, manage their own listings, confirm/cancel bookings |
| ADMIN | Approve/reject houses, manage all users |

### Flow: Signing Up
1. User sends signup request → account created, OTP sent to email
2. User verifies OTP (within 10 minutes) → account activated
3. User logs in → receives JWT token
4. All protected requests require: `Authorization: Bearer <token>`

### Flow: Renting a House
1. Landlord creates a house → status is PENDING
2. Admin approves the house → status becomes APPROVED
3. Tenant searches and finds the house → books it
4. Landlord confirms booking → house becomes unavailable automatically
5. If cancelled → house becomes available again automatically

---

## 📡 All API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | /auth/signup | Register new account |
| POST | /auth/verify-otp | Verify email with OTP |
| POST | /auth/login | Login and get JWT token |
| POST | /auth/forgot-password | Request password reset OTP |
| POST | /auth/reset-password | Reset password with OTP |

### Houses
| Method | Endpoint | Access |
|---|---|---|
| GET | /houses | Public — APPROVED + available only |
| GET | /houses/{id} | Public |
| GET | /houses?location=X&minPrice=X&maxPrice=X&page=X&size=X | Public |
| GET | /houses/my-houses | LANDLORD only |
| POST | /houses | LANDLORD only |
| PATCH | /houses/{id} | LANDLORD only |
| DELETE | /houses/{id} | LANDLORD only |

### Bookings
| Method | Endpoint | Access |
|---|---|---|
| POST | /bookings | TENANT only |
| GET | /bookings | TENANT — own bookings |
| DELETE | /bookings/{id} | TENANT — cancel pending booking |
| GET | /bookings/my-houses | LANDLORD — all bookings |
| GET | /bookings/house/{id} | LANDLORD — specific house |
| PATCH | /bookings/{id} | LANDLORD — confirm or cancel |

### Admin
| Method | Endpoint | Description |
|---|---|---|
| GET | /admin/users | All users |
| GET | /admin/houses | All houses |
| GET | /admin/houses?status=PENDING | Filter by status |
| PATCH | /admin/users/{id} | Change user role |
| PATCH | /admin/houses/{id} | Approve or reject house |
