# Single Responsibility Principle (SRP)

## What is SRP?

> A class should have **one responsibility** and therefore **one primary reason to change**.

SRP is about keeping a class focused on **one kind of job**.

---

## ❌ Without SRP

```java
class Invoice {

    public void calculateTotal() {
        // calculate invoice total
    }

    public void printInvoice() {
        // print invoice
    }

    public void saveToDatabase() {
        // save invoice
    }
}
```

Here, `Invoice` has **three different responsibilities**:

* Calculate the invoice
* Print the invoice
* Save the invoice to the database

Therefore, the class has **multiple reasons to change**.

For example:

```text
Calculation changes
        ↓
Invoice changes

Printing changes
        ↓
Invoice changes

Database changes
        ↓
Invoice changes
```

This makes the class harder to maintain.

---

## ✅ With SRP

We separate the responsibilities into different classes.

### 1. Invoice

```java
class Invoice {

    public void calculateTotal() {
        // calculate invoice total
    }
}
```

**Responsibility:** Calculate the invoice.

---

### 2. InvoicePrinter

```java
class InvoicePrinter {

    public void print(Invoice invoice) {
        // print invoice
    }
}
```

**Responsibility:** Print the invoice.

---

### 3. InvoiceRepository

```java
class InvoiceRepository {

    public void save(Invoice invoice) {
        // save invoice to database
    }
}
```

**Responsibility:** Save the invoice.

---

## How SRP separates the responsibilities

```text
Invoice
   ↓
Calculates invoice

InvoicePrinter
   ↓
Prints invoice

InvoiceRepository
   ↓
Saves invoice
```

Now each class has **one clear responsibility**.

---

## Why is SRP helpful?

### 1. Easier to maintain

Suppose the database changes from MySQL to MongoDB.

With SRP:

```text
Database changes
       ↓
InvoiceRepository changes
```

We don't need to modify `Invoice` or `InvoicePrinter`.

Similarly, if the printing format changes:

```text
Printing changes
       ↓
InvoicePrinter changes
```

Only the class responsible for printing needs to change.

---

### 2. Changes are isolated

Each responsibility is separated:

```text
Calculation change
        ↓
     Invoice


Printing change
        ↓
  InvoicePrinter


Database change
        ↓
 InvoiceRepository
```

A change in one responsibility is less likely to affect unrelated code.

---

### 3. Easier to test

We can test invoice calculation independently.

```java
Invoice invoice = new Invoice();

invoice.calculateTotal();
```

We don't need to involve:

* Database
* Printing
* PDF generation
* Email

This makes testing simpler.

---

### 4. Easier to understand

When we see:

```java
class InvoicePrinter
```

we immediately know:

> This class is responsible for printing invoices.

When we see:

```java
class InvoiceRepository
```

we know:

> This class is responsible for storing/retrieving invoices.

The class name and responsibility become clear.

---

## Important Point

SRP does **NOT** mean:

> A class should have only one method.

A class can have multiple methods as long as those methods belong to the **same responsibility**.

For example:

```java
class InvoiceRepository {

    public void save(Invoice invoice) {
    }

    public Invoice findById(int id) {
        return null;
    }

    public void delete(Invoice invoice) {
    }
}
```

This class has three methods, but they all deal with the same responsibility:

> **Invoice persistence**

So this class can still follow SRP.

---

## Simple way to identify SRP

Ask yourself:

> **"Does this class have more than one reason to change?"**

If the answer is **yes**, the class may be doing multiple responsibilities.

For example:

```text
Invoice
 ├── Calculate invoice
 ├── Print invoice
 └── Save invoice
```

There are three reasons to change.

So we separate them:

```text
Invoice
 └── Calculate invoice

InvoicePrinter
 └── Print invoice

InvoiceRepository
 └── Save invoice
```

Now each class has one primary reason to change.

---

## Remember

### SRP in one line:

> **One class → One cohesive responsibility → One primary reason to change.**

The important thing is **one responsibility**, not **one method**.
