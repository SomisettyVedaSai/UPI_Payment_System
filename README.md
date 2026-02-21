# 💳 UPI Payment System (Java Console Application)

A fully object-oriented UPI Payment System built using Java.  
This project simulates real-world digital payment operations such as UPI transfers, bill payments, recharges, transaction logging, and notifications.

The system is designed using clean architecture principles with interfaces, service layers, custom exceptions, and file handling for transaction storage.

---

## 🚀 Features

### 👤 User & Account Management
- Create Users
- Create Bank Accounts
- Link Bank Account to UPI Account
- Maintain Account Balance

### 💸 Payment Services
- UPI Money Transfer
- Mobile Recharge
- Electricity Bill Payment
- FASTag Recharge
- International Transfer

### 📜 Transaction Handling
- Transaction history stored in `transactions.txt`
- File-based transaction persistence
- Transaction object modeling

### 🔔 Notifications
- SMS Notification Service (Simulated)

### ⚠️ Exception Handling
- Custom Exception: `UPIException`
- `InsufficientBalanceException`
- `InvalidUPIPinException`
- Proper validation & secure transaction flow

---

## 🏗️ Project Structure

```
UPI_Payment_System
│
├── src
│   ├── main
│   │   └── MainApp.java
│   │
│   ├── model
│   │   ├── User.java
│   │   ├── Bank.java
│   │   ├── BankAccount.java
│   │   ├── UPIAccount.java
│   │   ├── Transaction.java
│   │   ├── PaymentRequest.java
│   │   ├── MobileRechargeRequest.java
│   │   ├── ElectricityBillRequest.java
│   │   ├── FastagRechargeRequest.java
│   │   ├── InternationalTransferRequest.java
│   │   └── UPIRegistry.java
│   │
│   ├── interfaces
│   │   ├── PaymentService.java
│   │   ├── BillPaymentService.java
│   │   └── NotificationService.java
│   │
│   ├── services
│   │   ├── UPIPaymentServiceImpl.java
│   │   ├── FileTransactionService.java
│   │   └── SMSNotificationService.java
│   │
│   └── exceptions
│       ├── UPIException.java
│       ├── InsufficientBalanceException.java
│       └── InvalidUPIPinException.java
│
└── transactions.txt
```

---

## 🧠 Core Concepts Used

- Object-Oriented Programming (OOP)
- Abstraction using Interfaces
- Inheritance
- Polymorphism
- Encapsulation
- Custom Exception Handling
- File Handling in Java
- Modular Programming (module-info.java)

---

## ⚙️ How to Run the Project

### 1️⃣ Open in IDE
Import the project into:
- IntelliJ IDEA
- Eclipse
- VS Code (with Java Extension)

### 2️⃣ Compile Using Terminal

```
javac -d bin src/**/*.java
```

### 3️⃣ Run the Application

```
java main.MainApp
```

Or directly run `MainApp.java` from your IDE.

---

## 🔐 Security Features

- UPI PIN validation
- Insufficient balance checks
- Controlled transaction flow
- Custom exception handling for secure operations

---

## 📂 Transaction Storage

All transactions are saved in:

```
transactions.txt
```

This ensures:
- Transaction persistence
- Record keeping
- File-based logging system

---

## 🎯 Learning Outcomes

This project demonstrates:

- Real-world fintech payment simulation
- Clean layered architecture
- Service-based design
- Proper exception handling
- File persistence mechanism
- Interface-driven development

Suitable for:
- Java OOP Practice
- Academic Projects
- Interview Preparation
- Backend System Design Basics

---

## 🔮 Future Enhancements

- Database Integration (MySQL)
- REST API conversion (Spring Boot)
- GUI using JavaFX / Swing
- Real SMS API integration
- Secure PIN encryption
- Admin dashboard

---

## 👨‍💻 Author

Somisetty Veda Sai  
B.Tech CSE Student  

---

## 📄 License

This project is developed for educational purposes only.
