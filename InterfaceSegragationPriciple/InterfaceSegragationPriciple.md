# Interface Segregation Principle (ISP)

## What is ISP?

> **Clients should not be forced to depend on methods they do not use.**

In simple words:

> **Don't create one large interface that forces every class to implement unnecessary methods. Instead, split it into smaller, focused interfaces.**

---

# ❌ Without ISP

Suppose we create one interface for all restaurant employees:

```java
interface RestaurantEmployee {

    void cook();

    void serveCustomer();

    void takeOrder();

    void cleanTable();

    void manageInventory();
}
```

Now suppose we create a `Chef`:

```java
class Chef implements RestaurantEmployee {

    @Override
    public void cook() {
        System.out.println("Chef is cooking");
    }

    @Override
    public void serveCustomer() {
        // Chef doesn't serve customers
    }

    @Override
    public void takeOrder() {
        // Chef doesn't take orders
    }

    @Override
    public void cleanTable() {
        // Chef doesn't clean tables
    }

    @Override
    public void manageInventory() {
        // Chef doesn't manage inventory
    }
}
```

The `Chef` is forced to implement methods that are not part of the Chef's actual responsibility.

We might even end up doing this:

```java
@Override
public void serveCustomer() {
    throw new UnsupportedOperationException();
}
```

This is a strong indication that the interface is too large.

---

# What is the Problem?

The `RestaurantEmployee` interface contains too many unrelated responsibilities:

```text
RestaurantEmployee
 ├── cook()
 ├── takeOrder()
 ├── serveCustomer()
 ├── cleanTable()
 └── manageInventory()
```

But different employees have different responsibilities.

### Chef

```text
Cook
```

### Waiter

```text
Take Order
Serve Customer
```

### Cleaner

```text
Clean Table
```

### Manager

```text
Manage Inventory
```

There is no reason to force every employee to implement every method.

---

# ✅ With ISP

Instead of one large interface, create smaller, focused interfaces.

## Cooking

```java
interface Cookable {

    void cook();
}
```

## Order Taking

```java
interface OrderTaker {

    void takeOrder();
}
```

## Customer Service

```java
interface CustomerService {

    void serveCustomer();
}
```

## Cleaning

```java
interface Cleanable {

    void cleanTable();
}
```

## Inventory Management

```java
interface InventoryManager {

    void manageInventory();
}
```

Now each employee implements only the interfaces they actually need.

---

# Chef

```java
class Chef implements Cookable {

    @Override
    public void cook() {
        System.out.println("Chef is cooking");
    }
}
```

The Chef only depends on:

```text
Cookable
```

The Chef does not need:

```text
OrderTaker
CustomerService
Cleanable
InventoryManager
```

---

# Waiter

A waiter takes orders and serves customers:

```java
class Waiter implements OrderTaker, CustomerService {

    @Override
    public void takeOrder() {
        System.out.println("Waiter is taking order");
    }

    @Override
    public void serveCustomer() {
        System.out.println("Waiter is serving customer");
    }
}
```

The waiter doesn't need:

```text
cook()
cleanTable()
manageInventory()
```

---

# Cleaner

```java
class Cleaner implements Cleanable {

    @Override
    public void cleanTable() {
        System.out.println("Cleaner is cleaning the table");
    }
}
```

The cleaner only implements the functionality it needs.

---

# Manager

```java
class Manager implements InventoryManager {

    @Override
    public void manageInventory() {
        System.out.println("Manager is managing inventory");
    }
}
```

---

# The Design

After applying ISP, the design looks like this:

```text
             Restaurant Employees
                     |
       ┌─────────────┼─────────────┐
       ↓             ↓             ↓
      Chef         Waiter        Cleaner
       |             |             |
   Cookable     OrderTaker      Cleanable
                  +
             CustomerService

                    Manager
                       |
               InventoryManager
```

Each interface represents a **small and focused capability**.

---

# Why is ISP Helpful?

## 1. No unnecessary methods

A Chef only implements:

```java
cook()
```

A Cleaner only implements:

```java
cleanTable()
```

They don't have to implement methods they don't need.

---

## 2. Easier to maintain

Suppose the order-taking functionality changes.

Only the classes using:

```java
OrderTaker
```

are affected.

The `Chef` doesn't need to change.

Similarly, if cleaning functionality changes:

```text
Cleanable
    ↓
Cleaner
```

The Chef and Waiter are not affected.

---

## 3. Smaller interfaces are easier to understand

Compare:

```text
❌ RestaurantEmployee
 ├── cook()
 ├── takeOrder()
 ├── serveCustomer()
 ├── cleanTable()
 └── manageInventory()
```

with:

```text
✅ Cookable
 └── cook()

OrderTaker
 └── takeOrder()

CustomerService
 └── serveCustomer()

Cleanable
 └── cleanTable()

InventoryManager
 └── manageInventory()
```

The second design makes each responsibility much clearer.

---

# How to Identify an ISP Violation

Ask:

> **"Is this class being forced to implement methods that it doesn't need?"**

For example:

```java
class Chef implements RestaurantEmployee
```

If the Chef has to implement:

```java
takeOrder()
serveCustomer()
cleanTable()
manageInventory()
```

even though it doesn't need them, the interface is probably too large.

The solution is to **split the interface**.

---

# SRP vs OCP vs LSP vs ISP

These principles solve different problems.

### SRP — Single Responsibility Principle

> **Does a class have one responsibility?**

```text
Invoice
   ↓
Calculate invoice

InvoicePrinter
   ↓
Print invoice
```

---

### OCP — Open/Closed Principle

> **Can we add new behavior without modifying existing code?**

```text
       InvoiceRepository
          /     |      \
       MySQL  MongoDB  PostgreSQL
```

---

### LSP — Liskov Substitution Principle

> **Can a child object safely replace its parent object?**

```text
          Bike
         /    \
    MotorBike Bicycle

Both can safely perform:
        ↓
       ride()
```

---

### ISP — Interface Segregation Principle

> **Are classes forced to depend on methods they don't need?**

```text
Chef
 ↓
Cookable

Waiter
 ↓
OrderTaker
 +
CustomerService

Cleaner
 ↓
Cleanable
```

---

# Remember

### ISP in one line:

> **Prefer many small, focused interfaces over one large, general-purpose interface.**

### Simple mental model:

```text
❌ Bad

One large interface
        ↓
Every class implements everything
        ↓
Many unnecessary methods


✅ Good

Small interfaces
        ↓
Each class chooses what it needs
        ↓
No unnecessary dependencies
```

**Interface Segregation Principle = Don't force a class to depend on methods it doesn't need.**
