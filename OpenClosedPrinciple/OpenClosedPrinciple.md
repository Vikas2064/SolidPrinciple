# Open/Closed Principle (OCP)

## What is OCP?

> **Software entities should be open for extension but closed for modification.**

In simple words:

* **Open for extension** → We should be able to add new behavior.
* **Closed for modification** → We should avoid modifying existing, working code when adding new behavior.

---

## ❌ Without OCP

Suppose we have an `InvoiceRepository` that supports different databases:

```java
class InvoiceRepository {

    public void save(Invoice invoice, String database) {

        if (database.equals("MYSQL")) {
            // Save to MySQL
        }
        else if (database.equals("MONGODB")) {
            // Save to MongoDB
        }
    }
}
```

Usage:

```java
InvoiceRepository repository = new InvoiceRepository();

repository.save(invoice, "MYSQL");
```

Now suppose we want to add PostgreSQL.

We have to **modify the existing class**:

```java
class InvoiceRepository {

    public void save(Invoice invoice, String database) {

        if (database.equals("MYSQL")) {
            // Save to MySQL
        }
        else if (database.equals("MONGODB")) {
            // Save to MongoDB
        }
        else if (database.equals("POSTGRESQL")) {
            // Save to PostgreSQL
        }
    }
}
```

If we later add Oracle, Redis, etc., we keep modifying the same class.

This goes against the idea of OCP.

---

# ✅ With OCP

Instead of putting all database implementations inside one class, create an abstraction.

```java
interface InvoiceRepository {

    void save(Invoice invoice);
}
```

Now create different implementations.

### MySQL

```java
class MySQLInvoiceRepository implements InvoiceRepository {

    @Override
    public void save(Invoice invoice) {
        // Save invoice to MySQL
    }
}
```

### MongoDB

```java
class MongoDBInvoiceRepository implements InvoiceRepository {

    @Override
    public void save(Invoice invoice) {
        // Save invoice to MongoDB
    }
}
```

---

## Using the Repository

The service depends on the `InvoiceRepository` abstraction, not on a specific database.

```java
class InvoiceService {

    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public void createInvoice(Invoice invoice) {
        repository.save(invoice);
    }
}
```

Now we can use MySQL:

```java
InvoiceRepository repository =
        new MySQLInvoiceRepository();

InvoiceService service =
        new InvoiceService(repository);

service.createInvoice(invoice);
```

Or MongoDB:

```java
InvoiceRepository repository =
        new MongoDBInvoiceRepository();

InvoiceService service =
        new InvoiceService(repository);

service.createInvoice(invoice);
```

---

# Adding a New Database

Suppose we now need PostgreSQL.

With OCP, we **don't modify**:

```text
InvoiceRepository
InvoiceService
```

Instead, we add a new implementation:

```java
class PostgreSQLInvoiceRepository
        implements InvoiceRepository {

    @Override
    public void save(Invoice invoice) {
        // Save invoice to PostgreSQL
    }
}
```

Now we can use it:

```java
InvoiceRepository repository =
        new PostgreSQLInvoiceRepository();

InvoiceService service =
        new InvoiceService(repository);

service.createInvoice(invoice);
```

We **extended** the system without modifying the existing business logic.

---

# How OCP Works

The structure looks like this:

```text
             InvoiceRepository
                    ↑
          ┌─────────┼─────────┐
          │         │         │
        MySQL     MongoDB   PostgreSQL
```

`InvoiceRepository` defines **what** needs to be done:

```java
void save(Invoice invoice);
```

The implementations decide **how** it is done:

```text
MySQLInvoiceRepository
        ↓
How to save in MySQL


MongoDBInvoiceRepository
        ↓
How to save in MongoDB


PostgreSQLInvoiceRepository
        ↓
How to save in PostgreSQL
```

---

# Why is OCP Helpful?

## 1. New functionality can be added easily

If we need another database:

```text
Oracle
Redis
PostgreSQL
MongoDB
```

we can create another implementation:

```java
class OracleInvoiceRepository
        implements InvoiceRepository {

    @Override
    public void save(Invoice invoice) {
        // Save to Oracle
    }
}
```

We don't need to modify the existing `InvoiceService`.

---

## 2. Existing code is less likely to break

When adding PostgreSQL, we don't touch:

```java
InvoiceService
```

or the existing:

```java
MySQLInvoiceRepository
MongoDBInvoiceRepository
```

We only add new code.

This reduces the chance of introducing bugs into already-working functionality.

---

## 3. Code becomes easier to extend

The design allows us to add new implementations:

```text
                    InvoiceRepository
                           ↑
             ┌─────────────┼─────────────┐
             │             │             │
           MySQL         MongoDB      PostgreSQL
```

Adding another database means adding another implementation.

---

# SRP vs OCP

These principles solve different problems.

### SRP asks:

> **Does this class have one responsibility?**

Example:

```text
InvoiceRepository
        ↓
Invoice persistence
```

### OCP asks:

> **Can I extend the behavior without modifying existing code?**

Example:

```text
             InvoiceRepository
                    ↑
          ┌─────────┼─────────┐
          │         │         │
        MySQL     MongoDB   PostgreSQL
```

---

# Remember

### OCP in one line:

> **Add new behavior by extending the existing design rather than repeatedly modifying existing code.**

### Simple mental model:

```text
❌ Without OCP

Need new database
       ↓
Modify existing code
       ↓
Add another if/else
       ↓
Repeat again and again


✅ With OCP

Need new database
       ↓
Create new implementation
       ↓
Existing code remains unchanged
```

**Open for extension + Closed for modification = Open/Closed Principle.**
